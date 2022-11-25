 import java.util.*;

/**
 * A tiktaktoe game for Homework 01 & 02.
 * 
 * Some design decisions have been taken to introduce elements that will be useful
 * in more complex games, as well as in anticipation of a GUI interface to the game.
 * 
 * Row and column values are between 1 and SIZE (rather than following programming
 * conventions)
 * 
 * @author Giannis Terzakis
 * @version November 2022
 */
public class TikTakToe {
  /**
   * Default constructor
   */
  public TikTakToe() {
    cells = new Cell[SIZE+1][SIZE+1];
    for (int row=1; row<=SIZE; row++)
      for (int col=1; col<=SIZE; col++)
        cells[row][col] = new Cell(this, row, col);
  }
  
  /**
   * The TikTakToe game itself is not observable, but all the individual cells are.
   * This method adds the observer to all the cells.
   * 
   * @param o the observer
   */
  @SuppressWarnings("deprecation")
  void addObserver(Observer o) {
    if (o == null)
      throw new TikTakToeException("o is null");
    for (int row=1; row<=SIZE; row++) {
      for (int col=1; col<=SIZE; col++) {
        cells[row][col].addObserver(o);
      }
    }
  }
  
  /**
   * Retrieve a cell assignment (NOUGHT or CROSS), or EMPTY if not yet assigned
   * 
   * @param row the cell row
   * @param col the cell column
   * @return the assignment
   */
  public int getNum(int row, int col) {
    if ((row<1) || (row>SIZE))
      throw new TikTakToeException("invalid row (" + row + ")");
    if ((col<1) || (col>SIZE))
      throw new TikTakToeException("invalid col (" + col + ")");
    return cells[row][col].getNum();
  }
  
  /**
   * Has every cell in the game been assigned (given a single possible value)?
   * 
   * @return true if the game is fully assigned
   */
  public boolean isAssigned() {
    for (int row=1; row<=SIZE; row++)
      for (int col=1; col<=SIZE; col++)
        if (!cells[row][col].isAssigned())
          return false;
    return true;
  }
  
  /**
   * Has a cell in the game been assigned (given a single possible value)?
   * 
   * @param row the cell row
   * @param col the cell column
   * @return true if assigned
   */
  public boolean isAssigned(int row, int col) {
    if ((row<1) || (row>SIZE))
      throw new TikTakToeException("invalid row (" + row + ")");
    if ((col<1) || (col>SIZE))
      throw new TikTakToeException("invalid col (" + col + ")");
    return cells[row][col].isAssigned();
  }
  
  /**
   * Retrieve a copy of a cell set (possible assignments)
   * 
   * @param row the cell row
   * @param col the cell column
   * @return a copy of the cell set
   */
  public TreeSet<Integer> getSet(int row, int col) {
    if ((row<1) || (row>SIZE))
      throw new TikTakToeException("invalid row (" + row + ")");
    if ((col<1) || (col>SIZE))
      throw new TikTakToeException("invalid col (" + col + ")");
    return cells[row][col].getSet();
  }

  /**
   * Is this a possible assignment (NOUGHT or CROSS) for the cell?
   * 
   * @param row the cell row
   * @param col the cell column
   * @param num the possible assignment
   * @return true if the cell set contains this possible assignment
   */
  public boolean isValidAssign(int row, int col, int num) {
    if ((row<1) || (row>SIZE))
      throw new TikTakToeException("invalid row (" + row + ")");
    if ((col<1) || (col>SIZE))
      throw new TikTakToeException("invalid col (" + col + ")");
    if ((num != NOUGHT) && (num != CROSS))
      throw new TikTakToeException("invalid number (" + num + ")");
    if (isWin() || isDraw())
      return false;
    return cells[row][col].contains(num);
  }
  
  /**
   * Is this a possible assignment (NOUGHT or CROSS) for the cell?
   * 
   * @param move the possible assignment
   * @return true if the cell set contains this possible assignment
   */
  public boolean isValidAssign(Assign move) {
    if (move == null)
      throw new TikTakToeException("cannot have null move");
    return isValidAssign(move.getRow(), move.getCol(), move.getNum());
  }
  
  /**
   * Assign a cell to  a single value (NOUGHT or CROSS)
   * 
   * @param row the cell row
   * @param col the cell column
   * @param num the assigned value
   */
  
  void assign(int row, int col, int num) {
    if ((row<1) || (row>SIZE))
      throw new TikTakToeException("invalid row (" + row + ")");
    if ((col<1) || (col>SIZE))
      throw new TikTakToeException("invalid col (" + col + ")");
    if ((num != NOUGHT) && (num != CROSS))
      throw new TikTakToeException("invalid number (" + num + ")");
    if (isWin())
      throw new TikTakToeException("game already won");
    if (isDraw())
      throw new TikTakToeException("game already drawn");
    if (!isValidAssign(row, col, num))
      throw new TikTakToeException("invalid assign (" + row + "," + col + "," + num + ")");
    cells[row][col].assign(num);
  }
  
  /**
   * Assign a cell to  a single value (NOUGHT or CROSS)
   * 
   * @param move the assignment
   */
  
  void assign(Assign move) {
    if (move == null)
      throw new TikTakToeException("cannot have null move");
    assign(move.getRow(), move.getCol(), move.getNum());
  }
  
  /**
   * Retreive a vector of all possible assignments (moves) remaining in the game for NOUGHT or CROSS
   * 
   * @param num either NOUGHT or CROSS
   * @return a vector of possible assignments (moves)
   */
  public Vector<Assign> getChoices(int num) {
    if ((num != NOUGHT) && (num != CROSS))
      throw new TikTakToeException("invalid number (" + num + ")");
    Vector<Assign> choices = new Vector<Assign>();
    for (int row=1; row<=SIZE; row++) {
      for (int col=1; col<=SIZE; col++) {
        Cell c = cells[row][col];
        if (!c.isAssigned()) {
          TreeSet<Integer>  set = c.getSet();
          Iterator<Integer> itr = set.iterator();
          while (itr.hasNext()) {
            if (itr.next() == num)
              choices.add(new Assign(row, col, num));
          }
        } 
      }
    }
    return choices;
  }
  
  /**
   * Check if the game has been won (setting the game state accordingly)
   * 
   * @return true if won
   */
  public boolean checkWin() {
    if (isWin())
      throw new TikTakToeException("game already won");
    int row, col;
    // check rows
    for (row=1; row<=SIZE; row++) {
      TreeSet<Integer> rowSet = new TreeSet<Integer>();
      for (col=1; col<=SIZE; col++) {
        rowSet.addAll(cells[row][col].getSet());
      }
      if (rowSet.size() == 1) {
        win = rowSet.first();
        trace("row:"+row+"; win:"+winToString());
        return true;
      }
    }
    // check cols
    for (col=1; col<=SIZE; col++) {
      TreeSet<Integer> colSet = new TreeSet<Integer>();
      for (row=1; row<=SIZE; row++) {
        colSet.addAll(cells[row][col].getSet());
      }
      if (colSet.size() == 1) {
        win = colSet.first();
        trace("col:"+col+"; win:"+winToString());
        return true;
      }
    }
    //forward diagonal
    TreeSet<Integer> set = new TreeSet<Integer>();
    for (row=1; row<=SIZE; row++) {
      set.addAll(cells[row][row].getSet());
    }
    if (set.size() == 1) {
      win = set.first();
      trace("forward diag win:"+winToString());
      return true;
    }
    //reverse diagonal
    set = new TreeSet<Integer>();
    for (row=1; row<=SIZE; row++) {
      set.addAll(cells[row][SIZE+1-row].getSet());
    }
    if (set.size() == 1) {
      win = set.first();
      trace("reverse diag win:"+winToString());
      return true;
    }
    return false;
  }
  
  /**
   * Has the game been won?
   * 
   * @return true if won
   */
  public boolean isWin() {
    return win != EMPTY;
  }
  
  /**
   * Has the game been drawn?
   * 
   * @return true if a draw
   */
  public boolean isDraw() {
    return isAssigned();
  }
  
  /**
   * String representation of the game (useful for debugging)
   * 
   * @return the String representation
   */
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("TikTakToe("+ SIZE + ",");
    buf.append(winToString() + ",");
    buf.append((isDraw() ? "1" : "0") + ",\n");
    for (int row=1; row<=SIZE; row++)
      for (int col=1; col<=SIZE; col++) {
        buf.append(cells[row][col]);
        if ((row != SIZE) || (col != SIZE))
          buf.append(",");
        if (col == SIZE)
          buf.append("\n");
      }
    buf.append(")");
    return buf.toString();
  }
  
  /**
   * The winner (if there is one) of the game as a string
   * 
   * @return as a string, NOUGHT, CROSS or EMPTY (if no winner yet)
   */
  public String winToString() {
    return winToString(win);
  }
  
  /**
   * Convert an integer representing the winner (if there is one) of the game into a string
   * 
   * @param num the winner (NOUGHT, CROSS or EMPTY)
   * @return the string representation
   */
  public static String winToString(int num) {
    if ((num != TikTakToe.EMPTY) && (num != TikTakToe.NOUGHT) && (num != TikTakToe.CROSS))
      throw new TikTakToeException("invalid num (" + num + ")");
    String[] ws = {EMPTY_STR,NOUGHT_STR,CROSS_STR};
    return ws[num+1];
  }
  
  /**
   * A trace method for debugging (active when traceOn is true)
   * 
   * @param s the string to output
   */
  public static void trace(String s) {
    if (traceOn)
      System.out.println("trace: " + s);
  }
  
  public static void main(String[] args) {
    TikTakToe ttt = new TikTakToe();
    int next = NOUGHT;
    Player p = new Player();
    while (!ttt.isWin() && !ttt.isDraw()) {
      Vector<Assign> choices = ttt.getChoices(next);
      Assign move = p.move(choices);
      ttt.assign(move);
      System.out.println(ttt);
      next = next == NOUGHT ? CROSS : NOUGHT;
    }
  }
  
  private int              win     = EMPTY;  // tracks the winner, if any
  private Cell[][]         cells   = null;
  
  public static final int    EMPTY      = -1;
  public static final int    NOUGHT     = 0;
  public static final int    CROSS      = 1;
  public static final String EMPTY_STR  = "_";
  public static final String NOUGHT_STR = "O";
  public static final String CROSS_STR  = "X";
  public static final int    SIZE       = 3;
  
  private static boolean   traceOn = false; // for debugging
}

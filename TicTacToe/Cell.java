 import java.util.*;

/**
 * A cell in a tiktaktoe game for Homework 01 & 02.
 * 
 * The cell uses a TreeSet to represent possible values (NOUGHT and CROSS), as this is an approach that
 * would be applicable in more complex games. Row and column range from 1 to TikTakToe.SIZE (usually 3).
 * The Cell is Observable in anticipation of a GUI interface to the game.
 * 
 * @author Giannis Terzakis
 * @version November 2022
 */
@SuppressWarnings("deprecation")
public class Cell extends Observable {
  /**
   * A cell in a game of tiktaktoe
   * 
   * @param g the tiktaktoe game
   * @param r the row in the game
   * @param c the column in the game
   */
  public Cell(TikTakToe g, int r, int c) {
    if (g == null)
      throw new TikTakToeException("g is null");
    if ((r<1) || (r>TikTakToe.SIZE))
      throw new TikTakToeException("invalid row (" + r + ")");
    if ((c<1) || (c>TikTakToe.SIZE))
      throw new TikTakToeException("invalid col (" + c + ")");
    grid = g;
    row  = r;
    col  = c;
    set  = new TreeSet<Integer>();
    set.add(TikTakToe.NOUGHT);
    set.add(TikTakToe.CROSS);
  }
  
  /**
   * Test equality of two cells
   * 
   * @return true if equal
   */
  @Override
  public boolean equals(Object obj) {
    if ((obj != null) && (obj instanceof Cell)) {
      Cell c = (Cell) obj;
      if ((c.row == row) && (c.col == col))
        return true;
    }
    return false;
  }
  
  /**
   * Retrieve the cell row
   * 
   * @return the row
   */
  public int getRow() {
    return row;
  }

  /**
   * Retrieve the cell column
   * 
   * @return the column
   */
  public int getCol() {
    return col;
  }
  
  /**
   * Retrieve the cell set size (number of possible assignments)
   * 
   * @return the cell set size
   */
  public int size() {
    return set.size();
  }
  
  /**
   * Retrieve the cell assignment (NOUGHT or CROSS), or EMPTY if not yet assigned
   * 
   * @return the assignment
   */
  public int getNum() {
    if (isAssigned())
      return set.first();
    return TikTakToe.EMPTY;
  }
  
  /**
   * Retrieve a copy of the cell set (possible assignments)
   * 
   * @return a copy of the cell set
   */
  public TreeSet<Integer> getSet() {
    return new TreeSet<Integer>(set);
  }
  
  /**
   * Is this a possible assignment (NOUGHT or CROSS) for the cell (does the cell contain this possible assignment)?
   * 
   * @param num the possible assignment
   * @return true if the set contains this possible assignment
   */
  public boolean contains(int num) {
    if ((num != TikTakToe.NOUGHT) && (num != TikTakToe.CROSS))
      throw new TikTakToeException("invalid number (" + num + ")");
    return set.contains(num);
  }
  
  /**
   * Has the cell been assigned (given a single possible value)?
   * 
   * @return true if assigned
   */
  public boolean isAssigned() {
    return size() == 1;
  }
  
  /**
   * Assign the cell to  a single value (NOUGHT or CROSS)
   * 
   * @param num the assigned value
   */
  void assign(int num) {
    if ((num != TikTakToe.NOUGHT) && (num != TikTakToe.CROSS))
      throw new TikTakToeException("invalid number (" + num + ")");
    if (!set.contains(num))
      throw new TikTakToeException("set doesn't contain number (" + num + ")");
    if (isAssigned())
      throw new TikTakToeException("cell already assigned");
    set.clear();
    set.add(num);
    grid.checkWin();
    setChanged();
    notifyObservers(this);
  }
  
  /**
   * String representation of the cell (useful for debugging)
   * 
   * @return the String representation
   */
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("Cell(" + row + "," + col + ",[");
    buf.append(set.contains(TikTakToe.NOUGHT) ? TikTakToe.NOUGHT_STR : "");
    buf.append(size() == 2 ? "," : "");
    buf.append(set.contains(TikTakToe.CROSS) ? TikTakToe.CROSS_STR : "");
    buf.append("])");
    return buf.toString();
  }

  private TikTakToe        grid = null;
  private int              row;
  private int              col;
  private TreeSet<Integer> set = null;
}

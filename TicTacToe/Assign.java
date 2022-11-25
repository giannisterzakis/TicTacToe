 import java.util.*;

/**
 * A cell assignment (i.e. a move) in the game of tiktaktoe for Homework 01 & 02
 * 
 * @author Giannis Terzakis
 * @version November 2022
 */
public class Assign {
  /**
   * Constructor
   * 
   * @param r the row of the cell to be assigned
   * @param c the column of the cell to be assigned
   * @param n the assignment (will be NOUGHT or CROSS)
   */
  public Assign(int r, int c, int n) {
    if ((r<1) || (r>TikTakToe.SIZE))
      throw new TikTakToeException("invalid row (" + r + ")");
    if ((c<1) || (c>TikTakToe.SIZE))
      throw new TikTakToeException("invalid col (" + c + ")");
    if ((n != TikTakToe.NOUGHT) && (n != TikTakToe.CROSS))
      throw new TikTakToeException("invalid n (" + n + ")");
    row = r;
    col = c;
    num = n;
  }
  
  /**
   * Constructor from a Scanner
   * 
   * @param scnr the Scanner
   */
  public Assign(Scanner scnr) {
    if (scnr == null)
      throw new TikTakToeException("scnr is null");
    if (!scnr.hasNextInt())
      throw new TikTakToeException("expecting row");
    int r = scnr.nextInt();
    if ((r<1) || (r>TikTakToe.SIZE))
      throw new TikTakToeException("invalid row (" + r + ")");
    if (!scnr.hasNextInt())
      throw new TikTakToeException("expecting col");
    int c = scnr.nextInt();
    if ((c<1) || (c>TikTakToe.SIZE))
      throw new TikTakToeException("invalid col (" + c + ")");
    if (!scnr.hasNextInt())
      throw new TikTakToeException("expecting num");
    int n = scnr.nextInt();
    if ((n != TikTakToe.NOUGHT) && (n != TikTakToe.CROSS))
      throw new TikTakToeException("invalid n (" + n + ")");
    row = r;
    col = c;
    num = n;
    scnr.hasNextLine(); // clear the line
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
   * Retrieve the assignment (NOUGHT or CROSS)
   * 
   * @return the assignment
   */
  public int getNum() {
    return num;
  }
  
  /**
   * Test equality of two assignments
   * 
   * @return true if equal
   */
  @Override
  public boolean equals(Object obj) {
    if ((obj != null) && (obj instanceof Assign)) {
      Assign a = (Assign) obj;
      if ((a.row == row) && (a.col == col) && (a.num == num))
        return true;
    }
    return false;
  }
  
  /**
   * String representation of the assignment (useful for debugging)
   * 
   * @return the String representation
   */
  @Override
  public String toString() {
    StringBuffer buf = new StringBuffer();
    buf.append("Assign(" + row + "," + col + "," + TikTakToe.winToString(num) + ")");
    return buf.toString();
  }
  
  /**
   * String representation of the assignment for file storage
   * 
   * @return the String representation
   */
  public String toStringForFile() {
    StringBuffer buf = new StringBuffer();
    buf.append(row + " " + col + " " + num);
    return buf.toString();
  }
  
  private int row = 0;
  private int col = 0;
  private int num = 0;
}

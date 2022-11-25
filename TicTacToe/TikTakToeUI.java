import java.util.*;
import java.io.*;

/**
 * A text based user interface to the tiktaktoe game for Homework 01 & 02
 * 
 * @author Giannis Terzakis
 * @version November 2022
 */
public class TikTakToeUI {
  /**
   * Default constructor
   */
  public TikTakToeUI() {
    scnr   = new Scanner(System.in);
    game   = new TikTakToe();
    player = new Player();
    stack  = new Stack<Assign>();
  }
  
  /**
   * Main control loop.  This displays the game, then enters a loop displaying a menu,
   * getting the user command, executing the command, displaying the game and checking
   * for win or draw conditions
   */
  public void menu() {
    String command = "";
    displayGame();
    while (!command.equalsIgnoreCase("Quit") && !game.isWin() && !game.isDraw())  {
      displayMenu();
      command = getCommand();
      execute(command);
      displayGame();
      checkWin();
    }
  }

  /**
   * Display the tiktaktoe game on the console
   */
  private void displayGame() {
    for (int row=1; row<=TikTakToe.SIZE; row++) {
      for (int col=1; col<=TikTakToe.SIZE; col++) {
        System.out.print(TikTakToe.winToString(game.getNum(row, col)));
      }
      System.out.println();
    }
  }
  
  /**
   * Display the user menu
   */
  private void displayMenu()  {
    System.out.println( "Commands are (player is " + TikTakToe.NOUGHT_STR + ")");
    System.out.println("   Next move         [Move]");
    System.out.println("   Undo move         [Undo]");
    System.out.println("   Restart game     [Clear]");
    System.out.println("   Save game         [Save]");
    System.out.println("   Load game         [Load]");    
    System.out.println("   To end program    [Quit]");    
  }

  /**
   * Get the user command
   * 
   * @return the user command string
   */
  private String getCommand() {
    System.out.print ("Enter command: ");
    return scnr.nextLine();
  }

  /**
   * Execute the user command string
   * 
   * @param command the user command string
   */
  private void execute(String command) {
    if (command.equalsIgnoreCase("Quit")) {
      System.out.println("Program closing down");
      System.exit(0);
    } else if (command.equalsIgnoreCase("Move")) {
      move();
    } else if (command.equalsIgnoreCase("Undo")) {
      undo();
    } else if (command.equalsIgnoreCase("Clear")) {
      clear();
    } else if (command.equalsIgnoreCase("Save")) {
      save();
    } else if (command.equalsIgnoreCase("Load")) {
      load();
    } else {
      System.out.println("Unknown command (" + command + ")");
    }
  }
  
  /**
   * Make the user and computer moves.  The user move is requested from
   * the user, and then the computer player makes its move (if the game
   * has not yet been won or drawn)
   */
  private void move() {
    // user move
    Assign userMove = getUserMove();
    if (userMove == null || !game.isValidAssign(userMove)) {
      System.out.println("invalid user move");
      return;
    }
    game.assign(userMove);
    stack.push(userMove);
    trace("stack after userMove: " + stack);
    if (game.isWin() || game.isDraw())
      return;
    // computer move
    Vector<Assign> choices = game.getChoices(TikTakToe.CROSS);
    Assign compMove = player.move(choices);
    game.assign(compMove);
    stack.push(compMove);
    trace("stack after compMove: " + stack);
  }
  
  /**
   * Inform the user of a win or draw
   */
  private void checkWin() {
    if (game.isWin()) {
      System.out.println("Winner is: " + game.winToString());
    } else if (game.isDraw()) {
      System.out.println("Draw");
    }
  }

  /**
   * Get the user's move
   * 
   * @return the user move
   */
  private Assign getUserMove() {
    System.out.print ("Enter row (1 to " + TikTakToe.SIZE + ": ");
    if (!scnr.hasNextInt()) {
      scnr.nextLine(); // clear the line
      return null;
    }
    int row = scnr.nextInt();
    scnr.nextLine(); // clear the line
    if ((row<1) || (row>TikTakToe.SIZE)) {
      return null;
    }
    System.out.print ("Enter col (1 to " + TikTakToe.SIZE + ": ");
    if (!scnr.hasNextInt()) {
      scnr.nextLine(); // clear the line
      return null;
    }
    int col = scnr.nextInt();
    scnr.nextLine(); // clear the line
    if ((col<1) || (col>TikTakToe.SIZE))
      return null;
    return new Assign(row, col, TikTakToe.NOUGHT);
  }
  
  /**
   * Undo the last user and (if necessary) computer move
   */
  private void undo() {
    if (stack.empty())
      return;
    Assign top = stack.pop();              // discard last assign
    if (top.getNum() == TikTakToe.CROSS) { // computer move
      top = stack.pop();                   // discard user move
    }
    Stack<Assign> oldStack = stack;        // otherwise stack will be lost
    clear();
    for (Assign a : oldStack) {            // rebuild puzzle without last user move
      game.assign(a);
      stack.push(a);
    }
  }
  
  /**
   * Restart the game
   */
  private void clear() {
    game  = new TikTakToe();
    stack = new Stack<Assign>();
  }
  
  /**
   * Save the game to a text file (FILENAME)
   */
  private void save() {
    try {
      PrintStream ps = new PrintStream(new File(FILENAME));
      for (Assign a : stack)
        ps.println(a.toStringForFile());
      ps.close();
      System.out.println("game saved to file");
    } catch (IOException e) {
      System.out.println("an input output error occurred");
    }    
  }
  
  /**
   * Load the game from a text file (FILENAME)
   */
  private void load() {
    try {
      Scanner fscnr = new Scanner(new File(FILENAME));
      clear();
      while (fscnr.hasNextInt()) {
        Assign a = new Assign(fscnr);
        game.assign(a);
        stack.push(a);
      }
      fscnr.close();
      System.out.println("game loaded from file");
    } catch (IOException e) {
      System.out.println("an input output error occurred");
    } 
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
    TikTakToeUI ui = new TikTakToeUI();
    ui.menu();
  }

  private Scanner          scnr   = null;
  private TikTakToe        game   = null;
  private Player           player = null;
  private Stack<Assign>    stack  = null;
  
  private static final String FILENAME = "tiktaktoe.txt"; 
  
  private static boolean   traceOn = false; // for debugging
}

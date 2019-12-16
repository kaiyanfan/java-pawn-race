import java.util.Arrays;
import java.util.Scanner;

public class PawnRace {

  public static void main(String[] args) {
    String[] validFiles = {"A", "B", "C", "D", "E", "F", "G", "H"};
    String[] validPlayers = {"C", "P"};

    assert (args.length == 4) : "Invalid number of arguments!";

    assert Arrays.asList(validPlayers).contains(args[0].toUpperCase());
    assert Arrays.asList(validPlayers).contains(args[1].toUpperCase());
    assert Arrays.asList(validFiles).contains(args[2].toUpperCase());
    assert Arrays.asList(validFiles).contains(args[3].toUpperCase());

    boolean isWhiteAI = args[0].toUpperCase().equals("C");
    boolean isBlackAI = args[1].toUpperCase().equals("C");
    Board board = new Board(args[2].toUpperCase().charAt(0), args[3].toUpperCase().charAt(0));
    Game game = new Game(board);
    Player wPlayer = new Player(game, Color.WHITE, isWhiteAI);
    Player bPlayer = new Player(game, Color.BLACK, isBlackAI);
    Player cPlayer;

    while (!game.isFinished()) {
      if (game.getCurrentPlayer() == Color.WHITE) {
        cPlayer = wPlayer;
      } else {
        cPlayer = bPlayer;
      }

      if (!cPlayer.isComputerPlayer()) {
        game.getBoard().display();
        System.out.print(Color.strOf(cPlayer.getColor()));
        System.out.print(", it's your turn! Input a move: ");
        getMove(game);
      } else {
        long startTime = System.nanoTime();
        System.out.println();
        System.out.println("\nThe stupid AI is thinking...\n");
        Move move;
        move = AI.aiMove(cPlayer, 7);
        game.applyMove(move);
        System.out.print("AI decides: ");
        System.out.println(move.getSAN());
        long endTime = System.nanoTime();
        System.out.println(
            "This step takes the stupid ai " + (endTime - startTime) / 1000000 + "ms to decide\n");
      }
    }

    board.display();
    if (game.getGameResult() == Color.WHITE) {
      System.out.println("White wins!");
    }
    if (game.getGameResult() == Color.BLACK) {
      System.out.println("Black wins!");
    }
    if (game.getGameResult() == Color.NONE) {
      System.out.println("Game draws!");
    }
  }

  private static void getMove(Game game) {
    Scanner scanner = new Scanner(System.in);
    String nextMove = scanner.next();

    while (game.parseMove(nextMove) == null) {
      System.out.print("Invalid move! Try to input again: ");
      nextMove = scanner.next();
    }

    game.applyMove(game.parseMove(nextMove));
  }
}

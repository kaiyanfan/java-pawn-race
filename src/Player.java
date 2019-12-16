import java.util.ArrayList;
import java.util.List;

public class Player {

  private Game game;
  private int color;
  private boolean isComputerPlayer;

  public Player(Game game, int color, boolean isComputerPlayer) {
    this.game = game;
    this.color = color;
    this.isComputerPlayer = isComputerPlayer;
  }

  public int getColor() {
    return color;
  }

  public boolean isComputerPlayer() {
    return isComputerPlayer;
  }

  public Game getGame() {
    return game;
  }

  public Board getBoard() {
    return game.getBoard();
  }

  public static List<Square> getAllPawns(Board board, int color) {
    List<Square> pawns = new ArrayList<>();
    int i, j;

    if (color == Color.BLACK) {
      for (i = 0; i < 8; i++) {
        for (j = 0; j < 8; j++) {
          if (board.getSquare(j, i).occupiedBy() == color) {
            pawns.add(board.getSquare(j, i));
          }
        }
      }
    } else {
      for (i = 7; i >= 0; i--) {
        for (j = 7; j >= 0; j--) {
          if (board.getSquare(j, i).occupiedBy() == color) {
            pawns.add(board.getSquare(j, i));
          }
        }
      }
    }

    return pawns;
  }

  public static List<Move> getAllValidMoves(Board board, int color, Game game) {
    List<Move> moves = new ArrayList<>();

    for (Square pawn : getAllPawns(board, color)) {
      int x = pawn.getX();
      int y = pawn.getY();
      Square forward = board.getSquare(x, y + color);
      Square doubleForward = board.getSquare(x, y + color + color);
      Square captureL = board.getSquare(x - 1, y + color);
      Square captureR = board.getSquare(x + 1, y + color);

      // Move forward by 1 square
      if (forward.occupiedBy() == Color.NONE) {
        moves.add(new Move(pawn, forward, false, false));
      }

      // Move forward by 2 squares
      if (forward.occupiedBy() == Color.NONE && doubleForward != null &&
          doubleForward.occupiedBy() == Color.NONE) {
        if (color == Color.WHITE && y == 1 || color == Color.BLACK && y == 6) {
          moves.add(new Move(pawn, doubleForward, false, false));
        }
      }

      // Capture opponent's prawn
      if (captureL != null && captureL.occupiedBy() == -color) {
        moves.add(new Move(pawn, captureL, true, false));
      }
      if (captureR != null && captureR.occupiedBy() == -color) {
        moves.add(new Move(pawn, captureR, true, false));
      }

      // En Passant Rule
      if (game.getLastMove() != null) {
        int fromY = game.getLastMove().getFrom().getY();
        int toY = game.getLastMove().getTo().getY();
        int fromX = game.getLastMove().getFrom().getX();

        // 1 and 6 are the rows black and white pawns initially locates respectively
        // 3 and 4 are the rows black and white pawns would locate if it moves by 2 squares
        if (captureL != null && captureL.occupiedBy() == Color.NONE) {
          if ((fromY == 6 && toY == 4 && fromX == x - 1 && y == 4 && color == Color.WHITE) ||
              (fromY == 1 && toY == 3 && fromX == x - 1 && y == 3 && color == Color.BLACK)) {
            moves.add(new Move(pawn, captureL, true, true));
          }
        }

        if (captureR != null && captureR.occupiedBy() == Color.NONE) {
          if ((fromY == 6 && toY == 4 && fromX == x + 1 && y == 4 && color == Color.WHITE) ||
              (fromY == 1 && toY == 3 && fromX == x + 1 && y == 3 && color == Color.BLACK)) {
            moves.add(new Move(pawn, captureR, true, true));
          }
        }
      }
    }
    return moves;
  }

  public void makeMove(Move move) {
    game.applyMove(move);
  }
}

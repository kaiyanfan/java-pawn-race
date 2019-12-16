import java.util.Random;

public class Game {

  private static int MOVEARRAYLENGTH = 1000;
  private Move[] moves;
  private int i, player;
  private Board board;

  public Game(Board board) {
    moves = new Move[MOVEARRAYLENGTH];
    i = 0;
    player = Color.WHITE;
    this.board = board;
  }

  public Board getBoard() {
    return board;
  }

  public int getCurrentPlayer() {
    return player;
  }

  public int getMoveNum() {
    return i;
  }

  public Move getLastMove() {
    return moves[i];
  }

  public void applyMove(Move move) {
    i++;
    moves[i] = move;
    board.applyMove(move);
    player = -player;
  }

  public void unapplyMove() {
    if (i == 0) {
      return;
    }

    Move move = moves[i];
    board.unapplyMove(move);
    i--;
    player = -player;
  }

  public boolean isFinished() {
    if (i == 0) {
      return false;
    }

    boolean winW =
        (moves[i].getTo().getY() == Color.WHITETARGET) || (Player.getAllPawns(board, Color.BLACK)
            .isEmpty());
    boolean winB =
        (moves[i].getTo().getY() == Color.BLACKTARGET) || (Player.getAllPawns(board, Color.WHITE)
            .isEmpty());

    return winW || winB || (Player.getAllValidMoves(board, player, this).isEmpty());
  }

  public int getGameResult() {

    if ((moves[i].getTo().getY() == Color.WHITETARGET) || (Player.getAllPawns(board, Color.BLACK)
        .isEmpty())) {
      return Color.WHITE;
    }
    if ((moves[i].getTo().getY() == Color.BLACKTARGET) || (Player.getAllPawns(board, Color.WHITE)
        .isEmpty())) {
      return Color.BLACK;
    }

    return Color.NONE;
  }

  public Move parseMove(String san) {
    for (Move m : Player.getAllValidMoves(board, player, this)) {
      if (m.getSAN().equals(san)) {
        return m;
      }
    }
    return null;
  }

  public int naiveEvaluation(int color) {
    boolean isFinished = this.isFinished();
    int evaluation = 0;

    if (isFinished && getGameResult() == color) {
      evaluation += AI.MAXVALUE;
    }
    if (isFinished && getGameResult() == -color) {
      evaluation += AI.MINVALUE;
    }

    // Maximiser's Evaluation
    for (Square pawn : Player.getAllPawns(board, color)) {
      evaluation += 10;
    }

    // Minimiser's Evaluation
    for (Square pawn : Player.getAllPawns(board, -color)) {
      evaluation -= 10;
    }

    return evaluation;
  }

  public int staticEvaluation(int color) {
    boolean isFinished = this.isFinished();
    int[] filesMax = new int[8], filesMin = new int[8];
    int evaluation = 0;
    int stepsToDest;

    if (isFinished && getGameResult() == color) {
      evaluation = AI.MAXVALUE;
    }
    if (isFinished && getGameResult() == -color) {
      evaluation = AI.MINVALUE;
    }
    if (isFinished && getGameResult() == Color.NONE) {
      return 0;
    }

    // Maximiser's Evaluation
    for (Square pawn : Player.getAllPawns(board, color)) {
      // Default
      evaluation += 1000;
      // Record the file of this pawn. Useful in later evaluation
      filesMax[pawn.getX()]++;
      // Check if it's a passed pawn
      if (isPassedPawn(pawn, color)) {
        if (color == Color.BLACK) {
          stepsToDest = pawn.getY();
        } else {
          stepsToDest = 7 - pawn.getY();
        }
        evaluation += AI.DECISIVE * (9 - stepsToDest);
      }
    }

    // Check for isolated pawns
    if (filesMax[0] > 0 && filesMax[1] == 0) {
      evaluation -= 100;
    }
    if (filesMax[7] > 0 && filesMax[6] == 0) {
      evaluation -= 100;
    }
    for (int i = 1; i < 7; i++) {
      if (filesMax[i - 1] == 0 && filesMax[i + 1] == 0 && filesMax[i] > 0) {
        evaluation -= 100;
      }
    }

    // Minimiser's Evaluation
    for (Square pawn : Player.getAllPawns(board, -color)) {
      // Default
      evaluation -= 1000;
      // Record the file of this pawn. Useful in later evaluation
      filesMin[pawn.getX()]++;
      // Check if it's a passed pawn
      if (isPassedPawn(pawn, -color)) {
        if (-color == Color.BLACK) {
          stepsToDest = pawn.getY();
        } else {
          stepsToDest = 7 - pawn.getY();
        }
        evaluation -= AI.DECISIVE * (9 - stepsToDest);
      }
    }

    // Check for isolated pawns
    if (filesMin[0] > 0 && filesMin[1] == 0) {
      evaluation += 100;
    }
    if (filesMin[7] > 0 && filesMin[6] == 0) {
      evaluation += 100;
    }
    for (int i = 1; i < 7; i++) {
      if (filesMin[i - 1] == 0 && filesMin[i + 1] == 0 && filesMin[i] > 0) {
        evaluation += 100;
      }
    }

    return evaluation;
  }

  public boolean isPassedPawn(Square square, int color) {
    if (square.occupiedBy() != color) {
      return false;
    }

    int x = square.getX();
    int y = square.getY();

    for (int i = x - 1; i <= x + 1; i++) {
      Square left = getBoard().getSquare(x - 1, y);
      Square right = getBoard().getSquare(x + 1, y);
      if (((color == Color.BLACK && y == 4) || (color == Color.WHITE && y == 3)) && (
          (left != null && left.occupiedBy() == -color) || (right != null
              && right.occupiedBy() == -color))) {
        return false;
      }
      for (int j = y + color; !Color.hasReachedLastRank(color, j); j += color) {
        if (getBoard().getSquare(i, j) != null
            && (getBoard().getSquare(i, j).occupiedBy() == -color)) {
          return false;
        }
      }
    }

    return true;
  }
}

import java.util.List;
import java.util.Random;

public class AI {

  public static int DECISIVE = 1000000;
  public static int MINVALUE = -10000000;
  public static int MAXVALUE = 10000000;

  // An AI that makes random decisions
  public static Move randomAI(Player player) {
    List<Move> moves = Player
        .getAllValidMoves(player.getBoard(), player.getColor(), player.getGame());
    Random rand = new Random();
    int n = rand.nextInt(moves.size() - 1);
    return moves.get(n);
  }

  public static Move aiMove(Player player, int depth) {
    int evaluation, choice = MINVALUE * 2;
    Random rand = new Random();
    List<Move> moves = Player
        .getAllValidMoves(player.getBoard(), player.getColor(), player.getGame());
    Move nextMove = moves.get(0);

    for (Move move : moves) {
      player.getGame().applyMove(move);
      evaluation = alphaBeta(depth, player, MINVALUE, MAXVALUE);
      player.getGame().unapplyMove();

      System.out.print(move.getSAN() + ": ");
      System.out.println(evaluation);

      // some randomness
      if (evaluation > choice || ((evaluation == choice) && (rand.nextInt(4) == 0)))  {
        nextMove = move;
        choice = evaluation;
      }
    }

    return nextMove;
  }

  public static int alphaBeta(int depth, Player player, int alpha, int beta) {
    int evaluation;
    Game game = player.getGame();
    int color = game.getCurrentPlayer();
    boolean isMax = color == player.getColor();

    List<Move> moves = Player.getAllValidMoves(game.getBoard(), color, game);
    if (game.isFinished() || moves.size() == 0 || depth == 0) {
      return game.staticEvaluation(player.getColor());
    }

    for (Move move : moves) {
      game.applyMove(move);
      evaluation = alphaBeta(depth - 1, player, alpha, beta);
      game.unapplyMove();

      // Maximiser
      if (isMax) {
        if (evaluation > alpha) {
          alpha = evaluation;
        }
        if (alpha >= beta) {
          break; // beta cut-off
        }
      }

      // Minimiser
      if (!isMax) {
        if (evaluation < beta) {
          beta = evaluation;
        }
        if (alpha >= beta) {
          break; // alpha cut-off
        }
      }
    }

    if (isMax) {
      return alpha;
    } else {
      return beta;
    }
  }
}

public class Color {

  // It is not good to store these colors aa integers in terms of readability, but it makes
  // other methods easier to code.
  public static int WHITE = 1;
  public static int NONE = 0;
  public static int BLACK = -1;
  public static int WHITETARGET = 7;
  public static int BLACKTARGET = 0;

  public static char charOf(int color) {
    switch (color) {
      case 1:
        return 'W';
      case -1:
        return 'B';
    }
    return '.';
  }

  public static String strOf(int color) {
    switch (color) {
      case 1:
        return "WHITE";
      case -1:
        return "BLACK";
    }
    return "NONE";
  }

  public static boolean hasReachedLastRank(int color, int currentRank) {
    if (color == WHITE) {
      return currentRank >= WHITETARGET;
    }

    return currentRank <= BLACKTARGET;
  }
}
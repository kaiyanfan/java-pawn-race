public class Move {

  private Square from, to;
  private boolean isCapture, isEnPassantCapture;

  public Move(Square from, Square to, boolean isCapture, boolean isEnPassantCapture) {
    this.from = from;
    this.to = to;
    this.isCapture = isCapture;
    this.isEnPassantCapture = isEnPassantCapture;
  }

  public Square getFrom() {
    return from;
  }

  public Square getTo() {
    return to;
  }

  public boolean isCapture() {
    return isCapture;
  }

  public boolean isEnPassantCapture() {
    return isEnPassantCapture;
  }

  public String getSAN() {
    char x = (char) (to.getX() + 'a');
    char y = (char) (to.getY() + '1');
    if (isCapture) {
      char file = (char) (from.getX() + 'a');
      return (file + "x" + x + y);
    } else {
      return (Character.toString(x) + y);
    }
  }
}

public class Square {

  private int x, y;
  private int color;

  public Square(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int occupiedBy() {
    return color;
  }

  public void setOccupier(int color) {
    this.color = color;
  }
}

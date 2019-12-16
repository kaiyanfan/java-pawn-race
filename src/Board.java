public class Board {

  private Square[][] board;

  public Board(char whiteGap, char blackGap) {
    int wFile = whiteGap - 'A';
    int bFile = blackGap - 'A';
    board = new Square[8][8];

    for (int i = 0; i < 8; i++) {
      for (int j = 0; j < 8; j++) {
        Square sqr = new Square(i, j);
        if (j == 1 && i != wFile) {
          sqr.setOccupier(Color.WHITE);
        } else if (j == 6 && i != bFile) {
          sqr.setOccupier(Color.BLACK);
        } else {
          sqr.setOccupier(Color.NONE);
        }
        board[i][j] = sqr;
      }
    }
  }

  public Square getSquare(int x, int y) {
    if (x < 0 || y < 0 || x > 7 || y > 7) {
      return null;
    }

    Square sqr = new Square(x, y);
    sqr.setOccupier(board[x][y].occupiedBy());
    return sqr;
  }

  public void applyMove(Move move) {
    board[move.getTo().getX()][move.getTo().getY()].setOccupier(move.getFrom().occupiedBy());
    board[move.getFrom().getX()][move.getFrom().getY()].setOccupier(Color.NONE);
    if (move.isEnPassantCapture()) {
      board[move.getTo().getX()][move.getFrom().getY()].setOccupier(Color.NONE);
    }
  }

  public void unapplyMove(Move move) {
    int x = move.getFrom().getX();
    int y = move.getFrom().getY();
    int x1 = move.getTo().getX();
    int y1 = move.getTo().getY();

    int mover = board[x1][y1].occupiedBy();
    int captured = -mover;

    board[x][y].setOccupier(mover);
    if (move.isEnPassantCapture()) {
      board[x1][y1].setOccupier(Color.NONE);
      board[x1][y].setOccupier(captured);
    } else if (move.isCapture()) {
      board[x1][y1].setOccupier(captured);
    } else {
      board[x1][y1].setOccupier(Color.NONE);
    }
  }

  public void display() {
    System.out.println("   A B C D E F G H");
    for (int j = 7; j >= 0; j--) {
      System.out.print((j + 1) + "  ");
      for (int i = 0; i < 8; i++) {
        System.out.print(Color.charOf(board[i][j].occupiedBy()) + " ");
      }
      System.out.println("  " + (j + 1));
    }
    System.out.println("   A B C D E F G H");
  }
}

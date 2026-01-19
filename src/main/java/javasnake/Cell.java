package javasnake;

public class Cell {
  private final Position position;
  private CellType type;

  public enum CellType {
    EMPTY,
    SNAKE,
    FOOD
  }

  public Cell(int row, int col, CellType type) {
    this.position = new Position(row, col);
    this.type = type;
  }

  public static Cell getRandomFoodCell(int maxRows, int maxCols) {
    return new Cell((int) (Math.random() * maxRows), (int) (Math.random() * maxCols), CellType.FOOD);
  }

  public int getRow() {
    return position.row();
  }

  public int getCol() {
    return position.col();
  }

  public CellType getType() {
    return type;
  }

  public void setType(CellType type) {
    this.type = type;
  }

  public record Position(int row, int col) {
  }
}

package javasnake;

public class Cell {
  private int row;
  private int col;
  private CellType type;

  public enum CellType {
    EMPTY,
    SNAKE,
    FOOD
  }

  public Cell(int row, int col, CellType type) {
    this.row = row;
    this.col = col;
    this.type = type;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public CellType getType() {
    return type;
  }

  public void setType(CellType type) {
    this.type = type;
  }
}

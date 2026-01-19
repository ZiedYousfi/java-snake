package javasnake;

public class GameGrid {
  private final int rows;
  private final int cols;
  private final int cellSize;
  private Cell[][] grid;

  public GameGrid(int rows, int cols, int cellSize) {
    this.rows = rows;
    this.cols = cols;
    this.cellSize = cellSize;

    grid = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        grid[r][c] = new Cell(r, c, Cell.CellType.EMPTY);
      }
    }
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public int getCellSize() {
    return cellSize;
  }

  public Cell getCell(int row, int col) {
    if (row >= 0 && row < rows && col >= 0 && col < cols) {
      return grid[row][col];
    }
    throw new IndexOutOfBoundsException("Cell position out of bounds: (" + row + ", " + col + ")");
  }
}
package javasnake;

public class Player {
  // Head position is always the first element in the array then follows the body positions (from neck to tail)
  private Cell.Position[] snakeBodyPositions;

  public Player(Cell.Position initialPosition) {
    this.snakeBodyPositions = new Cell.Position[] { initialPosition };
  }

  public Cell.Position[] getSnakeBodyPositions() {
    return snakeBodyPositions;
  }

  public void changeHeadPosition(Cell.Position newHeadPosition) {
    Cell.Position[] newPositions = new Cell.Position[snakeBodyPositions.length];
    newPositions[0] = newHeadPosition;
    System.arraycopy(snakeBodyPositions, 0, newPositions, 1, snakeBodyPositions.length - 1);
    this.snakeBodyPositions = newPositions;
  }

  public void growSnake(Cell.Position newHeadPosition) {
    Cell.Position[] newPositions = new Cell.Position[snakeBodyPositions.length + 1];
    newPositions[0] = newHeadPosition;
    System.arraycopy(snakeBodyPositions, 0, newPositions, 1, snakeBodyPositions.length);
    this.snakeBodyPositions = newPositions;
  }

}

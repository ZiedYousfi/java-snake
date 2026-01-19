package javasnake;

import java.util.LinkedList;
import java.util.List;

public class Player {
  // Head position is always the first element in the list, then follows the body
  // positions (from neck to tail)
  private List<Cell.Position> snakeBodyPositions;
  private Direction currentDirection;
  private int rowLimit;
  private int colLimit;

  public Player(Cell.Position initialPosition, Direction initialDirection, int rowLimit, int colLimit) {
    this.snakeBodyPositions = new LinkedList<>();
    this.snakeBodyPositions.add(initialPosition);
    this.currentDirection = initialDirection;
    this.rowLimit = rowLimit;
    this.colLimit = colLimit;
  }

  public List<Cell.Position> getSnakeBodyPositions() {
    return snakeBodyPositions;
  }

  public void setDirection(Direction direction) {
    this.currentDirection = direction;
  }

  public Direction getDirection() {
    return currentDirection;
  }

  public boolean grow() {
    Cell.Position currentHead = snakeBodyPositions.get(0);
    if (currentHead.row() + currentDirection.deltaRow < 0 || currentHead.row() + currentDirection.deltaRow >= rowLimit
        ||
        currentHead.col() + currentDirection.deltaCol < 0
        || currentHead.col() + currentDirection.deltaCol >= colLimit) {
      return false; // Out of bounds
    }
    Cell.Position newHead = new Cell.Position(
        currentHead.row() + currentDirection.deltaRow,
        currentHead.col() + currentDirection.deltaCol);
    snakeBodyPositions.add(0, newHead); // Add new head
    return true;
  }

  public boolean stepUpdate() {
    Cell.Position currentHead = snakeBodyPositions.get(0);
    if (currentHead.row() + currentDirection.deltaRow < 0 || currentHead.row() + currentDirection.deltaRow >= rowLimit
        ||
        currentHead.col() + currentDirection.deltaCol < 0
        || currentHead.col() + currentDirection.deltaCol >= colLimit) {
      return false; // Out of bounds
    }
    Cell.Position newHead = new Cell.Position(
        currentHead.row() + currentDirection.deltaRow,
        currentHead.col() + currentDirection.deltaCol);
    snakeBodyPositions.add(0, newHead); // Add new head
    snakeBodyPositions.remove(snakeBodyPositions.size() - 1); // Remove tail
    return true;
  }

  public record Direction(int deltaRow, int deltaCol) {
  }
}

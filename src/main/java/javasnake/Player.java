package javasnake;

import java.util.LinkedList;
import java.util.List;

public class Player {
  // Head position is always the first element in the list, then follows the body
  // positions (from neck to tail)
  private List<Cell.Position> snakeBodyPositions;
  private Direction currentDirection;

  public Player(Cell.Position initialPosition) {
    this.snakeBodyPositions = new LinkedList<>();
    this.snakeBodyPositions.add(initialPosition);
    this.currentDirection = new Direction(1, 0);
  }

  public List<Cell.Position> getSnakeBodyPositions() {
    return snakeBodyPositions;
  }

  public void grow() {
    Cell.Position currentHead = snakeBodyPositions.get(0);
    Cell.Position newHead = new Cell.Position(
        currentHead.row() + currentDirection.deltaX,
        currentHead.col() + currentDirection.deltaY);
    snakeBodyPositions.add(0, newHead); // Add new head
  }

  public void stepUpdate() {
    Cell.Position currentHead = snakeBodyPositions.get(0);
    Cell.Position newHead = new Cell.Position(
        currentHead.row() + currentDirection.deltaX,
        currentHead.col() + currentDirection.deltaY);
    snakeBodyPositions.add(0, newHead); // Add new head
    snakeBodyPositions.remove(snakeBodyPositions.size() - 1); // Remove tail
  }

  public record Direction(int deltaX, int deltaY) {
  }
}

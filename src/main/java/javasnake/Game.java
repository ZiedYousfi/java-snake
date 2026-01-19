package javasnake;

import io.github.libsdl4j.api.event.SDL_Event;

import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYDOWN;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_QUIT;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_DOWN;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_UP;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_LEFT;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_RIGHT;

public class Game {
  private SdlRenderer renderer;
  private int frameCount;
  private int score;
  private boolean isRunning = true;
  private GameGrid grid;
  private Player player;
  private final int TARGET_FPS = 60;
  private double timeSinceLastFrame = 0.0;
  // Time per frame in seconds
  private final double TIME_PER_FRAME = 10.0 / TARGET_FPS;

  public Game() {
    this.renderer = SdlRenderer.getInstance();

    renderer.setColor(Color.WHITE);
    renderer.fillRect(new Rect(0, 0, 640, 640));
    renderer.clear();
    renderer.present();

    this.frameCount = 0;
    this.score = 0;
    this.grid = new GameGrid(100, 100, 32);
    this.player = new Player(new Cell.Position(10, 10), new Player.Direction(1, 0), grid.getRows(), grid.getCols());
    this.isRunning = true;

    this.render();
  }

  public void start() {
    long lastTime = System.nanoTime();
    double nsPerFrame = 1_000_000_000.0 / TARGET_FPS;
    double delta = 0;

    while (isRunning) {
      long now = System.nanoTime();
      delta += (now - lastTime) / nsPerFrame;
      lastTime = now;

      renderer.handleEvents(this::callbackSdlEvent);
      timeSinceLastFrame += delta * (1.0 / TARGET_FPS);
      delta = 0;
      if (timeSinceLastFrame >= TIME_PER_FRAME) {
        this.render();
        timeSinceLastFrame = 0;
      }
    }
    stop();
  }

  public int getFrameCount() {
    return frameCount;
  }

  public int getScore() {
    return score;
  }

  public GameGrid getGrid() {
    return grid;
  }

  public void stop() {
    isRunning = false;
    renderer.quit();
  }

  public void render() {
    renderer.clear();

    var food = Cell.getRandomFoodCell(grid.getRows(), grid.getCols());

    grid.getCell(food.getRow(), food.getCol()).setType(Cell.CellType.FOOD);

    if (player.stepUpdate() == false) {
      System.out.println("Game Over! Final Score: " + score);
      stop();
      return;
    }

    if (grid.getCell(player.getSnakeBodyPositions().get(0).row(),
        player.getSnakeBodyPositions().get(0).col()).getType() == Cell.CellType.FOOD) {
      score += 10;
      player.grow();
    }

    // Clear old snake positions from grid
    for (int r = 0; r < grid.getRows(); r++) {
      for (int c = 0; c < grid.getCols(); c++) {
        if (grid.getCell(r, c).getType() == Cell.CellType.SNAKE) {
          grid.getCell(r, c).setType(Cell.CellType.EMPTY);
        }
      }
    }

    // Set player cells on the grid
    for (Cell.Position pos : player.getSnakeBodyPositions()) {
      try {
        grid.getCell(pos.row(), pos.col()).setType(Cell.CellType.SNAKE);
      } catch (Exception e) {
        System.err.println(e);
      }
    }

    for (int r = 0; r < grid.getRows(); r++) {
      for (int c = 0; c < grid.getCols(); c++) {
        Cell cell = grid.getCell(r, c);
        switch (cell.getType()) {
          case EMPTY -> renderer.setColor(Color.BLACK);
          case SNAKE -> renderer.setColor(Color.GREEN);
          case FOOD -> renderer.setColor(Color.RED);
        }
        Rect cellRect = new Rect(c * grid.getCellSize(), r * grid.getCellSize(), grid.getCellSize(),
            grid.getCellSize());
        renderer.fillRect(cellRect);
      }
    }
    renderer.present();
  }

  public void callbackSdlEvent(SDL_Event sdlEvent) {
    switch (sdlEvent.type) {
      case SDL_QUIT -> stop();
      case SDL_KEYDOWN -> {
        int keycode = sdlEvent.key.keysym.sym;
        if (keycode == SDLK_UP) {
          player.setDirection(new Player.Direction(-1, 0)); // W or Up Arrow
        } else if (keycode == SDLK_DOWN) {
          player.setDirection(new Player.Direction(1, 0)); // S or Down Arrow
        } else if (keycode == SDLK_LEFT) {
          player.setDirection(new Player.Direction(0, -1)); // A or Left Arrow
        } else if (keycode == SDLK_RIGHT) {
          player.setDirection(new Player.Direction(0, 1)); // D or Right Arrow
        } else {
          // Do nothing or handle other keys if needed
        }
        render();
      }
      default -> {
        System.out.println("Unhandled SDL Event: " + sdlEvent.type);
      }
    }
  }
}

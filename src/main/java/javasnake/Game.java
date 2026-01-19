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
  private double timeSinceLastFoodSpawn = 0.0;
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

    while (isRunning) {
      long now = System.nanoTime();
      double elapsed = (now - lastTime) / 1_000_000_000.0;
      lastTime = now;

      renderer.handleEvents(this::callbackSdlEvent);

      timeSinceLastFrame += elapsed;
      timeSinceLastFoodSpawn += elapsed;
      if (timeSinceLastFrame >= TIME_PER_FRAME) {
        update();
        timeSinceLastFrame = 0;
      }

      this.render();

      try {
        Thread.sleep(1000 / TARGET_FPS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    stop();
  }

  public void update() {
    frameCount++;

    if (timeSinceLastFoodSpawn >= 0.833) {
      var food = Cell.getRandomFoodCell(grid.getRows(), grid.getCols());
      grid.getCell(food.getRow(), food.getCol()).setType(Cell.CellType.FOOD);
      timeSinceLastFoodSpawn = 0;
    }

    if (player.stepUpdate() == false) {
      System.out.println("Game Over! Final Score: " + score);
      stop();
      return;
    }

    Cell.Position headPos = player.getSnakeBodyPositions().get(0);
    if (grid.getCell(headPos.row(), headPos.col()).getType() == Cell.CellType.FOOD) {
      score += 10;
      player.grow();
      // Ensure the food is "consumed" on the grid
      grid.getCell(headPos.row(), headPos.col()).setType(Cell.CellType.EMPTY);
    }

    syncGrid();
  }

  private void syncGrid() {
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
        boolean directionChanged = false;
        var targetDirection = player.getDirection();
        if (keycode == SDLK_UP) {
          targetDirection = new Player.Direction(-1, 0); // W or Up Arrow
          directionChanged = true;
        } else if (keycode == SDLK_DOWN) {
          targetDirection = new Player.Direction(1, 0); // S or Down Arrow
          directionChanged = true;
        } else if (keycode == SDLK_LEFT) {
          targetDirection = new Player.Direction(0, -1); // A or Left Arrow
          directionChanged = true;
        } else if (keycode == SDLK_RIGHT) {
          targetDirection = new Player.Direction(0, 1); // D or Right Arrow
          directionChanged = true;
        }

        if (directionChanged) {
          player.setDirection(targetDirection); // W or Up Arrow
          update();
          timeSinceLastFrame = 0;
        }
      }
    }
  }
}

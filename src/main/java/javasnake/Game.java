package javasnake;

import io.github.libsdl4j.api.event.SDL_Event;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_QUIT;

public class Game {
  private SdlRenderer renderer;
  private int frameCount;
  private int score;
  private boolean isRunning = true;
  private GameGrid grid;

  public Game() {
    this.renderer = SdlRenderer.getInstance();

    renderer.setColor(Color.WHITE);
    renderer.fillRect(new Rect(0, 0, 640, 640));
    renderer.clear();
    renderer.present();

    this.frameCount = 0;
    this.score = 0;
    this.grid = new GameGrid(20, 20, 32);
    this.isRunning = true;

    this.render();
  }

  public void start() {
    long lastTime = System.nanoTime();
    double targetFps = 60.0;
    double nsPerFrame = 1_000_000_000.0 / targetFps;
    double delta = 0;

    while (isRunning) {
      long now = System.nanoTime();
      delta += (now - lastTime) / nsPerFrame;
      lastTime = now;

      renderer.handleEvents(this::callbackSdlEvent);

      while (delta >= 1) {
        frameCount++;
        delta--;
      }

      this.render();

      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
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
    for (int r = 0; r < grid.getRows(); r++) {
      for (int c = 0; c < grid.getCols(); c++) {
        Cell cell = grid.getCell(r, c);
        switch (cell.getType()) {
          case EMPTY -> renderer.setColor(Color.BLACK);
          case SNAKE -> renderer.setColor(Color.GREEN);
          case FOOD -> renderer.setColor(Color.RED);
        }
        Rect cellRect = new Rect(c * grid.getCellSize(), r * grid.getCellSize(), grid.getCellSize(), grid.getCellSize());
        renderer.fillRect(cellRect);
      }
    }
    renderer.present();
  }

  public void callbackSdlEvent(SDL_Event sdlEvent) {
    if (sdlEvent.type == SDL_QUIT) {
      stop();
    } else {
      System.out.println("Unhandled SDL Event: " + sdlEvent.type);
    }
  }
}

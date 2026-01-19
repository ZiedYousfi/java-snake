package javasnake;

public class Game {
  private SdlRenderer renderer;
  private int frameCount;
  private int score;
  private boolean isRunning = true;
  private GameGrid grid;

  public Game() {
    this.renderer = SdlRenderer.getInstance();
    this.frameCount = 0;
    this.score = 0;
    this.grid = new GameGrid(20, 20, 32);
  }

  public void start() {
    while (isRunning) {
      frameCount++;
      // Game loop and logic would go here
    }
  }

  public int getFrameCount() {
    return frameCount;
  }

  public int getScore() {
    return score;
  }
}

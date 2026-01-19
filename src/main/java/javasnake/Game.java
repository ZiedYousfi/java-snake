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
    this.isRunning = true;

    this.render();
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

  public GameGrid getGrid() {
    return grid;
  }

  public void stop() {
    isRunning = false;
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
}

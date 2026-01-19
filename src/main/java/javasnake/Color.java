package javasnake;

public record Color(int r, int g, int b, int a) {
  public static final Color GREEN = new Color(0, 255, 0, 255);
  public static final Color BLUE = new Color(0, 0, 255, 255);
  public static final Color YELLOW = new Color(255, 255, 0, 255);
  public static final Color RED = new Color(255, 0, 0, 255);
  public static final Color CYAN = new Color(0, 255, 255, 255);
  public static final Color MAGENTA = new Color(255, 0, 255, 255);
  public static final Color BLACK = new Color(0, 0, 0, 255);
  public static final Color WHITE = new Color(255, 255, 255, 255);
}

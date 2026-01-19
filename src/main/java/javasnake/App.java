package javasnake;

public class App {

    public static void main(String[] args) {
        runApp();
    }

    private static void runApp() {
        var game = new Game();

        game.start();
    }
}
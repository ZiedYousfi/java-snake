package javasnake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

    public static void main(String[] args) {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            if (!"true".equals(System.getProperty("restarted"))) {
                try {
                    restartOnFirstThread(args);
                    return;
                } catch (Exception e) {
                    System.err.println("Failed to restart on first thread: " + e.getMessage());
                }
            }
        }
        runApp();
    }

    private static void restartOnFirstThread(String[] args) throws IOException, InterruptedException {
        String javaBin = System.getProperty("java.home") + "/bin/java";
        String classpath = System.getProperty("java.class.path");
        String mainClass = App.class.getName();

        List<String> command = new ArrayList<>();
        command.add(javaBin);
        command.add("-XstartOnFirstThread");
        command.add("-Drestarted=true");
        command.add("-cp");
        command.add(classpath);
        command.add(mainClass);
        for (String arg : args) {
            command.add(arg);
        }

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.inheritIO();
        Process process = pb.start();
        System.exit(process.waitFor());
    }

    private static void runApp() {
        var game = new Game();

        game.start();
    }
}
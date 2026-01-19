package javasnake;

import io.github.libsdl4j.api.event.SDL_Event;
import static io.github.libsdl4j.api.event.SDL_EventType.*;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_SPACE;

public class App {

    public static void main(String[] args) {
        runApp();
    }

    private static void runApp() {
        SdlRenderer renderer = SdlRenderer.getInstance();

        // Set color of renderer to green
        renderer.setColor(Color.GREEN);
        renderer.clear();
        renderer.present();

        Rect rect1 = new Rect(100, 100, 200, 150);

        // Start an event loop and react to events
        SDL_Event evt = new SDL_Event();
        boolean shouldRun = true;
        while (shouldRun) {
            while (SDL_PollEvent(evt) != 0) {
                switch (evt.type) {
                    case SDL_QUIT:
                        shouldRun = false;
                        break;
                    case SDL_KEYDOWN:
                        if (evt.key.keysym.sym == SDLK_SPACE) {
                            System.out.println("SPACE pressed");
                        }
                        break;
                    case SDL_WINDOWEVENT:
                        System.out.println("Window event " + evt.window.event);
                        break;
                    default:
                        break;
                }
            }

            renderer.clear();

            // Draw color : blue
            renderer.setColor(Color.BLUE);
            renderer.drawLine(new Point(0, 0), new Point(1024, 768));

            // Draw color : green
            renderer.setColor(Color.GREEN);
            renderer.drawLine(new Point(1024, 0), new Point(0, 768));

            // Draw color : yellow
            renderer.setColor(Color.YELLOW);
            renderer.drawRect(rect1);

            // Draw color : cyan
            renderer.setColor(Color.CYAN);
            renderer.fillRect(rect1);

            // Draw color : magenta
            renderer.setColor(Color.MAGENTA);

            rect1 = new Rect(rect1.x() + 1, rect1.y() + 1, rect1.w(), rect1.h());

            renderer.present();
        }

        renderer.quit();
    }
}
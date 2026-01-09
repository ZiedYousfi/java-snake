package javasnake;

import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.video.SDL_Window;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.SdlSubSystemConst.SDL_INIT_EVERYTHING;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.event.SDL_EventType.*;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_SPACE;
import static io.github.libsdl4j.api.render.SDL_RendererFlags.SDL_RENDERER_ACCELERATED;
import static io.github.libsdl4j.api.render.SdlRender.*;
import static io.github.libsdl4j.api.video.SDL_WindowFlags.SDL_WINDOW_RESIZABLE;
import static io.github.libsdl4j.api.video.SDL_WindowFlags.SDL_WINDOW_SHOWN;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_CreateWindow;
import static io.github.libsdl4j.api.video.SdlVideoConst.SDL_WINDOWPOS_CENTERED;

public class App {

    static {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            String libraryPath = System.getProperty("jna.library.path", "");
            if (!libraryPath.contains("/opt/homebrew/lib")) {
                if (!libraryPath.isEmpty()) {
                    libraryPath += ":";
                }
                libraryPath += "/opt/homebrew/lib";
                System.setProperty("jna.library.path", libraryPath);
            }
        }
    }

    public static void main(String[] args) {
        runApp();
    }

    private static void runApp() {
        // Initialize SDL
        int result = SDL_Init(SDL_INIT_EVERYTHING);
        if (result != 0) {
            throw new IllegalStateException(
                    "Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
        }

        // Create and init the window
        SDL_Window window = SDL_CreateWindow("Demo SDL2", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 1024, 768,
                SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE);
        if (window == null) {
            throw new IllegalStateException("Unable to create SDL window: " + SDL_GetError());
        }

        // Create and init the renderer
        SDL_Renderer renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
        if (renderer == null) {
            throw new IllegalStateException("Unable to create SDL renderer: " + SDL_GetError());
        }

        // Set color of renderer to green
        SDL_SetRenderDrawColor(renderer, (byte) 0, (byte) 255, (byte) 0, (byte) 255);

        // Clear the window and make it all green
        SDL_RenderClear(renderer);

        // Render the changes above
        SDL_RenderPresent(renderer);

        var rect1 = new io.github.libsdl4j.api.rect.SDL_Rect();
        rect1.x = 100;
        rect1.y = 100;
        rect1.w = 200;
        rect1.h = 150;

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

            SDL_RenderClear(renderer);
            
            SDL_SetRenderDrawColor(renderer, (byte) 0, (byte) 0, (byte) 255, (byte) 255);
            SDL_RenderDrawLine(renderer, 0, 0, 1024, 768);
            SDL_SetRenderDrawColor(renderer, (byte) 0, (byte) 255, (byte) 0, (byte) 255);
            SDL_RenderDrawLine(renderer, 1024, 0, 0, 768);
            SDL_SetRenderDrawColor(renderer, (byte) 255, (byte) 255, (byte) 0, (byte) 255);
            SDL_RenderDrawRect(renderer, rect1);
            SDL_SetRenderDrawColor(renderer, (byte) 0, (byte) 255, (byte) 255, (byte) 255);
            SDL_RenderFillRect(renderer, rect1);
            SDL_SetRenderDrawColor(renderer, (byte) 255, (byte) 0, (byte) 255, (byte) 255);

            rect1.x += 1;
            rect1.y += 1;
            SDL_RenderDrawRect(renderer, rect1);
            SDL_SetRenderDrawColor(renderer, (byte) 255, (byte) 0, (byte) 0, (byte) 255);
            SDL_RenderFillRect(renderer, rect1);

            SDL_RenderPresent(renderer);
        }

        SDL_Quit();
    }
}
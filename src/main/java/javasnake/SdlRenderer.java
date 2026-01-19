package javasnake;

import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.video.SDL_Window;
import io.github.libsdl4j.api.rect.SDL_Rect;
import io.github.libsdl4j.api.event.SDL_Event;

import java.util.function.Consumer;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.SdlSubSystemConst.SDL_INIT_EVERYTHING;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.render.SDL_RendererFlags.SDL_RENDERER_ACCELERATED;
import static io.github.libsdl4j.api.render.SdlRender.*;
import static io.github.libsdl4j.api.video.SDL_WindowFlags.SDL_WINDOW_RESIZABLE;
import static io.github.libsdl4j.api.video.SDL_WindowFlags.SDL_WINDOW_SHOWN;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_CreateWindow;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_DestroyWindow;
import static io.github.libsdl4j.api.video.SdlVideoConst.SDL_WINDOWPOS_CENTERED;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;

public class SdlRenderer {
  private static SdlRenderer instance;
  private final SDL_Window window;
  private final SDL_Renderer renderer;

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

  private SdlRenderer() {
    // Initialize SDL
    int result = SDL_Init(SDL_INIT_EVERYTHING);
    if (result != 0) {
      throw new IllegalStateException(
          "Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
    }

    // Create and init the window
    window = SDL_CreateWindow("Demo SDL2", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, 1024, 768,
        SDL_WINDOW_SHOWN | SDL_WINDOW_RESIZABLE);
    if (window == null) {
      throw new IllegalStateException("Unable to create SDL window: " + SDL_GetError());
    }

    // Create and init the renderer
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
    if (renderer == null) {
      throw new IllegalStateException("Unable to create SDL renderer: " + SDL_GetError());
    }
  }

  public static synchronized SdlRenderer getInstance() {
    if (instance == null) {
      instance = new SdlRenderer();
    }
    return instance;
  }

  public void setColor(Color color) {
    SDL_SetRenderDrawColor(renderer, (byte) color.r(), (byte) color.g(), (byte) color.b(), (byte) color.a());
  }

  public void handleEvents(Consumer<SDL_Event> callback) {
    SDL_Event event = new SDL_Event();
    while (SDL_PollEvent(event) != 0) {
      callback.accept(event);
    }
  }

  public void clear() {
    SDL_RenderClear(renderer);
  }

  public void present() {
    SDL_RenderPresent(renderer);
  }

  public void drawLine(Point p1, Point p2) {
    SDL_RenderDrawLine(renderer, p1.x(), p1.y(), p2.x(), p2.y());
  }

  public void drawRect(Rect rect) {
    SDL_Rect sdlRect = new SDL_Rect();
    sdlRect.x = rect.x();
    sdlRect.y = rect.y();
    sdlRect.w = rect.w();
    sdlRect.h = rect.h();
    SDL_RenderDrawRect(renderer, sdlRect);
  }

  public void fillRect(Rect rect) {
    SDL_Rect sdlRect = new SDL_Rect();
    sdlRect.x = rect.x();
    sdlRect.y = rect.y();
    sdlRect.w = rect.w();
    sdlRect.h = rect.h();
    SDL_RenderFillRect(renderer, sdlRect);
  }

  public void quit() {
    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    SDL_Quit();
  }
}

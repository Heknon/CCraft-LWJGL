import engine.GameEngine;
import engine.Window;
import game.CCraft;

public class Main {
    private static final String TITLE = "CCraft";
    private static final boolean VSYNC = true;

    public static void main(String[] args) {
        try {
            GameEngine gameEngine = new GameEngine(TITLE, new Window.Size(), new CCraft(), VSYNC);
            gameEngine.start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}


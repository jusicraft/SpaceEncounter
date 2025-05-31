package Game;

import State.GameState;

public class GameContext {
    private static GameContext instance;
    private GameState gameState;

    private GameContext() {}

    public static GameContext getInstance() {
        if (instance == null) instance = new GameContext();
        return instance;
    }

    public void setState(GameState state) {
        this.gameState = state;
    }

    public GameState getState() {
        return gameState;
    }
}

package State;

import Game.GameContext;
import Game.GameModel;

import java.awt.*;
import java.awt.event.KeyEvent;

class PausedState implements GameState {
    private final GameModel model;

    public PausedState(GameModel model) {
        this.model = model;
    }

    @Override
    public void update(double delta) {
        //blup blup
    }

    @Override
    public void render(Graphics g) {
        //pause overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, 1000, 500);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("PAUSED", 440, 230);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString("Press P to Resume", 435, 270);
    }

    @Override
    public void handleInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            GameContext.getInstance().setState(new PlayingState(model));
        }
    }
}

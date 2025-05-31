package State;

import java.awt.*;
import java.awt.event.KeyEvent;

public interface GameState {
    void update(double delta);
    void render(Graphics g);
    void handleInput(KeyEvent e);
}
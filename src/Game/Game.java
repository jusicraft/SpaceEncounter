package Game;

import State.GameState;
import State.PlayingState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Game extends JPanel implements Runnable, KeyListener {

    private GameState gameState;
    private final GameModel model;

    public Game() {
        model = GameModel.getInstance();
        GameContext.getInstance().setState(new PlayingState(model));

        setPreferredSize(new Dimension(1000, 500));
        setFocusable(true);
        setBackground(Color.BLACK);
        addKeyListener(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        while (true) {
            long now = System.nanoTime();
            double delta = (now - lastTime) / 1e9;
            lastTime = now;

            GameContext.getInstance().getState().update(delta);
            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        GameContext.getInstance().getState().render(g);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_A || code == KeyEvent.VK_S || code == KeyEvent.VK_D) {
            model.setKeyState(code, true);
        }

        if (code == KeyEvent.VK_SHIFT) {
            model.isBoosting = true;
        }

        GameState currentState = GameContext.getInstance().getState();
        if (currentState != null) {
            currentState.handleInput(e);
        }
    }



    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W || code == KeyEvent.VK_A || code == KeyEvent.VK_S || code == KeyEvent.VK_D) {
            model.setKeyState(code, false);
        }

        if (code == KeyEvent.VK_SHIFT) {
            model.isBoosting = false;
        }
    }



    @Override
    public void keyTyped(KeyEvent e) {}
}

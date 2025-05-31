import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Movement extends JPanel implements Runnable, KeyListener {

    private int x = 100, y = 100;
    private double speed = 300;
    private boolean up, down, left, right;

    public Movement() {
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

            update(delta);
            repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(double delta) {
        if (up) y -= speed * delta;
        if (down) y += speed * delta;
        if (left) x -= speed * delta;
        if (right) x += speed * delta;

        int spriteSize = 50;
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        x = Math.max(0, Math.min(x, getWidth() - 50));
        y = Math.max(0, Math.min(y, getHeight() - 50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image img = Toolkit.getDefaultToolkit().getImage("src/Sprites/ship.png");
        g.drawImage(img, x, y, 50, 50, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = false;
            case KeyEvent.VK_S -> down = false;
            case KeyEvent.VK_A -> left = false;
            case KeyEvent.VK_D -> right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}

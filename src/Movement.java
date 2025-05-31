import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Movement extends JPanel implements Runnable, KeyListener {

    private final Image shipImg;
    private int x = 100, y = 100;
    private double speed = 300;
    private boolean up, down, left, right;
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    double enemySpawnTimer = 0;

    public Movement() {
        shipImg = Toolkit.getDefaultToolkit().getImage("src/Sprites/ship.png");
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

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(delta);
            if (bullets.get(i).isOffScreen(getHeight())) {
                bullets.remove(i);
                i--;
            }
        }

        //spawner
        enemySpawnTimer -= delta;
        if (enemySpawnTimer <= 0) {
            enemySpawnTimer = 2 + Math.random();
            double spawnY = Math.random() * (getHeight() - 50);
            enemies.add(new Enemy(1000, spawnY));
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta);

            if (enemy.isOffScreen()) {
                enemies.remove(i--);
                continue;
            }

            for (int j = 0; j < bullets.size(); j++) {
                if (enemy.isHitBy(bullets.get(j))) {
                    bullets.remove(j--);
                    enemies.remove(i--);
                    break;
                }
            }
        }

        for (Enemy enemy : enemies) {
            for (Bullet b : enemy.bullets) {
                Rectangle playerBounds = new Rectangle(x, y, 50, 50);
                Rectangle bulletBounds = new Rectangle((int)b.x, (int)b.y, 5, 10);
                if (playerBounds.intersects(bulletBounds)) {
                    System.out.println("Player hit!");
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //bullets
        for (Bullet b : bullets) {
            b.draw(g);
        }
        //player
        g.drawImage(shipImg, x, y, 50, 50, this);
        //enemy
        for (Enemy e : enemies) {
            e.draw(g);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> up = true;
            case KeyEvent.VK_S -> down = true;
            case KeyEvent.VK_A -> left = true;
            case KeyEvent.VK_D -> right = true;
            case KeyEvent.VK_SPACE -> {
                bullets.add(new Bullet(x + 20, y, true));
                bullets.add(new Bullet(x + 20, y+45, true));
            }
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

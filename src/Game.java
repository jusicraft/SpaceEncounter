import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Game extends JPanel implements Runnable, KeyListener {

    private final Image shipImg;
    private int x = 100, y = 100;
    private double speed = 300;
    private boolean up, down, left, right;
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Enemy> enemies = new ArrayList<>();
    double enemySpawnTimer = 0;
    int score = 0;
    boolean gameOver = false;

    public Game() {
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
        if (gameOver) return;

        // Player movement
        if (up) y -= speed * delta;
        if (down) y += speed * delta;
        if (left) x -= speed * delta;
        if (right) x += speed * delta;

        x = Math.max(0, Math.min(x, getWidth() - 50));
        y = Math.max(0, Math.min(y, getHeight() - 50));

        // Bullets
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(delta);
            if (bullets.get(i).isOffScreen(getHeight())) {
                bullets.remove(i--);
            }
        }

        // Enemy spawner
        enemySpawnTimer -= delta;
        if (enemySpawnTimer <= 0) {
            enemySpawnTimer = 2 + Math.random();
            double spawnY = Math.random() * (getHeight() - 50);
            enemies.add(new Enemy(1000, spawnY));
        }

        // Enemies
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            enemy.update(delta);

            if (enemy.isOffScreen()) {
                enemies.remove(i--);
                continue;
            }

            // Enemy hit by player bullet
            for (int j = 0; j < bullets.size(); j++) {
                if (enemy.isHitBy(bullets.get(j))) {
                    bullets.remove(j--);
                    enemies.remove(i--);
                    score += 100;
                    break;
                }
            }
        }

        // Check if player was hit
        Rectangle playerBounds = new Rectangle(x, y, 50, 50);
        for (Enemy enemy : enemies) {
            for (Bullet b : enemy.bullets) {
                Rectangle bulletBounds = new Rectangle((int)b.x, (int)b.y, 5, 10);
                if (playerBounds.intersects(bulletBounds)) {
                    gameOver = true;
                    return;
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
        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 25);
        //display message
        if (gameOver) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", getWidth() / 2 - 130, getHeight() / 2 - 40);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press R to Restart", getWidth() / 2 - 90, getHeight() / 2);
            g.drawString("Press Q to Quit", getWidth() / 2 - 75, getHeight() / 2 + 30);
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
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                restartGame();
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                System.exit(0);
            }
            return;
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

    private void restartGame() {
        x = 100;
        y = 300;
        bullets.clear();
        enemies.clear();
        score = 0;
        gameOver = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}

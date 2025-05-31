package State;

import Entity.Bullet;
import Entity.Enemy;
import Entity.EnemyFactory;
import Game.GameContext;
import Game.GameModel;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlayingState implements GameState {
    private final GameModel model;
    private final Image shipImg = Toolkit.getDefaultToolkit().getImage("src/Sprites/ship.png");
    private double speed = 300;

    public PlayingState(GameModel model) {
        this.model = model;
    }

    @Override
    public void update(double delta) {
        if (model.gameOver) return;

        //movement
        double currentSpeed = model.isBoosting ? speed * 1.8 : speed;

        if (model.isKeyPressed(KeyEvent.VK_W)) model.playerY -= (int) (currentSpeed * delta);
        if (model.isKeyPressed(KeyEvent.VK_S)) model.playerY += (int) (currentSpeed * delta);
        if (model.isKeyPressed(KeyEvent.VK_A)) model.playerX -= (int) (currentSpeed * delta);
        if (model.isKeyPressed(KeyEvent.VK_D)) model.playerX += (int) (currentSpeed * delta);


        model.playerX = Math.max(0, Math.min(model.playerX, 950));
        model.playerY = Math.max(0, Math.min(model.playerY, 450));

        // Bullets
        for (int i = 0; i < model.bullets.size(); i++) {
            Bullet b = model.bullets.get(i);
            b.update(delta);
            if (b.isOffScreen(500)) model.bullets.remove(i--);
        }

        //enemies
        model.enemySpawnTimer -= delta;
        if (model.enemySpawnTimer <= 0) {
            model.enemySpawnTimer = 2 + Math.random();
            model.enemies.add(EnemyFactory.createRandomEnemy(500));
        }

        for (int i = 0; i < model.enemies.size(); i++) {
            Enemy e = model.enemies.get(i);
            e.update(delta);

            if (e.isOffScreen()) {
                model.enemies.remove(i--);
                continue;
            }

            for (int j = 0; j < model.bullets.size(); j++) {
                if (e.isHitBy(model.bullets.get(j))) {
                    model.bullets.remove(j--);
                    model.enemies.remove(i--);
                    model.score += 100;
                    break;
                }
            }
        }

        //player hit
        if (!model.isBoosting) {
            Rectangle playerBounds = new Rectangle(model.playerX, model.playerY, 50, 50);
            for (Enemy e : model.enemies) {
                for (Bullet b : e.getBullets()) {
                    Rectangle bRect = new Rectangle((int)b.getX(), (int)b.getY(), 5, 10);
                    if (playerBounds.intersects(bRect)) {
                        model.gameOver = true;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Composite old = g2d.getComposite();

        if (model.isBoosting) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g2d.drawImage(shipImg, model.playerX, model.playerY, 50, 50, null);
        g2d.setComposite(old);

        for (Bullet b : model.bullets) b.draw(g);
        for (Enemy e : model.enemies) e.draw(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + model.score, 10, 25);

        if (model.gameOver) {
            g.setColor(new Color(0, 0, 0, 200));
            g.fillRect(0, 0, 1000, 500);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 420, 230);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press R to Restart", 440, 270);
            g.drawString("Press Q to Quit", 450, 300);
        }
    }

    @Override
    public void handleInput(KeyEvent e) {
        if (model.gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                model.bullets.clear();
                model.enemies.clear();
                model.score = 0;
                model.playerX = 100;
                model.playerY = 300;
                model.gameOver = false;

                GameContext.getInstance().setState(new PlayingState(model));
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                System.exit(0);
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            GameContext.getInstance().setState(new PausedState(model));
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D -> model.setKeyState(e.getKeyCode(), true);
            case KeyEvent.VK_SPACE -> {
                if (!model.isBoosting) {
                    model.bullets.add(new Bullet(model.playerX + 20, model.playerY, true));
                    model.bullets.add(new Bullet(model.playerX + 20, model.playerY + 45, true));
                }
            }
        }
    }
}
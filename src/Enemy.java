import java.awt.*;
import java.util.ArrayList;

class Enemy {
    double x, y;
    int width = 50, height = 50;
    double speed = 100; // moves left
    Image sprite;
    ArrayList<Bullet> bullets = new ArrayList<>();
    double shootCooldown = 0;

    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
        sprite = Toolkit.getDefaultToolkit().getImage("src/Sprites/enemy.png");
    }

    public void update(double delta) {
        x -= speed * delta;

        shootCooldown -= delta;
        if (shootCooldown <= 0) {
            shootCooldown = 1.5 + Math.random();
            bullets.add(new Bullet(x, y + height / 2, false));
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).update(delta);
            if (bullets.get(i).isOffScreen(600)) {
                bullets.remove(i--);
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(sprite, (int)x, (int)y, width, height, null);
        for (Bullet b : bullets) {
            b.draw(g);
        }
    }

    public boolean isHitBy(Bullet b) {
        Rectangle enemyBounds = new Rectangle((int)x, (int)y, width, height);
        return enemyBounds.intersects(new Rectangle((int)b.x, (int)b.y, 5, 10));
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }
}


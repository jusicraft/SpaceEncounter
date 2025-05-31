package Entity;

import java.awt.*;

public class Bullet {
    double x, y;
    double speed = 600;
    boolean direction;

    public Bullet(double x, double y, boolean direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public void update(double delta) {
        x += (direction ? 1 : -1) * speed * delta;
    }

    public boolean isOffScreen(int height) {
        return x < -10;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect((int)x, (int)y, 20, 5);
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
}


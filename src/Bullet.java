import java.awt.*;

class Bullet {
    double x, y;
    double speed = 600;

    public Bullet(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update(double delta) {
        x += speed * delta;
    }

    public boolean isOffScreen(int height) {
        return x < -10;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect((int)x, (int)y, 20, 5);
    }
}


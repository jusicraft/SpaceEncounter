package Entity;

public class EnemyFactory {
    public static Enemy createRandomEnemy(int panelHeight) {
        double y = Math.random() * (panelHeight - 50);
        return new Enemy(1000, y);
    }
}
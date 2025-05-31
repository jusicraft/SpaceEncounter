package Game;

import Entity.Bullet;
import Entity.Enemy;

import java.util.ArrayList;

public class GameModel {
    private static final GameModel instance = new GameModel();
    public static GameModel getInstance() { return instance; }

    public int playerX = 100, playerY = 300;
    public int score = 0;
    public boolean gameOver = false;
    public double enemySpawnTimer = 0;
    public boolean isBoosting = false;
    public double baseEnemySpawnRate = 2.0;


    public final ArrayList<Bullet> bullets = new ArrayList<>();
    public final ArrayList<Enemy> enemies = new ArrayList<>();
    public final boolean[] keys = new boolean[256];

    public void setKeyState(int keyCode, boolean pressed) {
        if (keyCode >= 0 && keyCode < keys.length) keys[keyCode] = pressed;
    }


    public boolean isKeyPressed(int keyCode) {
        return keyCode >= 0 && keyCode < keys.length && keys[keyCode];
    }
}
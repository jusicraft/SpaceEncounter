import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("WSAD Sprite Movement");
        Game gamePanel = new Game();

        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
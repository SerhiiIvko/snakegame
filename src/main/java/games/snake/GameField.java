package games.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private int appleX;
    private int appleY;
    private int dots;
    private int count = 0;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private boolean left;
    private boolean right;
    private boolean down;
    private boolean up;
    private boolean inGame = false;
    private Image dot;
    private Image apple;
    private JButton startButton;
    private JLabel scoreLabel;

    GameField() {
        setLayout(new GridBagLayout());
        setBackground(Color.black);
        loadImage();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        Timer timer = new Timer(270, this);
        timer.start();
        startButton = new JButton(GameParameters.START_BUTTON_TEXT);
        startButton.addActionListener(e -> startGame());
        scoreLabel = new JLabel();
        scoreLabel.setForeground(Color.white);
        scoreLabel.setVisible(false);
        add(scoreLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        add(startButton, new GridBagConstraints(0, 1, 1, 1, 0, 0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 0, 0, 0), 0, 0));
    }

    private void startGame() {
        count = 0;
        left = false;
        right = true;
        down = false;
        up = false;
        dots = 3;
        inGame = true;
        startButton.setVisible(false);
        scoreLabel.setVisible(false);
        for (int i = 0; i < dots; i++) {
            x[i] = dots * DOT_SIZE - i * DOT_SIZE;
            y[i] = dots * DOT_SIZE;
        }
        createApple();
    }

    private void createApple() {
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
    }

    private void loadImage() {
        ImageIcon appleIcon = new ImageIcon(GameParameters.APPLE_FILE_NAME);
        apple = appleIcon.getImage();
        ImageIcon dotIcon = new ImageIcon(GameParameters.DOT_FILE_NAME);
        dot = dotIcon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            startButton.setVisible(true);
            String str = GameParameters.COUNT_STRING + count;
            scoreLabel.setText(str);
            scoreLabel.setVisible(true);
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] = x[0] - DOT_SIZE;
        }
        if (right) {
            x[0] = x[0] + DOT_SIZE;
        }
        if (down) {
            y[0] = y[0] + DOT_SIZE;
        }
        if (up) {
            y[0] = y[0] - DOT_SIZE;
        }
    }

    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            count++;
            createApple();
        }
    }

    private void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }
        int SIZE = 320;
        if (x[0] > SIZE || x[0] < 0 || y[0] > SIZE || y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            } else if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = false;
                right = false;
            } else if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = false;
                right = false;
            }
        }
    }
}
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
    private Image dot;
    private Image apple;
    private int appleX;
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private boolean left;
    private boolean right;
    private boolean down;
    private boolean up;
    private boolean inGame = false;
    private int count = 0;

    GameField() {
        setBackground(Color.black);
        loadImage();
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
        Timer timer = new Timer(270, this);
        timer.start();
        startGame();
    }

    private void startGame() {
        left = false;
        right = true;
        down = false;
        up = false;
        dots = 3;
        inGame = true;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
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
            String str = GameParameters.COUNT_STRING + count;
            g.setColor(Color.white);
            drawStringMiddleOfPanel(str, g);
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

    private void drawStringMiddleOfPanel(String string, Graphics graphics) {
        int stringWidth;
        int stringAccent;
        int xCoordinate;
        int yCoordinate;
        FontMetrics fontMetrics = graphics.getFontMetrics();
        stringWidth = fontMetrics.stringWidth(string);
        stringAccent = fontMetrics.getAscent();
        xCoordinate = getWidth() / 2 - stringWidth / 2;
        yCoordinate = getHeight() / 2 + stringAccent / 2;
        graphics.drawString(string, xCoordinate, yCoordinate);
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
//            else if (key == KeyEvent.VK_ENTER) {
//                inGame = true;
//                startGame();
//            }
        }
    }
}
package games.snake;

import javax.swing.*;

class MainWindow extends JFrame {

    MainWindow() {
        setTitle(GameParameters.TITLE_MESSAGE);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(GameParameters.WIDTH_SIZE, GameParameters.HEIGHT_SIZE);
        setLocationRelativeTo(null);
        add(new GameField());
        setVisible(true);
    }
}
package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends ShipsPanel {
    private final JPanel userField;
    private final JPanel enemyField;
    private final JButton[][] userButtons;
    private final JButton[][] enemyButtons;

    public GamePanel(Config config) {
        super(config, config.fieldBackground);

        userField = new JPanel();
        userField.setLayout(new GridLayout(fieldHeight, fieldWidth));
        enemyField = new JPanel();
        enemyField.setLayout(new GridLayout(fieldHeight, fieldWidth));
        userButtons = new JButton[fieldHeight][fieldWidth];
        enemyButtons = new JButton[fieldHeight][fieldWidth];

        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                // CR: button init is the same here and in SettingShips panel, can be moved to some ViewUtils and called
                userButtons[y][x] = new JButton();
                userButtons[y][x].setPreferredSize(new Dimension(50, 50));
                userButtons[y][x].setIcon(empty);
                userButtons[y][x].setFocusPainted(false);
                userButtons[y][x].setContentAreaFilled(false);

                userField.add(userButtons[y][x]);
                int finalX = x;
                int finalY = y;
                enemyButtons[y][x] = new JButton();
                enemyButtons[y][x].setPreferredSize(new Dimension(50, 50));
                enemyButtons[y][x].setIcon(empty);
                enemyButtons[y][x].setFocusPainted(false);
                enemyButtons[y][x].setContentAreaFilled(false);
                enemyButtons[y][x].addActionListener(e -> listener.makeShot(finalY * fieldWidth + finalX));
                enemyField.add(enemyButtons[y][x]);
            }
        }
        this.setLayout(null);
        Dimension userFieldSize = userField.getPreferredSize();
        userField.setBounds(100, 100, userFieldSize.width, userFieldSize.height);
        userField.setOpaque(false);
        this.add(userField);
        Dimension enemyFieldSize = enemyField.getPreferredSize();
        enemyField.setBounds(700, 100, enemyFieldSize.width, enemyFieldSize.height);
        enemyField.setOpaque(false);
        this.add(enemyField);
    }

    public void setPlayerNames(String userName, String enemyName) {
        userField.setBorder(BorderFactory.createTitledBorder(userName + "'s field"));
        enemyField.setBorder(BorderFactory.createTitledBorder(enemyName + "'s field"));
    }

    public void updateGameFields(String userField, String computerField) {
        updateField(userField, userButtons);
        updateField(computerField, enemyButtons);
    }
}

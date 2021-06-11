package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends WindowPanel {
    protected final Config config;
    protected final int fieldWidth;
    protected final int fieldHeight;
    protected ClickListener listener;
    private final JPanel userField;
    private final JPanel enemyField;
    private final JButton[][] userButtons;
    private final JButton[][] enemyButtons;

    public GamePanel(Config config, ClickListener listener) {
        super(System.getProperty("file.separator") + "Field.png");
        this.config = config;
        this.fieldWidth = config.fieldWidth;
        this.fieldHeight = config.fieldHeight;
        this.listener = listener;
        userField = new JPanel();
        userField.setLayout(new GridLayout(fieldHeight, fieldWidth));
        enemyField = new JPanel();
        enemyField.setLayout(new GridLayout(fieldHeight, fieldWidth));
        userButtons = new JButton[fieldHeight][fieldWidth];
        enemyButtons = new JButton[fieldHeight][fieldWidth];

        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                userButtons[y][x] = FieldButtons.initButtonForField(FieldButtons.empty);
                userField.add(userButtons[y][x]);
                int finalX = x;
                int finalY = y;
                enemyButtons[y][x] = FieldButtons.initButtonForField(FieldButtons.empty);
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
        FieldButtons.updateField(fieldWidth, fieldHeight, userField, userButtons);
        FieldButtons.updateField(fieldWidth, fieldHeight, computerField, enemyButtons);
    }
}

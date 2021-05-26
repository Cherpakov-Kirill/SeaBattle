package nsu.seabattle.view;

import nsu.seabattle.config.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Panel extends JPanel {
    private ImageIcon alive;
    private ImageIcon killed;
    private ImageIcon injured;
    private ImageIcon missed;
    private ImageIcon empty;

    private final Config config;
    private final int fieldWidth;
    private final int fieldHeight;
    private ClickListener listener = null;

    private final WindowPanel allPanel;
    private final JPanel userField;
    private final JPanel enemyField;

    private final WindowPanel allSettingShips;
    private JPanel settingShips;

    private final JButton[][] userButtons;
    private final JButton[][] enemyButtons;
    private final JButton[][] settingShipButtons;

    JLabel theEnd;

    private ImageIcon getImageIcon(ButtonsIcons type) {
        URL file = Frame.class.getResource(config.getFileNameForButton(type));
        if (file == null) {
            BufferedImage defaultBackground = new BufferedImage(1300, 943, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = defaultBackground.createGraphics();
            graphics.setPaint(config.gerColorForButton(type));
            graphics.fillRect(0, 0, defaultBackground.getWidth(), defaultBackground.getHeight());
            return new ImageIcon(defaultBackground);
        } else {
            return new ImageIcon(file);
        }
    }

    private void initButtonsIcons() {
        alive = getImageIcon(ButtonsIcons.ALIVE);
        killed = getImageIcon(ButtonsIcons.DEAD);
        injured = getImageIcon(ButtonsIcons.INJURED);
        missed = getImageIcon(ButtonsIcons.MISS);
        empty = getImageIcon(ButtonsIcons.EMPTY);
    }

    public Panel(Config config) {
        this.config = config;
        this.fieldWidth = config.fieldWidth;
        this.fieldHeight = config.fieldHeight;
        initButtonsIcons();
        allPanel = new WindowPanel("/Field.png");
        userField = new JPanel();
        userField.setLayout(new GridLayout(fieldHeight, fieldWidth));
        enemyField = new JPanel();
        enemyField.setLayout(new GridLayout(fieldHeight, fieldWidth));
        userButtons = new JButton[fieldHeight][fieldWidth];
        enemyButtons = new JButton[fieldHeight][fieldWidth];

        allSettingShips = new WindowPanel("/4Deck.png");
        settingShips = new JPanel();
        settingShipButtons = new JButton[fieldHeight][fieldWidth];
        settingShips.setLayout(new GridLayout(fieldHeight, fieldWidth));

        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                userButtons[y][x] = new JButton();
                userButtons[y][x].setPreferredSize(new Dimension(50, 50));
                userButtons[y][x].setIcon(empty);
                //userButtons[y][x].setBorderPainted(false);
                userButtons[y][x].setFocusPainted(false);
                userButtons[y][x].setContentAreaFilled(false);

                userField.add(userButtons[y][x]);
                int finalX = x;
                int finalY = y;
                enemyButtons[y][x] = new JButton();
                enemyButtons[y][x].setPreferredSize(new Dimension(50, 50));
                enemyButtons[y][x].setIcon(empty);
                //enemyButtons[y][x].setBorderPainted(false);
                enemyButtons[y][x].setFocusPainted(false);
                enemyButtons[y][x].setContentAreaFilled(false);
                enemyButtons[y][x].addActionListener(e -> listener.makeShot(finalY * fieldWidth + finalX));
                enemyField.add(enemyButtons[y][x]);

                settingShipButtons[y][x] = new JButton();
                settingShipButtons[y][x].setPreferredSize(new Dimension(50, 50));
                settingShipButtons[y][x].setIcon(empty);
                settingShipButtons[y][x].setFocusPainted(false);
                settingShipButtons[y][x].setContentAreaFilled(false);
                settingShipButtons[y][x].addActionListener(e -> listener.setCoordinateOfNewShip(finalY * fieldWidth + finalX));
                settingShips.add(settingShipButtons[y][x]);
                //enemyField[y][x].setBorderPainted(false);
            }
        }
        allPanel.setLayout(null);
        Dimension userFieldSize = userField.getPreferredSize();
        userField.setBounds(100, 100, userFieldSize.width, userFieldSize.height);
        userField.setOpaque(false);
        allPanel.add(userField);
        Dimension enemyFieldSize = enemyField.getPreferredSize();
        enemyField.setBounds(700, 100, enemyFieldSize.width, enemyFieldSize.height);
        enemyField.setOpaque(false);
        allPanel.add(enemyField);
        allSettingShips.setLayout(null);
        Dimension settingShipsSize = settingShips.getPreferredSize();
        settingShips.setBounds(400, 100, settingShipsSize.width, settingShipsSize.height);
        allSettingShips.add(settingShips);
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public void setPlayerNames(String userName, String enemyName) {
        userField.setBorder(BorderFactory.createTitledBorder(userName + "'s field"));
        enemyField.setBorder(BorderFactory.createTitledBorder(enemyName + "'s field"));
    }

    public WindowPanel getAllSettingShips() {
        return allSettingShips;
    }

    public JPanel getAllPanel() {
        return allPanel;
    }

    public WindowPanel updateSettingShipsField(String field) {
        updateField(field, settingShipButtons);
        return allSettingShips;
    }

    public JPanel updateGameFields(String userField, String computerField) {
        updateField(userField, userButtons);
        updateField(computerField, enemyButtons);
        return allPanel;
    }

    private void updateField(String field, JButton[][] matrix) {
        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                switch (field.charAt(y * fieldWidth + x)) {
                    case '-' -> matrix[y][x].setIcon(empty);
                    case '#' -> matrix[y][x].setIcon(alive);
                    case '@' -> matrix[y][x].setIcon(killed);
                    case '~' -> matrix[y][x].setIcon(missed);
                    case '*' -> matrix[y][x].setIcon(injured);
                }
            }
        }
    }

    public void addTheEnd(String winner) {
        theEnd = new JLabel(winner + " is winner!!");
        Dimension size = theEnd.getPreferredSize();
        theEnd.setBounds(590, 750, size.width, size.height);
        theEnd.setForeground(Color.red);
        allPanel.add(theEnd);
        allPanel.repaint();
    }

    interface ClickListener {
        void makeShot(int squareNum);

        void setCoordinateOfNewShip(int buttonNumber);
    }
}

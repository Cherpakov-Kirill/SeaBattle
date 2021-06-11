package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.ship.Position;
import nsu.seabattle.model.ship.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SettingShipsPanel extends WindowPanel {
    protected final Config config;
    protected final int fieldWidth;
    protected final int fieldHeight;
    protected ClickListener listener;
    private final JButton[][] settingShipButtons;
    private int numberOfDecks;
    private int numberOfCurrType;
    private int numberOfShip;
    private final List<Ship> ships;
    private final StringBuffer field;
    private Position lastCoordinates = null;
    private final String[] filesForSettingShipsBackgrounds;

    public SettingShipsPanel(Config config, ClickListener listener) {
        super(System.getProperty("file.separator") + "4Deck.png");
        this.config = config;
        this.fieldWidth = config.fieldWidth;
        this.fieldHeight = config.fieldHeight;
        this.listener = listener;
        String fileSeparator = System.getProperty("file.separator");
        filesForSettingShipsBackgrounds = new String[]{fileSeparator + "1Deck.png", fileSeparator + "2Deck.png", fileSeparator + "3Deck.png", fileSeparator + "4Deck.png"};
        JPanel settingShips = new JPanel();
        settingShips.setLayout(new GridLayout(fieldHeight, fieldWidth));
        settingShipButtons = new JButton[fieldHeight][fieldWidth];
        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                int finalX = x;
                int finalY = y;
                settingShipButtons[y][x] = FieldButtons.initButtonForField(FieldButtons.empty);
                settingShipButtons[y][x].addActionListener(e -> setCoordinateOfNewShip(finalY * fieldWidth + finalX));
                settingShips.add(settingShipButtons[y][x]);
            }
        }
        this.setLayout(null);
        Dimension settingShipsSize = settingShips.getPreferredSize();
        settingShips.setBounds(400, 100, settingShipsSize.width, settingShipsSize.height);
        this.add(settingShips);
        numberOfDecks = 4;
        numberOfCurrType = 0;
        numberOfShip = 0;
        ships = new ArrayList<>();
        field = new StringBuffer("-".repeat(fieldHeight * fieldWidth));
        updateSettingShipsField(field.toString());
    }

    private Position getPositionOfNewShip(int buttonNumber) {
        return new Position(buttonNumber % fieldWidth, buttonNumber / fieldWidth);
    }

    private void setCoordinateOfNewShip(int buttonNumber) {
        Position newCoordinate = getPositionOfNewShip(buttonNumber);
        if (lastCoordinates == null) {
            if (numberOfDecks == 1) {
                if (IsIntervalEmpty(newCoordinate, newCoordinate)) {
                    ships.add(new Ship(newCoordinate, newCoordinate));
                    numberOfShip++;
                    field.setCharAt(newCoordinate.y * fieldWidth + newCoordinate.x, '#');
                    updateSettingShipsField(field.toString());
                    if (numberOfShip >= config.ships.get(numberOfCurrType)) {
                        listener.setShips(ships);
                    }
                }
            } else {
                if (IsIntervalEmpty(newCoordinate, newCoordinate)) {
                    field.setCharAt(newCoordinate.y * fieldWidth + newCoordinate.x, '#');
                    updateSettingShipsField(field.toString());
                    lastCoordinates = newCoordinate;
                }
            }
        } else {
            Position first = lastCoordinates;
            int lengthX = abs(first.x - newCoordinate.x) + 1;
            int lengthY = abs(first.y - newCoordinate.y) + 1;
            if (((lengthX != 1 || lengthY != numberOfDecks) && (lengthX != numberOfDecks || lengthY != 1)) || !IsIntervalEmpty(first, newCoordinate)) {
                System.out.println("Sorry! Incorrect interval of the ship!");
                field.setCharAt(lastCoordinates.y * fieldWidth + lastCoordinates.x, '-');
                lastCoordinates = null;
                updateSettingShipsField(field.toString());
                return;
            }

            ships.add(new Ship(first, newCoordinate));
            if (first.x == newCoordinate.x) {
                int startY = Math.min(first.y, newCoordinate.y);
                int finishY = Math.max(first.y, newCoordinate.y);
                for (int i = startY; i <= finishY; i++) {
                    field.setCharAt(i * fieldWidth + first.x, '#');
                }
            } else {
                int startX = Math.min(first.x, newCoordinate.x);
                int finishX = Math.max(first.x, newCoordinate.x);
                for (int i = startX; i <= finishX; i++) {
                    field.setCharAt(first.y * fieldWidth + i, '#');
                }
            }
            updateSettingShipsField(field.toString());
            if (numberOfDecks == 1 && numberOfShip == config.ships.get(config.ships.size() - 1)) {
                listener.setShips(ships);
            }
            lastCoordinates = null;
            numberOfShip++;
            if (numberOfShip >= config.ships.get(numberOfCurrType)) {
                numberOfCurrType++;
                numberOfShip = 0;
                numberOfDecks--;
            }
            if (numberOfDecks != 0) {
                setImageIcon(filesForSettingShipsBackgrounds[numberOfDecks - 1]);
            }
        }
    }

    private boolean IsCoordinateEmpty(int x, int y) {
        if (x >= fieldWidth || x < 0) return false;
        if (y >= fieldHeight || y < 0) return false;
        if (lastCoordinates != null && x == lastCoordinates.x && y == lastCoordinates.y) return false;
        return field.charAt(y * fieldWidth + x) == '#';
    }

    private boolean IsIntervalEmpty(Position FirstPair, Position LastPair) {
        if (FirstPair.x == LastPair.x) {
            int xPos = FirstPair.x;
            int yPosStart = Math.min(FirstPair.y, LastPair.y);
            int yPosFinish = Math.max(FirstPair.y, LastPair.y);

            int xPosStart = xPos - 1;
            while (xPosStart <= xPos + 1) {
                if (IsCoordinateEmpty(xPosStart, yPosStart - 1)) return false;
                if (IsCoordinateEmpty(xPosStart, yPosFinish + 1)) return false;
                xPosStart++;
            }

            while (yPosStart <= yPosFinish) {
                if (IsCoordinateEmpty(xPos, yPosStart)) return false;
                if (IsCoordinateEmpty(xPos - 1, yPosStart)) return false;
                if (IsCoordinateEmpty(xPos + 1, yPosStart)) return false;
                yPosStart++;
            }

        } else {
            int yPos = FirstPair.y;
            int xPosStart = Math.min(FirstPair.x, LastPair.x);
            int xPosFinish = Math.max(FirstPair.x, LastPair.x);

            int yPosStart = yPos - 1;
            while (yPosStart <= yPos + 1) {
                if (IsCoordinateEmpty(xPosStart - 1, yPosStart)) return false;
                if (IsCoordinateEmpty(xPosFinish + 1, yPosStart)) return false;
                yPosStart++;
            }
            while (xPosStart <= xPosFinish) {
                if (IsCoordinateEmpty(xPosStart, yPos)) return false;
                if (IsCoordinateEmpty(xPosStart, yPos - 1)) return false;
                if (IsCoordinateEmpty(xPosStart, yPos + 1)) return false;
                xPosStart++;
            }
        }
        return true;
    }

    public void updateSettingShipsField(String field) {
        FieldButtons.updateField(fieldWidth, fieldHeight, field, settingShipButtons);
    }
}

package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.ship.Position;
import nsu.seabattle.model.ship.Ship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class SettingShipsPanel extends ShipsPanel {
    private JPanel settingShips;
    private final JButton[][] settingShipButtons;
    private int numberOfDecks;
    private int numberOfCurrType;
    private int numberOfShip;
    private final List<Ship> ships;
    private final StringBuffer field;
    private Position lastCoordinates = null;

    public SettingShipsPanel(Config config) {
        super(config, config.getFileNameForSettingShipsBackground(config.ships.size() - 1));
        settingShips = new JPanel();
        settingShips.setLayout(new GridLayout(fieldHeight, fieldWidth));
        settingShipButtons = new JButton[fieldHeight][fieldWidth];
        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                int finalX = x;
                int finalY = y;
                settingShipButtons[y][x] = new JButton();
                settingShipButtons[y][x].setPreferredSize(new Dimension(50, 50));
                settingShipButtons[y][x].setIcon(empty);
                settingShipButtons[y][x].setFocusPainted(false);
                settingShipButtons[y][x].setContentAreaFilled(false);
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
        field = new StringBuffer("-".repeat(config.fieldHeight * config.fieldWidth));
        updateSettingShipsField(field.toString());
    }

    private Position getPositionOfNewShip(int buttonNumber) {
        return new Position(buttonNumber % config.fieldWidth, buttonNumber / config.fieldWidth);
    }

    private void setCoordinateOfNewShip(int buttonNumber) {
        Position newCoordinate = getPositionOfNewShip(buttonNumber);
        if (lastCoordinates == null) {
            lastCoordinates = newCoordinate;
            if (numberOfDecks == 1) {
                lastCoordinates = null;
                if (checkIntervalOnMapUser(newCoordinate, newCoordinate)) {
                    ships.add(new Ship(newCoordinate, newCoordinate));
                    numberOfShip++;
                    if (numberOfShip >= config.ships.get(numberOfCurrType)) {
                        listener.setShips(ships);
                    }
                }
            }
            if (checkIntervalOnMapUser(newCoordinate, newCoordinate)) {
                field.setCharAt(newCoordinate.y * config.fieldWidth + newCoordinate.x, '#');
                updateSettingShipsField(field.toString());
            } else lastCoordinates = null;
            return;
        }
        Position first = lastCoordinates;
        int lengthX = abs(first.x - newCoordinate.x) + 1;
        int lengthY = abs(first.y - newCoordinate.y) + 1;
        if (((lengthX != 1 || lengthY != numberOfDecks) && (lengthX != numberOfDecks || lengthY != 1)) || !checkIntervalOnMapUser(first, newCoordinate)) {
            System.out.println("Sorry! Incorrect interval of the ship!");
            field.setCharAt(lastCoordinates.y * config.fieldWidth + lastCoordinates.x, '-');
            lastCoordinates = null;
            updateSettingShipsField(field.toString());
            return;
        }

        ships.add(new Ship(first, newCoordinate));
        if (first.x == newCoordinate.x) {
            int startY;
            int finishY;
            if (first.y < newCoordinate.y) {
                startY = first.y;
                finishY = newCoordinate.y;
            } else {
                startY = newCoordinate.y;
                finishY = first.y;
            }
            for (int i = startY; i <= finishY; i++) {
                field.setCharAt(i * config.fieldWidth + first.x, '#');
            }
        } else {
            int startX;
            int finishX;
            if (first.x < newCoordinate.x) {
                startX = first.x;
                finishX = newCoordinate.x;
            } else {
                startX = newCoordinate.x;
                finishX = first.x;
            }
            for (int i = startX; i <= finishX; i++) {
                field.setCharAt(first.y * config.fieldWidth + i, '#');
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
            setImageIcon(config.getFileNameForSettingShipsBackground(numberOfDecks - 1));
        }
    }

    private boolean checkUserCoordinateForShip(int x, int y) {
        if (x >= config.fieldWidth || x < 0) return false;
        if (y >= config.fieldHeight || y < 0) return false;
        if (lastCoordinates != null && x == lastCoordinates.x && y == lastCoordinates.y) return false;
        return field.charAt(y * config.fieldWidth + x) == '#';
    }

    private boolean checkIntervalOnMapUser(Position FirstPair, Position LastPair) {
        if (FirstPair.x >= config.fieldWidth || FirstPair.x < 0) return false;
        if (FirstPair.y >= config.fieldHeight || FirstPair.y < 0) return false;
        if (LastPair.x >= config.fieldWidth || LastPair.x < 0) return false;
        if (LastPair.y >= config.fieldHeight || LastPair.y < 0) return false;

        if (FirstPair.x == LastPair.x) {
            int xPos = FirstPair.x;
            int yPosStart;
            int yPosFinish;
            if (FirstPair.y < LastPair.y) {
                yPosStart = FirstPair.y;
                yPosFinish = LastPair.y;
            } else {
                yPosStart = LastPair.y;
                yPosFinish = FirstPair.y;
            }
            if (checkUserCoordinateForShip(xPos, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(xPos + 1, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(xPos - 1, yPosStart - 1)) return false;

            if (checkUserCoordinateForShip(xPos, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(xPos + 1, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(xPos - 1, yPosFinish + 1)) return false;
            while (yPosStart <= yPosFinish) {
                if (checkUserCoordinateForShip(xPos, yPosStart)) return false;
                if (checkUserCoordinateForShip(xPos - 1, yPosStart)) return false;
                if (checkUserCoordinateForShip(xPos + 1, yPosStart)) return false;
                yPosStart++;
            }

        } else {
            int yPos = FirstPair.y;
            int xPosStart;
            int xPosFinish;
            if (FirstPair.x < LastPair.x) {
                xPosStart = FirstPair.x;
                xPosFinish = LastPair.x;
            } else {
                xPosStart = LastPair.x;
                xPosFinish = FirstPair.x;
            }
            if (checkUserCoordinateForShip(xPosStart - 1, yPos)) return false;
            if (checkUserCoordinateForShip(xPosStart - 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(xPosStart - 1, yPos - 1)) return false;

            if (checkUserCoordinateForShip(xPosFinish + 1, yPos)) return false;
            if (checkUserCoordinateForShip(xPosFinish + 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(xPosFinish + 1, yPos - 1)) return false;
            while (xPosStart <= xPosFinish) {
                if (checkUserCoordinateForShip(xPosStart, yPos)) return false;
                if (checkUserCoordinateForShip(xPosStart, yPos - 1)) return false;
                if (checkUserCoordinateForShip(xPosStart, yPos + 1)) return false;
                xPosStart++;
            }
        }
        return true;
    }

    public void updateSettingShipsField(String field) {
        updateField(field, settingShipButtons);
    }
}

package nsu.seabattle.model;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.ship.*;

import java.util.ArrayList;
import java.util.List;

public class ModelUtils {

    private static int randomize(int interval) {
        return (int) (Math.random() * interval);
    }

    private static void setShipOnField(char[][] field, List<Ship> ships, Position firstCoordinate, Position lastCoordinate, int numberOfDecks, int numberOfShip) {
        if (firstCoordinate.x == lastCoordinate.x) {
            Position current;
            if (firstCoordinate.y < lastCoordinate.y) {
                current = new Position(firstCoordinate);
            } else {
                current = new Position(lastCoordinate);
            }
            for (int i = 0; i < numberOfDecks; i++) {
                field[current.y][current.x] = '#';
                current.y++;
            }
        } else {
            Position current;
            if (firstCoordinate.x < lastCoordinate.x) {
                current = new Position(firstCoordinate);
            } else {
                current = new Position(lastCoordinate);
            }
            for (int i = 0; i < numberOfDecks; i++) {
                field[current.y][current.x] = '#';
                current.x++;
            }
        }
        ships.add(new Ship(firstCoordinate, lastCoordinate));
    }

    private static boolean checkUserCoordinateForShip(Config config, char[][] field, int x, int y) {
        if (x >= config.fieldWidth || x < 0) return false;
        if (y >= config.fieldHeight || y < 0) return false;
        return field[y][x] == '#';
    }

    private static boolean checkIntervalOnMapUser(Config config, char[][] field, Position FirstPair, Position LastPair) {
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
            if (checkUserCoordinateForShip(config, field, xPos, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(config, field, xPos + 1, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(config, field, xPos - 1, yPosStart - 1)) return false;

            if (checkUserCoordinateForShip(config, field, xPos, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(config, field, xPos + 1, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(config, field, xPos - 1, yPosFinish + 1)) return false;
            while (yPosStart <= yPosFinish) {
                if (checkUserCoordinateForShip(config, field, xPos, yPosStart)) return false;
                if (checkUserCoordinateForShip(config, field, xPos - 1, yPosStart)) return false;
                if (checkUserCoordinateForShip(config, field, xPos + 1, yPosStart)) return false;
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
            if (checkUserCoordinateForShip(config, field, xPosStart - 1, yPos)) return false;
            if (checkUserCoordinateForShip(config, field, xPosStart - 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(config, field, xPosStart - 1, yPos - 1)) return false;

            if (checkUserCoordinateForShip(config, field, xPosFinish + 1, yPos)) return false;
            if (checkUserCoordinateForShip(config, field, xPosFinish + 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(config, field, xPosFinish + 1, yPos - 1)) return false;
            while (xPosStart <= xPosFinish) {
                if (checkUserCoordinateForShip(config, field, xPosStart, yPos)) return false;
                if (checkUserCoordinateForShip(config, field, xPosStart, yPos - 1)) return false;
                if (checkUserCoordinateForShip(config, field, xPosStart, yPos + 1)) return false;
                xPosStart++;
            }
        }
        return true;
    }

    private static void findNewPositionForCurrentShip(Config config, char[][] field, List<Ship> ships, int numberOfDecks, int numberOfShip) {
        int squareOfMap = config.fieldHeight * config.fieldWidth;
        boolean correctNewPosition = false;
        Position FirstCoordinates = null;
        Position LastCoordinates = null;
        while (!correctNewPosition) {
            int randomValue = randomize(squareOfMap);
            int x = randomValue % 10;
            int y = randomValue / 10;
            FirstCoordinates = new Position(x, y);
            int randomOrientation = randomize(4);
            switch (randomOrientation) {
                case 0:
                    if (checkIntervalOnMapUser(config, field, FirstCoordinates, new Position(x + (numberOfDecks - 1), y))) {
                        LastCoordinates = new Position(x + (numberOfDecks - 1), y);
                        correctNewPosition = true;
                    }
                    break;
                case 1:
                    if (checkIntervalOnMapUser(config, field, FirstCoordinates, new Position(x - (numberOfDecks - 1), y))) {
                        LastCoordinates = new Position(x - (numberOfDecks - 1), y);
                        correctNewPosition = true;
                    }
                    break;
                case 2:
                    if (checkIntervalOnMapUser(config, field, FirstCoordinates, new Position(x, y + (numberOfDecks - 1)))) {
                        LastCoordinates = new Position(x, y + (numberOfDecks - 1));
                        correctNewPosition = true;
                    }
                    break;
                case 3:
                    if (checkIntervalOnMapUser(config, field, FirstCoordinates, new Position(x, y - (numberOfDecks - 1)))) {
                        LastCoordinates = new Position(x, y - (numberOfDecks - 1));
                        correctNewPosition = true;
                    }
                    break;
            }
        }
        setShipOnField(field, ships, FirstCoordinates, LastCoordinates, numberOfDecks, numberOfShip);
    }

    public static List<Ship> generateShips(Config config) {
        char[][] field = new char[config.fieldHeight][config.fieldWidth];
        for (int i = 0; i < config.fieldHeight; i++) {
            for (int j = 0; j < config.fieldWidth; j++) {
                field[i][j] = '-';
            }
        }
        List<Ship> ships = new ArrayList<>();
        int numberOfDecks = 4;
        for (int numberShipsOfCurrType : config.ships) {
            for (int numberOfShip = 0; numberOfShip < numberShipsOfCurrType; numberOfShip++) {
                findNewPositionForCurrentShip(config, field, ships, numberOfDecks, numberOfShip);
            }
            numberOfDecks--;
        }
        return ships;
    }

}

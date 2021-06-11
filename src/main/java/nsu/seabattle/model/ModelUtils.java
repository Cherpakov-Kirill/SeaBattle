package nsu.seabattle.model;

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

    private static boolean checkUserCoordinateForShip(int fieldWidth, int fieldHeight, char[][] field, int x, int y) {
        if (x >= fieldWidth || x < 0) return false;
        if (y >= fieldHeight || y < 0) return false;
        return field[y][x] == '#';
    }

    private static boolean checkIntervalOnMapUser(int fieldWidth, int fieldHeight, char[][] field, Position FirstPair, Position LastPair) {
        if (FirstPair.x >= fieldWidth || FirstPair.x < 0) return false;
        if (FirstPair.y >= fieldHeight || FirstPair.y < 0) return false;
        if (LastPair.x >= fieldWidth || LastPair.x < 0) return false;
        if (LastPair.y >= fieldHeight || LastPair.y < 0) return false;

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
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos + 1, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos - 1, yPosStart - 1)) return false;

            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos + 1, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos - 1, yPosFinish + 1)) return false;
            while (yPosStart <= yPosFinish) {
                if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos, yPosStart)) return false;
                if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos - 1, yPosStart)) return false;
                if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPos + 1, yPosStart)) return false;
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
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosStart - 1, yPos)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosStart - 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosStart - 1, yPos - 1)) return false;

            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosFinish + 1, yPos)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosFinish + 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosFinish + 1, yPos - 1)) return false;
            while (xPosStart <= xPosFinish) {
                if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosStart, yPos)) return false;
                if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosStart, yPos - 1)) return false;
                if (checkUserCoordinateForShip(fieldWidth, fieldHeight, field, xPosStart, yPos + 1)) return false;
                xPosStart++;
            }
        }
        return true;
    }

    private static void findNewPositionForCurrentShip(int fieldWidth, int fieldHeight, char[][] field, List<Ship> ships, int numberOfDecks, int numberOfShip) {
        int squareOfMap = fieldHeight * fieldWidth;
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
                    if (checkIntervalOnMapUser(fieldWidth, fieldHeight, field, FirstCoordinates, new Position(x + (numberOfDecks - 1), y))) {
                        LastCoordinates = new Position(x + (numberOfDecks - 1), y);
                        correctNewPosition = true;
                    }
                    break;
                case 1:
                    if (checkIntervalOnMapUser(fieldWidth, fieldHeight, field, FirstCoordinates, new Position(x - (numberOfDecks - 1), y))) {
                        LastCoordinates = new Position(x - (numberOfDecks - 1), y);
                        correctNewPosition = true;
                    }
                    break;
                case 2:
                    if (checkIntervalOnMapUser(fieldWidth, fieldHeight, field, FirstCoordinates, new Position(x, y + (numberOfDecks - 1)))) {
                        LastCoordinates = new Position(x, y + (numberOfDecks - 1));
                        correctNewPosition = true;
                    }
                    break;
                case 3:
                    if (checkIntervalOnMapUser(fieldWidth, fieldHeight, field, FirstCoordinates, new Position(x, y - (numberOfDecks - 1)))) {
                        LastCoordinates = new Position(x, y - (numberOfDecks - 1));
                        correctNewPosition = true;
                    }
                    break;
            }
        }
        setShipOnField(field, ships, FirstCoordinates, LastCoordinates, numberOfDecks, numberOfShip);
    }

    public static List<Ship> generateShips(int fieldWidth, int fieldHeight, List<Integer> shipsCount) {
        char[][] field = new char[fieldHeight][fieldWidth];
        for (int i = 0; i < fieldHeight; i++) {
            for (int j = 0; j < fieldWidth; j++) {
                field[i][j] = '-';
            }
        }
        List<Ship> ships = new ArrayList<>();
        int numberOfDecks = 4;
        for (int numberShipsOfCurrType : shipsCount) {
            for (int numberOfShip = 0; numberOfShip < numberShipsOfCurrType; numberOfShip++) {
                findNewPositionForCurrentShip(fieldWidth, fieldHeight, field, ships, numberOfDecks, numberOfShip);
            }
            numberOfDecks--;
        }
        return ships;
    }

}

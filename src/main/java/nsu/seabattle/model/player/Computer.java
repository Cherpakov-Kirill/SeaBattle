package nsu.seabattle.model.player;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.ship.Position;

import java.util.ArrayList;
import java.util.List;

public class Computer {
    private boolean inFindOfShip;
    private final List<Position> destroyedCoordinatesOnLastStep;
    private final Config config;
    private String field;

    public Computer(Config config) {
        this.config = config;
        inFindOfShip = false;
        destroyedCoordinatesOnLastStep = new ArrayList<>();
    }

    public void resetFindShip(){
        inFindOfShip = false;
        destroyedCoordinatesOnLastStep.clear();
    }

    private boolean checkEnemyFieldForEmptyPosition(int x, int y) {
        if (x >= config.fieldWidth || x < 0) return false;
        if (y >= config.fieldHeight || y < 0) return false;
        return field.charAt(y * config.fieldWidth + x) == '-';
    }

    private int randomize(int interval) {
        return (int) (Math.random() * interval);
    }

    public Position getCoordinateForStep(String userField) {
        field = userField;
        int squareOfMap = config.fieldWidth * config.fieldHeight;
        boolean correctNewPosition = false;
        Position newCoordinates = null;
        if (inFindOfShip) {
            if (destroyedCoordinatesOnLastStep.size() == 1) {
                int xPos = destroyedCoordinatesOnLastStep.get(0).x;
                int yPos = destroyedCoordinatesOnLastStep.get(0).y;
                if (checkEnemyFieldForEmptyPosition(xPos - 1, yPos)) {
                    newCoordinates = new Position(xPos - 1, yPos);
                } else if (checkEnemyFieldForEmptyPosition(xPos, yPos - 1)) {
                    newCoordinates = new Position(xPos, yPos - 1);
                } else if (checkEnemyFieldForEmptyPosition(xPos + 1, yPos)) {
                    newCoordinates = new Position(xPos + 1, yPos);
                } else if (checkEnemyFieldForEmptyPosition(xPos, yPos + 1)) {
                    newCoordinates = new Position(xPos, yPos + 1);
                } else {
                    System.out.println("Something went wrong! (get coordinates in optimal player)\n");
                }
            } else {
                destroyedCoordinatesOnLastStep.sort((o1, o2) -> {
                    if (o1.x == o2.x) {
                        return Integer.compare(o1.y, o2.y);
                    } else {
                        return Integer.compare(o1.x, o2.x);
                    }
                });
                while (!correctNewPosition) {
                    Position start = destroyedCoordinatesOnLastStep.get(0);
                    Position finish = destroyedCoordinatesOnLastStep.get(destroyedCoordinatesOnLastStep.size() - 1);
                    if (start.x == finish.x) {
                        if (checkEnemyFieldForEmptyPosition(start.x, start.y - 1)) {
                            correctNewPosition = true;
                            newCoordinates = new Position(start.x, start.y - 1);
                        } else if (checkEnemyFieldForEmptyPosition(finish.x, finish.y + 1)) {
                            correctNewPosition = true;
                            newCoordinates = new Position(finish.x, finish.y + 1);
                        }
                    } else {
                        if (checkEnemyFieldForEmptyPosition(start.x - 1, start.y)) {
                            correctNewPosition = true;
                            newCoordinates = new Position(start.x - 1, start.y);
                        } else if (checkEnemyFieldForEmptyPosition(finish.x + 1, finish.y)) {
                            correctNewPosition = true;
                            newCoordinates = new Position(finish.x + 1, finish.y);
                        } else {
                            System.out.println("Something went wrong! (get coordinates in optimal player)\n");
                        }
                    }
                }
            }
        } else {
            while (!correctNewPosition) {
                int randomValue = randomize(squareOfMap);
                int x = randomValue % 10;
                int y = randomValue / 10;
                if (checkEnemyFieldForEmptyPosition(x, y)) {
                    correctNewPosition = true;
                    newCoordinates = new Position(x, y);
                }
            }
        }
        return newCoordinates;
    }

    public void setHit(Position position) {
        inFindOfShip = true;
        destroyedCoordinatesOnLastStep.add(position);
    }
}

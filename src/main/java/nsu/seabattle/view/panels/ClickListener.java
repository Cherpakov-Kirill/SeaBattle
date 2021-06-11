package nsu.seabattle.view.panels;

import nsu.seabattle.model.ship.Ship;

import java.util.List;

public interface ClickListener {
    void makeShot(int squareNum);

    void setShips(List<Ship> ships);

    void createNewGame();

    void openRules();

    void closeTheGame();
}
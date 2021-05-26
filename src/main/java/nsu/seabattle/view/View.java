package nsu.seabattle.view;

import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.presenter.Presenter;

import java.util.List;

public interface View {
    void attachPresenter(Presenter presenter);

    void newGame();

    List<Ship> getStartUserShipList();

    void startGame(String userField, String computerField, String userName, String enemyName);

    void updateGameFields(String userField, String computerField);

    void end(String winner);

}

package nsu.seabattle.presenter;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.Model;
import nsu.seabattle.model.player.Player;
import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.view.View;

import java.util.List;

public class Presenter implements FieldListener {
    private static final String COMPUTER_NAME = "Computer";
    private String userName;
    private final Config config;
    private final View view;
    private Model model;

    public Presenter(Config config, View view) {
        this.userName = "";
        this.config = config;
        this.view = view;
        this.model = null;
        this.view.attachPresenter(this);
    }

    public void launchTheStartWindow() {
        view.launchTheStartWindow();
    }

    public void createNewGame() {
        view.createNewGame();
    }

    public void startTheGame(List<Ship> ships) {
        this.model = Model.create(config, ships);
        this.model.setListener(this);
        view.startTheGame(model.getUserField(), model.getEnemyField(), userName, "Computer");
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setCoordinateOfStep(int buttonNumber) {
        model.shoot(buttonNumber);
    }

    private String getWinnerName() {
        return model.getWinner() == Player.USER ? userName : COMPUTER_NAME;
    }

    public String[] getUserFieldStatistics() {
        return model.getUserFieldStatistics();
    }

    public String[] getComputerFieldStatistics() {
        return model.getComputerFieldStatistics();
    }

    @Override
    public void updateGameField(Player winner) {
        view.updateGameFields(model.getUserField(), model.getEnemyField());
        if (winner != null) {
            view.end(getWinnerName());
        }
    }
}

package nsu.seabattle.presenter;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.Model;
import nsu.seabattle.model.player.Player;
import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.view.View;

import java.util.List;

public class Presenter implements FieldListener {
    private final Config config;
    private final View view;
    private Model model;

    public Presenter(Config config, View view) {
        this.config = config;
        this.view = view;
        this.model = null;
        this.view.attachPresenter(this);
    }

    public void launchTheGame() {
        view.visible(true);
    }

    public void startTheGame(List<Ship> ships) {
        this.model = Model.create(config, ships);
        this.model.setListener(this);
        view.startTheGame(model.getUserField(), model.getEnemyField());
    }

    public void setCoordinateOfStep(int buttonNumber) {
        model.shoot(buttonNumber);
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
            view.end(winner);
        }
    }
}

package nsu.seabattle.presenter;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.Model;
import nsu.seabattle.model.player.Player;
import nsu.seabattle.view.View;

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

    public void newGame() {
        view.newGame();
        this.model = Model.create(config, view.getStartUserShipList());
        this.model.setListener(this);
        view.startGame(model.getUserField(), model.getEnemyField(), userName, "Computer");
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCoordinateOfStep(int buttonNumber) {
        model.shoot(buttonNumber);
    }

    private String getWinnerName(){
        return model.getWinner() == Player.USER ? userName : COMPUTER_NAME;
    }

    @Override
    public void updateGameField(Player winner) {
        view.updateGameFields(model.getUserField(), model.getEnemyField());
        if(winner != null){
            view.end(getWinnerName());
        }
    }
}

package nsu.seabattle.model;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.player.Computer;
import nsu.seabattle.model.player.Player;
import nsu.seabattle.model.player.Shot;
import nsu.seabattle.model.ship.Position;
import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.presenter.FieldListener;

import java.util.List;

public class Model {
    private final Config config;
    private final Computer computer;
    private final Field userField;
    private final Field computerField;
    private FieldListener listener;
    private Player winner;

    public static Model create(Config config, List<Ship> userShips) {
        int width = config.fieldWidth;
        int height = config.fieldHeight;
        Field userField = new Field(width, height);
        if (userShips == null) userShips = ModelUtils.generateShips(config);
        userShips.forEach(userField::addShip);
        Field computerField = new Field(width, height);
        List<Ship> computerShips = ModelUtils.generateShips(config);
        computerShips.forEach(computerField::addShip);
        return new Model(config, userField, computerField);
    }

    public Model(Config config, Field userField, Field computerField) {
        this.config = config;
        computer = new Computer(config);
        winner = null;
        this.userField = userField;
        this.computerField = computerField;
    }

    public void shoot(int positionNumber) {
        if(winner != null) return;
        Position position = new Position(positionNumber % config.fieldWidth, positionNumber / config.fieldWidth);
        Shot shot = computerField.shoot(position);
        switch (shot) {
            case HIT, KILL -> {
                boolean isWinner = checkWinner(computerField);
                if (isWinner) {
                    winner = Player.USER;
                }
            }
            case MISS -> {
                makeComputerMove();
                boolean isWinner = checkWinner(userField);
                if (isWinner) {
                    winner = Player.COMPUTER;
                }
            }
            case REPEAT -> {
                return;
            }
        }
        listener.updateGameField(winner);
    }

    private void makeComputerMove() {
        Shot shot;
        do {
            Position position = computer.getCoordinateForStep(userField.getField(true));
            shot = userField.shoot(position);
            if(shot == Shot.HIT) computer.setHit(position);
            if(shot == Shot.KILL) computer.resetFindShip();
        } while (shot != Shot.MISS);
    }

    public String getEnemyField() {
        return computerField.getField(true);
    }

    public String getUserField() {
        return userField.getField(false);
    }

    public Player getWinner() {
        return winner;
    }

    private boolean checkWinner(Field field) {
        return !field.hasAliveShips();
    }

    public void setListener(FieldListener listener) {
        this.listener = listener;
    }
}

package nsu.seabattle.model;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.player.Computer;
import nsu.seabattle.model.player.Player;
import nsu.seabattle.model.ship.Position;
import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.presenter.FieldListener;

import java.util.List;

/// # - alive ship
/// @ - dead ship
/// * - injured ship
/// ~ - miss
/// - - empty

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
        if (userShips == null) userShips = ModelUtils.generateShips(width, height, config.ships);
        userShips.forEach(userField::addShip);
        Field computerField = new Field(width, height);
        List<Ship> computerShips = ModelUtils.generateShips(width, height, config.ships);
        computerShips.forEach(computerField::addShip);
        return new Model(config, userField, computerField);
    }

    public Model(Config config, Field userField, Field computerField) {
        this.config = config;
        computer = new Computer(config);
        this.userField = userField;
        this.computerField = computerField;
    }

    public void shoot(int positionNumber) {
        if (winner != null) return;
        Position position = new Position(positionNumber % config.fieldWidth, positionNumber / config.fieldWidth);
        Shot shot = computerField.shoot(position);
        switch (shot) {
            case HIT, KILL -> {
                if (checkWinner(computerField)) winner = Player.USER;
                listener.updateGameField(winner);
            }
            case MISS -> {
                listener.updateGameField(winner);
                makeComputerMove();
            }
            case REPEAT -> {
            }
        }
    }

    public String[] getUserFieldStatistics() {
        return userField.getStatistics();
    }

    public String[] getComputerFieldStatistics() {
        return computerField.getStatistics();
    }

    private void makeComputerMove() {
        Shot shot;
        do {
            //I tried to set this sleep for 1 sec.
            //When user makes a shot. Game should update field and after update it must sleep for 1 sec.
            //But view had not update! It waited 1 sec. and after that view update user's shot and all computer's shots together.

            //I need help with this problem.
            /*
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            Position position = computer.getCoordinateForStep(userField.getField(true));
            shot = userField.shoot(position);
            if (shot == Shot.HIT) computer.setHit(position);
            if (shot == Shot.KILL) computer.resetFindShip();
            if (checkWinner(userField)) winner = Player.COMPUTER;
            listener.updateGameField(winner);
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

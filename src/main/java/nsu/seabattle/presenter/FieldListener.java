package nsu.seabattle.presenter;


import nsu.seabattle.model.player.Player;

public interface FieldListener {
    void updateGameField(Player winner);
}

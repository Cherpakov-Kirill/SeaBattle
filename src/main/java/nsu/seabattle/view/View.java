package nsu.seabattle.view;

import nsu.seabattle.model.player.Player;
import nsu.seabattle.presenter.Presenter;

public interface View {
    void attachPresenter(Presenter presenter);

    void startTheGame(String userField, String computerField);

    void updateGameFields(String userField, String computerField);

    void end(Player winner);

    void visible(boolean var);
}

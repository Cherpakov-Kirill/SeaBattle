package nsu.seabattle.view;

import nsu.seabattle.presenter.Presenter;

public interface View {
    void attachPresenter(Presenter presenter);

    void createNewGame();

    void startTheGame(String userField, String computerField, String userName, String enemyName);

    void updateGameFields(String userField, String computerField);

    void end(String winner);

    void launchTheStartWindow();
}

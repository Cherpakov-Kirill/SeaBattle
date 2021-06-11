package nsu.seabattle;

import nsu.seabattle.config.Config;
import nsu.seabattle.presenter.Presenter;
import nsu.seabattle.view.windows.MainWindow;
import nsu.seabattle.view.View;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        View view = new MainWindow(config);
        Presenter presenter = new Presenter(config, view);
        presenter.launchTheGame();
    }
}

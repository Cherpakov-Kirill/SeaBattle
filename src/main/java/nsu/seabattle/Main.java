package nsu.seabattle;

import nsu.seabattle.config.Config;
import nsu.seabattle.presenter.Presenter;
import nsu.seabattle.view.Frame;
import nsu.seabattle.view.View;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        View view = new Frame(config);
        Presenter presenter = new Presenter(config, view);
        presenter.newGame();
    }
}

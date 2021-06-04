package nsu.seabattle.view.windows;

import nsu.seabattle.config.Config;
import nsu.seabattle.view.panels.WindowPanel;

import javax.swing.*;

public class Rules {
    private static final String GAME_RULES = "Game Rules";
    private final JFrame rulesFrame;
    private final WindowPanel background;

    Rules(Config config) {
        rulesFrame = new JFrame(GAME_RULES);
        rulesFrame.setResizable(false);
        rulesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rulesFrame.setSize(500, 707);
        background = new WindowPanel(config.rulesBackground);
        rulesFrame.setContentPane(background);
        rulesFrame.repaint();
        rulesFrame.setVisible(true);
    }

    public void dispose() {
        rulesFrame.dispose();
    }
}

package nsu.seabattle.view.windows;

import nsu.seabattle.view.panels.WindowPanel;

import javax.swing.*;

public class Rules implements Disposable {
    private static final String GAME_RULES = "Game Rules";
    private final JFrame rulesFrame;

    Rules() {
        rulesFrame = new JFrame(GAME_RULES);
        rulesFrame.setResizable(false);
        rulesFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        rulesFrame.setSize(500, 707);
        rulesFrame.setContentPane(new WindowPanel(System.getProperty("file.separator") + "Rules.png"));
        rulesFrame.repaint();
    }

    public void visible(boolean var) {
        rulesFrame.setVisible(var);
    }

    @Override
    public void dispose() {
        rulesFrame.dispose();
    }
}

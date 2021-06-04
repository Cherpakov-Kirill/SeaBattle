package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;

import javax.swing.*;

public class ShipsPanel extends WindowPanel {
    protected final Config config;
    protected final int fieldWidth;
    protected final int fieldHeight;
    protected ClickListener listener;

    protected ImageIcon alive;
    protected ImageIcon killed;
    protected ImageIcon injured;
    protected ImageIcon missed;
    protected ImageIcon empty;

    ShipsPanel(Config config, String backgroundFilename) {
        super(backgroundFilename);
        this.config = config;
        this.fieldWidth = config.fieldWidth;
        this.fieldHeight = config.fieldHeight;
        listener = null;
        initButtonsIcons();
    }

    private void initButtonsIcons() {
        alive = ButtonsIcons.getImageButtonIcon(config, ButtonsIcons.ALIVE);
        killed = ButtonsIcons.getImageButtonIcon(config, ButtonsIcons.DEAD);
        injured = ButtonsIcons.getImageButtonIcon(config, ButtonsIcons.INJURED);
        missed = ButtonsIcons.getImageButtonIcon(config, ButtonsIcons.MISS);
        empty = ButtonsIcons.getImageButtonIcon(config, ButtonsIcons.EMPTY);
    }

    protected void updateField(String field, JButton[][] matrix) {
        for (int y = 0; y < fieldHeight; y++) {
            for (int x = 0; x < fieldWidth; x++) {
                switch (field.charAt(y * fieldWidth + x)) {
                    case '-' -> matrix[y][x].setIcon(empty);
                    case '#' -> matrix[y][x].setIcon(alive);
                    case '@' -> matrix[y][x].setIcon(killed);
                    case '~' -> matrix[y][x].setIcon(missed);
                    case '*' -> matrix[y][x].setIcon(injured);
                }
            }
        }
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }
}

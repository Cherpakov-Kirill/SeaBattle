package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;

import javax.swing.*;
import java.awt.*;

public class StartPanel extends WindowPanel {
    // CR: not used fields
    private final Config config;
    private final int fieldWidth;
    private final int fieldHeight;
    private ClickListener listener;

    // CR: why not just local variables
    private final JButton start;
    private final JButton rules;
    private final JButton close;

    public StartPanel(Config config){
        super(config.startBackground);
        this.config = config;
        this.fieldWidth = config.fieldWidth;
        this.fieldHeight = config.fieldHeight;
        listener = null;
        this.setLayout(null);

        start = new JButton();
        start.setPreferredSize(new Dimension(530, 120));
        start.setFocusPainted(false);
        start.setContentAreaFilled(false);
        start.setBorderPainted(false);
        start.addActionListener(e -> listener.startGame());
        Dimension startSize = start.getPreferredSize();
        start.setBounds(390, 370, startSize.width, startSize.height);
        start.setOpaque(false);
        this.add(start);

        rules = new JButton();
        rules.setPreferredSize(new Dimension(375, 80));
        rules.setFocusPainted(false);
        rules.setContentAreaFilled(false);
        rules.setBorderPainted(false);
        rules.addActionListener(e -> listener.openRules());
        Dimension rulesSize = rules.getPreferredSize();
        rules.setBounds(140, 560, rulesSize.width, rulesSize.height);
        rules.setOpaque(false);
        this.add(rules);

        // CR: all buttons are initialized in pretty much the same way, let's have a static method for it
        close = new JButton();
        close.setPreferredSize(new Dimension(375, 80));
        close.setFocusPainted(false);
        close.setContentAreaFilled(false);
        close.setBorderPainted(false);
        close.addActionListener(e -> listener.closeTheGame());
        Dimension closeSize = close.getPreferredSize();
        close.setBounds(790, 560, closeSize.width, closeSize.height);
        close.setOpaque(false);
        this.add(close);
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }
}

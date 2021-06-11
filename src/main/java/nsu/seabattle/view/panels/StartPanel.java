package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartPanel extends WindowPanel {
    private static JButton initButton(int width, int height, int posX, int posY, ActionListener listener){
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(width, height));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(listener);
        Dimension startSize = button.getPreferredSize();
        button.setBounds(posX, posY, startSize.width, startSize.height);
        button.setOpaque(false);
        return button;
    }

    public StartPanel(Config config, ClickListener listener){
        super(System.getProperty("file.separator") + "Start.png");
        this.setLayout(null);
        this.add(initButton(530,120,390,370, e -> listener.createNewGame()));
        this.add(initButton(375,80,140,560, e -> listener.openRules()));
        this.add(initButton(375,80,790,560, e -> listener.closeTheGame()));
    }
}

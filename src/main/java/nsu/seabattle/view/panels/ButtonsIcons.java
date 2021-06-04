package nsu.seabattle.view.panels;

import nsu.seabattle.config.Config;
import nsu.seabattle.view.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

public enum ButtonsIcons {
    EMPTY, MISS, ALIVE, INJURED, DEAD;

    public static ImageIcon getImageButtonIcon(Config config, ButtonsIcons type) {
        URL file = MainWindow.class.getResource(config.getFileNameForButton(type));
        if (file == null) {
            BufferedImage defaultBackground = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = defaultBackground.createGraphics();
            graphics.setPaint(config.getColorForButton(type));
            graphics.fillRect(0, 0, defaultBackground.getWidth(), defaultBackground.getHeight());
            return new ImageIcon(defaultBackground);
        } else {
            return new ImageIcon(file);
        }
    }
}


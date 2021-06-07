package nsu.seabattle.config;

import nsu.seabattle.view.panels.ButtonsIcons;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {
    public final int fieldWidth;
    public final int fieldHeight;
    public List<Integer> ships;
    public final String fieldBackground;
    public final String startBackground;
    public final String rulesBackground;

    private final String[] filesForSettingShipsBackgrounds;
    private final String[] filesForButtonsIcons;
    private final Color[] colorsForButtons;

    public String getFileNameForSettingShipsBackground(int position) {
        return filesForSettingShipsBackgrounds[position];
    }

    public String getFileNameForButton(ButtonsIcons type) {
        return filesForButtonsIcons[type.ordinal()];
    }

    public Color getColorForButton(ButtonsIcons type) {
        // CR: i think it's better to store this info inside ButtonIcons enum
        return colorsForButtons[type.ordinal()];
    }

    public Config() {
        InputStream file = getClass().getResourceAsStream("/config.properties");
        if (file != null) {
            Properties props = new Properties();
            try {
                props.load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fieldBackground = props.getProperty("fieldBackground");
            startBackground = props.getProperty("startBackground");
            rulesBackground = props.getProperty("rulesFilename");
            filesForSettingShipsBackgrounds = new String[]{props.getProperty("settingOneDeckShipBackgroundFilename"), props.getProperty("settingTwoDecksShipBackgroundFilename"), props.getProperty("settingThreeDecksShipBackgroundFilename"), props.getProperty("settingFourDecksShipBackgroundFilename")};
            filesForButtonsIcons = new String[]{props.getProperty("buttonEmptyFilename"), props.getProperty("buttonMissFilename"), props.getProperty("buttonAliveFilename"), props.getProperty("buttonInjuredFilename"), props.getProperty("buttonDeadFilename")};
            ships = Arrays.asList(Integer.parseInt(props.getProperty("numberOfFourDecksShips")), Integer.parseInt(props.getProperty("numberOfThreeDecksShips")), Integer.parseInt(props.getProperty("numberOfTwoDecksShips")), Integer.parseInt(props.getProperty("numberOfOneDeckShips")));
            fieldWidth = Integer.parseInt(props.getProperty("fieldWidth"));
            fieldHeight = Integer.parseInt(props.getProperty("fieldHeight"));
        } else {
            fieldBackground = "/Field.png";
            startBackground = "/Start.png";
            rulesBackground = "/Rules.png";
            filesForSettingShipsBackgrounds = new String[]{"/1Deck.png", "/2Deck.png", "/3Deck.png", "/4Deck.png"};
            filesForButtonsIcons = new String[]{"/Empty.png", "/Miss.png", "/Alive.png", "/Injured.png", "/Dead.png"};
            ships = Arrays.asList(1, 2, 3, 4);
            fieldWidth = 10;
            fieldHeight = 10;
        }
        colorsForButtons = new Color[]{Color.WHITE, Color.GRAY, Color.GREEN, Color.PINK, Color.RED};

    }


}

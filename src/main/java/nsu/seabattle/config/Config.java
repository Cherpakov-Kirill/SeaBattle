package nsu.seabattle.config;

import nsu.seabattle.view.ButtonsIcons;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class Config {
    public final int fieldWidth;
    public final int fieldHeight;
    public List<Integer> ships;
    private final String[] filesForSettingShipsBackgrounds;
    private final String[] filesForButtonsIcons;
    private final Color[] colorsForButtons;

    public String getFileNameForSettingShipsBackground(int position) {
        return filesForSettingShipsBackgrounds[position];
    }

    public String getFileNameForButton(ButtonsIcons type) {
        return filesForButtonsIcons[type.ordinal()];
    }

    public Color gerColorForButton(ButtonsIcons type) {
        return colorsForButtons[type.ordinal()];
    }

    public Config() {
        //This is the default constructor for Config.
        //When i've made a Parser for propertiesFile, I will create new constructor for properties.
        colorsForButtons = new Color[]{Color.WHITE, Color.GRAY, Color.GREEN, Color.PINK, Color.RED};
        filesForSettingShipsBackgrounds = new String[]{"/1Deck.png", "/2Deck.png", "/3Deck.png", "/4Deck.png"};
        filesForButtonsIcons = new String[]{"/Empty.png", "/Miss.png", "/Alive.png", "/Injured.png", "/Dead.png"};
        ships = Arrays.asList(1, 2, 3, 4);
        this.fieldWidth = 10;
        this.fieldHeight = 10;
    }
}

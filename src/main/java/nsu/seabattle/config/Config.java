package nsu.seabattle.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Config {
    public final int fieldWidth;
    public final int fieldHeight;
    public List<Integer> ships;

    public Config() {
        InputStream file = getClass().getResourceAsStream(System.getProperty("file.separator") + "config.properties");
        if (file != null) {
            Properties props = new Properties();
            try {
                props.load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ships = Arrays.asList(Integer.parseInt(props.getProperty("numberOfFourDecksShips")), Integer.parseInt(props.getProperty("numberOfThreeDecksShips")), Integer.parseInt(props.getProperty("numberOfTwoDecksShips")), Integer.parseInt(props.getProperty("numberOfOneDeckShips")));
            for (int i = 0; i < 4; i++) {
                if (ships.get(i) <= 0) ships.set(i, i + 1); ///if negative or zero -> set default value
            }
            fieldWidth = Integer.parseInt(props.getProperty("fieldWidth"));
            fieldHeight = Integer.parseInt(props.getProperty("fieldHeight"));
        } else {
            ships = Arrays.asList(1, 2, 3, 4);
            fieldWidth = 10;
            fieldHeight = 10;
        }
    }
}

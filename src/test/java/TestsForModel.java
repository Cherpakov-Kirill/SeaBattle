import nsu.seabattle.config.Config;
import nsu.seabattle.model.Model;
import nsu.seabattle.model.ModelUtils;
import nsu.seabattle.model.player.Player;
import nsu.seabattle.model.ship.Ship;

import java.util.ArrayList;
import java.util.List;

import nsu.seabattle.presenter.FieldListener;
import org.junit.*;

public class TestsForModel {
    private final static Config config = new Config();
    private static List<Ship> ships = new ArrayList<>();
    private static Model model;
    private static final Listener listener = new Listener();
    private static String field;

    static class Listener implements FieldListener {
        @Override
        public void updateGameField(Player winner) {

        }
    }

    @BeforeClass
    public static void initShips() {
        System.out.println("InitShips");
        ships = ModelUtils.generateShips(config.fieldWidth, config.fieldHeight, config.ships);
    }

    @BeforeClass
    public static void createModel() {
        System.out.println("CreateModel");
        model = Model.create(config, ships);
        model.setListener(listener);
        field = model.getUserField();
    }

    @Test
    public void sizeOfShips() {
        Assert.assertEquals(10, ships.size());
    }

    @Test
    public void modelIsNotNull() {
        Assert.assertNotEquals(null, model);
    }

    @Test
    public void checkNewField() {
        int counterOfDecks = 0;
        for (int y = 0; y < config.fieldHeight; y++) {
            for (int x = 0; x < config.fieldWidth; x++) {
                char sym = field.charAt(y * config.fieldWidth + x);
                if (sym == '#') counterOfDecks++;
                else Assert.assertEquals('-', sym);
            }
        }
        Assert.assertEquals(20, counterOfDecks);
    }

    @Test
    public void checkCorrectShipPositions() {
        for (Ship ship : ships) {
            if (ship.start().x == ship.end().x) {
                int startY;
                int finishY;
                if (ship.start().y < ship.end().y) {
                    startY = ship.start().y;
                    finishY = ship.end().y;
                } else {
                    startY = ship.end().y;
                    finishY = ship.start().y;
                }
                for (int i = startY; i <= finishY; i++) {
                    Assert.assertEquals('#', field.charAt(i * config.fieldWidth + ship.start().x));
                }
            } else {
                int startX;
                int finishX;
                if (ship.start().x < ship.end().x) {
                    startX = ship.start().x;
                    finishX = ship.end().x;
                } else {
                    startX = ship.end().x;
                    finishX = ship.start().x;
                }
                for (int i = startX; i <= finishX; i++) {
                    Assert.assertEquals('#', field.charAt(ship.start().y * config.fieldWidth + i));
                }
            }
        }
    }

    @Test
    public void tryToShotInAllShips() {
        for (int y = 0; y < config.fieldHeight; y++) {
            for (int x = y; x < config.fieldWidth; x++) {
                model.shoot(y * config.fieldWidth + x);
            }
        }
        String field = model.getEnemyField();
        for (int y = 0; y < config.fieldHeight; y++) {
            for (int x = y; x < config.fieldWidth; x++) {
                char sym = field.charAt(y * config.fieldWidth + x);
                if (sym != '@' && sym != '*') Assert.assertEquals('~', sym);
            }
        }
    }

}

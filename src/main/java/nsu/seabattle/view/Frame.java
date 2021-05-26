package nsu.seabattle.view;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.ship.Position;
import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.presenter.Presenter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

public class Frame extends JFrame implements View, Panel.ClickListener {
    private static final String NAME = "Sea Battle";
    private static final String MENU = "Menu";
    private static final String NEW_GAME = "New game";
    private static final String EXIT = "Exit";
    private final Config config;
    private final List<Ship> ships;
    private final List<Position> flowOfInputCoordinates;
    private int nextCoordinateInFlow;
    private StringBuffer field;

    private Panel fieldPanel;
    private Presenter presenter;
    private boolean settingShipsByUser;

    private final JPanel startPanel;

    private void resetParameters() {
        ships.clear();
        flowOfInputCoordinates.clear();
        nextCoordinateInFlow = 0;
        settingShipsByUser = false;
        field = new StringBuffer("-".repeat(config.fieldHeight * config.fieldWidth));
    }

    public Frame(Config config) {
        super(NAME);
        startPanel = new WindowPanel("/Start.png");
        ships = new ArrayList<>();
        flowOfInputCoordinates = new ArrayList<>();
        this.config = config;
        resetParameters();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1300, 1000);
        setupMenu();
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(MENU);

        JMenuItem newGameItem = new JMenuItem(NEW_GAME);
        JMenuItem exitItem = new JMenuItem(EXIT);

        newGameItem.addActionListener(event -> presenter.newGame());
        exitItem.addActionListener(event -> System.exit(0));

        menu.add(newGameItem);
        menu.add(exitItem);
        menuBar.add(menu);

        this.setJMenuBar(menuBar);
    }

    public void createFieldPanel() {
        fieldPanel = new Panel(config);
        fieldPanel.setClickListener(this);
    }

    public void getUserName() {
        String userName;
        userName = (String) JOptionPane.showInputDialog(
                this,
                "Please, type your name",
                NAME,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                "User"
        );
        if (userName == null) {
            System.exit(0);
        }
        if (userName.isEmpty()) {
            userName = "NoName";
        }
        presenter.setUserName(userName);
    }

    private void openSettingShipsField() {
        this.setContentPane(fieldPanel.updateSettingShipsField(field.toString()));
        this.repaint();
        this.setVisible(false);
        this.setVisible(true);
    }

    public void askAboutRandom() {
        String[] OPTIONS = {"Yes", "No"};
        int INITIAL_CHOICE = 0; //Yes
        int choice = JOptionPane.showOptionDialog(
                this,
                "Would you prefer to place the ships on the field by yourself?",
                "Setting up ships",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                OPTIONS,
                OPTIONS[INITIAL_CHOICE]);
        if (choice == JOptionPane.CLOSED_OPTION) choice = INITIAL_CHOICE;
        if (choice == 0) {
            settingShipsByUser = true;
            openSettingShipsField();
        }
    }

    ///View interface
    @Override
    public void newGame() {
        this.setContentPane(startPanel);
        this.repaint();
        this.setVisible(true);

        resetParameters();
        createFieldPanel();
        getUserName();
        askAboutRandom();
    }

    @Override
    public void startGame(String userField, String computerField, String userName, String enemyName) {
        fieldPanel.setPlayerNames(userName, enemyName);
        this.setContentPane(fieldPanel.updateGameFields(userField, computerField));
        this.repaint();
        this.setVisible(true);
    }

    @Override
    public void updateGameFields(String userField, String computerField) {
        fieldPanel.updateGameFields(userField, computerField);
    }

    @Override
    public void end(String winner) {
        fieldPanel.addTheEnd(winner);
        System.out.println("THE END");
    }

    @Override
    public void attachPresenter(Presenter presenter) {
        this.presenter = presenter;
    }


    ///Set new user ships
    @Override
    public void setCoordinateOfNewShip(int buttonNumber) {
        flowOfInputCoordinates.add(new Position(buttonNumber % config.fieldWidth, buttonNumber / config.fieldWidth));
    }

    @Override
    public List<Ship> getStartUserShipList() {
        if (!settingShipsByUser) return null;
        WindowPanel settings = fieldPanel.getAllSettingShips();
        int numberOfDecks = 4;
        for (int numberShipsOfCurrType : config.ships) {
            for (int numberOfShip = 0; numberOfShip < numberShipsOfCurrType; numberOfShip++) {
                boolean checker = false;
                Position first = null;
                Position second = null;
                while (!checker) {
                    first = getNextCoordinateFromFlow();
                    if (numberOfDecks == 1) second = first;
                    else second = getNextCoordinateFromFlow();
                    int lengthX = abs(first.x - second.x) + 1;
                    int lengthY = abs(first.y - second.y) + 1;
                    if (((lengthX == 1 && lengthY == numberOfDecks) || (lengthX == numberOfDecks && lengthY == 1)) && checkIntervalOnMapUser(first, second))
                        checker = true;
                    else System.out.println("Sorry!");
                }
                ships.add(new Ship(first, second));
                if (first.x == second.x) {
                    int startY;
                    int finishY;
                    if (first.y < second.y) {
                        startY = first.y;
                        finishY = second.y;
                    } else {
                        startY = second.y;
                        finishY = first.y;
                    }
                    for (int i = startY; i <= finishY; i++) {
                        field.setCharAt(i * config.fieldWidth + first.x, '#');
                    }
                } else {
                    int startX;
                    int finishX;
                    if (first.x < second.x) {
                        startX = first.x;
                        finishX = second.x;
                    } else {
                        startX = second.x;
                        finishX = first.x;
                    }
                    for (int i = startX; i <= finishX; i++) {
                        field.setCharAt(first.y * config.fieldWidth + i, '#');
                    }
                }

                if (numberOfDecks != 1 || numberOfShip != 3) {
                    settings = fieldPanel.updateSettingShipsField(field.toString());
                }
            }
            numberOfDecks--;
            if (numberOfDecks != 0) {
                settings.setImageIcon(config.getFileNameForSettingShipsBackground(numberOfDecks - 1));
            }
        }
        return ships;
    }

    private boolean checkUserCoordinateForShip(int x, int y) {
        if (x >= config.fieldWidth || x < 0) return false;
        if (y >= config.fieldHeight || y < 0) return false;
        return field.charAt(y * config.fieldWidth + x) == '#';
    }

    private boolean checkIntervalOnMapUser(Position FirstPair, Position LastPair) {
        if (FirstPair.x >= config.fieldWidth || FirstPair.x < 0) return false;
        if (FirstPair.y >= config.fieldHeight || FirstPair.y < 0) return false;
        if (LastPair.x >= config.fieldWidth || LastPair.x < 0) return false;
        if (LastPair.y >= config.fieldHeight || LastPair.y < 0) return false;

        if (FirstPair.x == LastPair.x) {
            int xPos = FirstPair.x;
            int yPosStart;
            int yPosFinish;
            if (FirstPair.y < LastPair.y) {
                yPosStart = FirstPair.y;
                yPosFinish = LastPair.y;
            } else {
                yPosStart = LastPair.y;
                yPosFinish = FirstPair.y;
            }
            if (checkUserCoordinateForShip(xPos, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(xPos + 1, yPosStart - 1)) return false;
            if (checkUserCoordinateForShip(xPos - 1, yPosStart - 1)) return false;

            if (checkUserCoordinateForShip(xPos, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(xPos + 1, yPosFinish + 1)) return false;
            if (checkUserCoordinateForShip(xPos - 1, yPosFinish + 1)) return false;
            while (yPosStart <= yPosFinish) {
                if (checkUserCoordinateForShip(xPos, yPosStart)) return false;
                if (checkUserCoordinateForShip(xPos - 1, yPosStart)) return false;
                if (checkUserCoordinateForShip(xPos + 1, yPosStart)) return false;
                yPosStart++;
            }

        } else {
            int yPos = FirstPair.y;
            int xPosStart;
            int xPosFinish;
            if (FirstPair.x < LastPair.x) {
                xPosStart = FirstPair.x;
                xPosFinish = LastPair.x;
            } else {
                xPosStart = LastPair.x;
                xPosFinish = FirstPair.x;
            }
            if (checkUserCoordinateForShip(xPosStart - 1, yPos)) return false;
            if (checkUserCoordinateForShip(xPosStart - 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(xPosStart - 1, yPos - 1)) return false;

            if (checkUserCoordinateForShip(xPosFinish + 1, yPos)) return false;
            if (checkUserCoordinateForShip(xPosFinish + 1, yPos + 1)) return false;
            if (checkUserCoordinateForShip(xPosFinish + 1, yPos - 1)) return false;
            while (xPosStart <= xPosFinish) {
                if (checkUserCoordinateForShip(xPosStart, yPos)) return false;
                if (checkUserCoordinateForShip(xPosStart, yPos - 1)) return false;
                if (checkUserCoordinateForShip(xPosStart, yPos + 1)) return false;
                xPosStart++;
            }
        }
        return true;
    }

    protected Position getNextCoordinateFromFlow() {
        while (flowOfInputCoordinates.size() == nextCoordinateInFlow) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Position next = flowOfInputCoordinates.get(nextCoordinateInFlow);
        nextCoordinateInFlow++;
        return next;
    }

    ///ClickListener interface
    @Override
    public void makeShot(int buttonNumber) {
        presenter.setCoordinateOfStep(buttonNumber);
    }

}

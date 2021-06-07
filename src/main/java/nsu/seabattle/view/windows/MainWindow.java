package nsu.seabattle.view.windows;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.presenter.Presenter;
import nsu.seabattle.view.View;
import nsu.seabattle.view.panels.ClickListener;
import nsu.seabattle.view.panels.GamePanel;
import nsu.seabattle.view.panels.SettingShipsPanel;
import nsu.seabattle.view.panels.StartPanel;

import javax.swing.*;
import java.util.List;

public class MainWindow extends JFrame implements View, ClickListener {
    private static final String NAME = "Sea Battle";
    private static final String MENU = "Menu";
    private static final String RULES = "Open the rules";
    private static final String CLOSE_CURRENT_GAME = "Close current game";
    private static final String NEW_GAME = "New game";
    private static final String RATING = "Records table";
    private static final String EXIT = "Exit";
    private final Config config;


    private final StartPanel startPanel;
    private GamePanel gamePanel;
    private SettingShipsPanel settingShipsPanel;
    private Presenter presenter;

    private GameStatistics gameStatistics;
    private RecordsTable tableOfRecords;
    private Rules rules;

    private void resetParameters() {
        gameStatistics = null;
        tableOfRecords = null;
        rules = null;
    }

    public MainWindow(Config config) {
        super(NAME);
        startPanel = new StartPanel(config);
        // CR: just pass as constructor parameter
        startPanel.setClickListener(this);
        this.config = config;
        // CR: they are already null
        resetParameters();
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1300, 1000);
        setupMenu();
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(MENU);

        // CR: can have static method like createMenuItem(String name, ActionListener lister)
        JMenuItem newGameItem = new JMenuItem(NEW_GAME);
        JMenuItem ratingItem = new JMenuItem(RATING);
        JMenuItem exitItem = new JMenuItem(EXIT);
        JMenuItem rules = new JMenuItem(RULES);
        JMenuItem closeCurrentGame = new JMenuItem(CLOSE_CURRENT_GAME);

        newGameItem.addActionListener(event -> presenter.createNewGame());
        ratingItem.addActionListener(event -> showRecordTable());
        exitItem.addActionListener(event -> closeTheGame());
        rules.addActionListener(event -> openRules());
        closeCurrentGame.addActionListener(event -> launchTheStartWindow());

        menu.add(newGameItem);
        menu.add(rules);
        menu.add(closeCurrentGame);
        menu.add(ratingItem);
        menu.add(exitItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    private void showRecordTable() {
        closeWindow(tableOfRecords);
        tableOfRecords = new RecordsTable();
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
        if (userName.isEmpty()) {
            userName = "NoName";
        }
        presenter.setUserName(userName);
    }

    private void closeWindow(Object obj) {
        if (obj != null) {
            // CR: create interface Disposable with method dispose() and make all this classes implement it
            // CR: then you won't have to check which class is this is window
            if (obj instanceof GameStatistics) ((GameStatistics) obj).dispose();
            else if (obj instanceof RecordsTable) ((RecordsTable) obj).dispose();
            else if (obj instanceof Rules) ((Rules) obj).dispose();
        }
    }

    private void openSettingShipsPanel() {
        settingShipsPanel = new SettingShipsPanel(config);
        settingShipsPanel.setClickListener(this);
        this.setContentPane(settingShipsPanel);
        this.repaint();
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
        if (choice == 0) openSettingShipsPanel();
        else setShips(null);
    }

    ///View interface
    @Override
    public void attachPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void launchTheStartWindow() {
        closeWindow(gameStatistics);
        closeWindow(tableOfRecords);
        closeWindow(rules);

        this.setContentPane(startPanel);
        this.repaint();
        this.setVisible(true);
    }

    @Override
    public void createNewGame() {
        closeWindow(gameStatistics);
        closeWindow(tableOfRecords);
        closeWindow(rules);

        resetParameters();
        gamePanel = new GamePanel(config);
        gamePanel.setClickListener(this);
        getUserName();
        askAboutRandom();
    }

    @Override
    public void startTheGame(String userField, String computerField, String userName, String enemyName) {
        gamePanel.setPlayerNames(userName, enemyName);
        gamePanel.updateGameFields(userField, computerField);
        this.setContentPane(gamePanel);
        this.repaint();
        this.setVisible(true);
    }

    @Override
    public void updateGameFields(String userField, String computerField) {
        gamePanel.updateGameFields(userField, computerField);
    }

    @Override
    public void end(String winner) {
        closeWindow(rules);
        String[] userFieldStat = presenter.getUserFieldStatistics();
        String[] computerFieldStat = presenter.getComputerFieldStatistics();
        gameStatistics = new GameStatistics(presenter.getUserName(), winner, userFieldStat, computerFieldStat);
        tableOfRecords = new RecordsTable(winner, Integer.parseInt(computerFieldStat[0]));
        System.out.println("THE END");
    }

    ///ClickListener interface
    @Override
    public void startGame() {
        presenter.createNewGame();
    }

    @Override
    public void openRules() {
        closeWindow(rules);
        // CR: I think you may just hide it, not recreate
        rules = new Rules(config);
    }

    @Override
    public void closeTheGame() {
        System.exit(0);
    }

    @Override
    public void setShips(List<Ship> ships) {
        presenter.startTheGame(ships);
    }

    @Override
    public void makeShot(int buttonNumber) {
        presenter.setCoordinateOfStep(buttonNumber);
    }
}

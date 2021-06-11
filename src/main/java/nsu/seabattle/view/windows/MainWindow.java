package nsu.seabattle.view.windows;

import nsu.seabattle.config.Config;
import nsu.seabattle.model.player.Player;
import nsu.seabattle.model.ship.Ship;
import nsu.seabattle.presenter.Presenter;
import nsu.seabattle.view.View;
import nsu.seabattle.view.panels.ClickListener;
import nsu.seabattle.view.panels.GamePanel;
import nsu.seabattle.view.panels.SettingShipsPanel;
import nsu.seabattle.view.panels.StartPanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MainWindow extends JFrame implements View, ClickListener {
    private static final String NAME = "Sea Battle";
    private static final String MENU = "Menu";
    private static final String RULES = "Open the rules";
    private static final String CLOSE_CURRENT_GAME = "Close current game";
    private static final String NEW_GAME = "New game";
    private static final String RATING = "Records table";
    private static final String EXIT = "Exit";
    private static final String COMPUTER_NAME = "Computer";
    private String userName;
    private final Config config;

    private final StartPanel startPanel;
    private GamePanel gamePanel;
    private Presenter presenter;

    private GameStatistics gameStatistics;
    private RecordsTable tableOfRecords;
    private final Rules rules;

    public MainWindow(Config config) {
        super(NAME);
        this.config = config;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1300, 1000);
        setupMenu();
        startPanel = new StartPanel(config, this);
        rules = new Rules();
        this.setContentPane(startPanel);
        this.repaint();
        this.setResizable(false);
    }

    private JMenuItem createMenuItem(String name, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(listener);
        return item;
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu(MENU);
        menu.add(createMenuItem(NEW_GAME, e -> createNewGame()));
        menu.add(createMenuItem(RATING, e -> showRecordTable()));
        menu.add(createMenuItem(EXIT, e -> closeTheGame()));
        menu.add(createMenuItem(RULES, e -> openRules()));
        menu.add(createMenuItem(CLOSE_CURRENT_GAME, e -> enableTheStartWindow()));
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
    }

    public void visible(boolean var) {
        this.setVisible(var);
    }

    private void closeWindow(Disposable obj) {
        if (obj != null) obj.dispose();
    }

    public void enableTheStartWindow() {
        closeWindow(gameStatistics);
        closeWindow(tableOfRecords);
        rules.visible(false);

        this.setContentPane(startPanel);
        this.repaint();
        this.setVisible(true);
    }

    private void showRecordTable() {
        closeWindow(tableOfRecords);
        tableOfRecords = new RecordsTable();
    }

    public boolean getUserName() {
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
        if (userName == null) return false;
        if (userName.isEmpty()) {
            userName = "NoName";
        }
        this.userName = userName;
        return true;
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

    private void openSettingShipsPanel() {
        SettingShipsPanel settingShipsPanel = new SettingShipsPanel(config, this);
        this.setContentPane(settingShipsPanel);
        this.repaint();
        this.setVisible(true);
    }

    private void getInitialParameters() {
        if (!getUserName()) return;
        askAboutRandom();
    }

    @Override
    public void attachPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void createNewGame() {
        closeWindow(gameStatistics);
        closeWindow(tableOfRecords);
        gamePanel = new GamePanel(config, this);
        getInitialParameters(); //get name, asking about user settings of ships -> get ships
    }

    @Override
    public void startTheGame(String userField, String computerField) {
        gamePanel.setPlayerNames(userName, COMPUTER_NAME);
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
    public void end(Player winner) {
        rules.visible(false);
        String[] userFieldStat = presenter.getUserFieldStatistics();
        String[] computerFieldStat = presenter.getComputerFieldStatistics();
        String winnerName = winner == Player.USER ? userName : COMPUTER_NAME;
        gameStatistics = new GameStatistics(userName, winnerName, userFieldStat, computerFieldStat);
        tableOfRecords = new RecordsTable(winnerName, Integer.parseInt(computerFieldStat[0]));
        System.out.println("THE END");
    }

    @Override
    public void openRules() {
        rules.visible(true);
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

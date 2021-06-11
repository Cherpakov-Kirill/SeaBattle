package nsu.seabattle.view.windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GameStatistics implements Disposable{
    private static final String GAME_STAT = "Game Statistics";
    private final JFrame statFrame;

    GameStatistics(String username, String winnerName, String[] userFieldStat, String[] computerFieldStat) {
        statFrame = new JFrame(GAME_STAT);
        statFrame.setResizable(false);
        statFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statFrame.setSize(500, 160);
        Object[] columnNames = new Object[]{" ", username, "Computer"};
        Object[][] data = new Object[][]{
                {"Shots", computerFieldStat[0], userFieldStat[0]},
                {"Hits", computerFieldStat[1], userFieldStat[1]}, {"Misses", computerFieldStat[2], userFieldStat[2]},
                {"Misses", computerFieldStat[2], userFieldStat[2]},
                {"Alive Decks", userFieldStat[3], computerFieldStat[3]},
                {"IsWinner", winnerName.equals("Computer") ? "No" : "Yes", winnerName.equals("Computer") ? "Yes" : "No"}};
        DefaultTableModel tableModelOfStat = new DefaultTableModel(data,columnNames);
        JTable tableOfStatistics = new JTable(tableModelOfStat);
        statFrame.add(new JScrollPane(tableOfStatistics));
        statFrame.setVisible(true);
    }

    @Override
    public void dispose() {
        statFrame.dispose();
    }
}

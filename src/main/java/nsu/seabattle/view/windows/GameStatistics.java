package nsu.seabattle.view.windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class GameStatistics {
    private static final String GAME_STAT = "Game Statistics";
    private final JFrame statFrame;

    GameStatistics(String username, String winnerName, String[] userFieldStat, String[] computerFieldStat) {
        statFrame = new JFrame(GAME_STAT);
        statFrame.setResizable(false);
        statFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statFrame.setSize(500, 150);
        DefaultTableModel tableModelOfStat = new DefaultTableModel();
        JTable tableOfStatistics = new JTable(tableModelOfStat);
        tableModelOfStat.addColumn(" ");
        tableModelOfStat.addColumn(username);
        tableModelOfStat.addColumn("Computer");

        tableModelOfStat.insertRow(0, new Object[]{"Shots", computerFieldStat[0], userFieldStat[0]});
        tableModelOfStat.insertRow(0, new Object[]{"Hits", computerFieldStat[1], userFieldStat[1]});
        tableModelOfStat.insertRow(0, new Object[]{"Misses", computerFieldStat[2], userFieldStat[2]});
        tableModelOfStat.insertRow(0, new Object[]{"Alive Decks", userFieldStat[3], computerFieldStat[3]});
        tableModelOfStat.insertRow(0, new Object[]{"IsWinner", winnerName.equals("Computer") ? "No" : "Yes", winnerName.equals("Computer") ? "Yes" : "No"});
        statFrame.add(new JScrollPane(tableOfStatistics));
        statFrame.setVisible(true);
    }

    public void dispose() {
        statFrame.dispose();
    }
}

package nsu.seabattle.view.windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

// CR: better to make it private nested class
record PlayerRecord(String name, int shots) {
    public static int compare(PlayerRecord left, PlayerRecord right) {
        return Integer.compare(left.shots, right.shots);
    }
}

public class RecordsTable {
    private static final String RECORDS_TABLE = "Table of the records";
    private final InputStream file;
    private final List<PlayerRecord> rating;
    private String winnerName;
    private int winnerShots;
    private PlayerRecord userPlayerRecord;
    private boolean newRecord;
    private JFrame recordsFrame;

    public RecordsTable() {
        file = getClass().getResourceAsStream("/tableOfRecords.properties");
        rating = new ArrayList<>();
        recordsFrame = new JFrame(RECORDS_TABLE);
        recordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        recordsFrame.setSize(500, 400);
        // CR: i guess we should create table in else branch
        if (file != null) {
            Properties props = new Properties();
            try {
                props.load(file);
            } catch (IOException e) {
                // CR: rewrite file with empty?
                e.printStackTrace();
            }
            Set<Object> names = props.keySet();
            for (Object o : names) {
                String name = (String) o;
                PlayerRecord p = new PlayerRecord(name, Integer.parseInt(props.getProperty(name)));
                rating.add(p);
            }
            // CR: Collections.sort
            rating.sort(PlayerRecord::compare);
            recordsFrame.add(new JScrollPane(new JTable(getTableModel(rating))));
            recordsFrame.setVisible(true);
        }
    }

    public RecordsTable(String winnerName, int winnerShots) {
        this.winnerName = winnerName;
        this.winnerShots = winnerShots;
        newRecord = false;
        userPlayerRecord = null;
        file = getClass().getResourceAsStream("/tableOfRecords.properties");
        rating = new ArrayList<>();
        makeTable();
    }

    private void makeTable() {
        if (file != null) {
            Properties props = new Properties();
            try {
                props.load(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Set<Object> names = props.keySet();
            for (Object o : names) {
                String name = (String) o;
                PlayerRecord p = new PlayerRecord(name, Integer.parseInt(props.getProperty(name)));
                if (name.equals(winnerName)) {
                    if (p.shots() > winnerShots) {
                        p = new PlayerRecord(name, winnerShots);
                        props.setProperty(name, Integer.toString(winnerShots));
                        newRecord = true;
                    }
                    userPlayerRecord = p;
                }
                rating.add(p);
            }
            if (userPlayerRecord == null) {
                PlayerRecord p = new PlayerRecord(winnerName, winnerShots);
                props.setProperty(winnerName, Integer.toString(winnerShots));
                rating.add(p);
                newRecord = true;
            }
            rating.sort(PlayerRecord::compare);
            try {
                props.store(new FileWriter(Objects.requireNonNull(getClass().getResource("/tableOfRecords.properties")).getPath()), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (newRecord) recordsFrame = new JFrame(RECORDS_TABLE + " - new record!");
            else recordsFrame = new JFrame(RECORDS_TABLE);
            recordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            recordsFrame.setSize(500, 400);
            recordsFrame.add(new JScrollPane(new JTable(getTableModel(rating))));
            recordsFrame.setVisible(true);
        }
    }

    private static DefaultTableModel getTableModel(List<PlayerRecord> rating) {
        // CR: better to use non-default constructor with column names
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("#");
        tableModel.addColumn("Name");
        tableModel.addColumn("Shots");
        int position = 0;
        // CR: iterate with index
        for (PlayerRecord p : rating) {
            tableModel.insertRow(position, new Object[]{Integer.toString(position + 1), p.name(), Integer.toString(p.shots())});
            position++;
        }
        return tableModel;
    }

    public void dispose() {
        recordsFrame.dispose();
    }
}

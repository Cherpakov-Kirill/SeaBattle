package nsu.seabattle.view.windows;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class RecordsTable implements Disposable {
    private record PlayerRecord(String name, int shots) implements Comparable<PlayerRecord> {
        @Override
        public int compareTo(PlayerRecord o) {
            return Integer.compare(this.shots, o.shots);
        }
    }

    private static final String RECORDS_TABLE = "Table of the records";
    private final File file;
    private final List<PlayerRecord> rating;
    private String winnerName;
    private int winnerShots;
    private PlayerRecord userPlayerRecord;
    private boolean newRecord;
    private JFrame recordsFrame;

    public RecordsTable() {
        file = new File("src/main/resources" + System.getProperty("file.separator") + "tableOfRecords.properties");
        rating = new ArrayList<>();
        recordsFrame = new JFrame(RECORDS_TABLE);
        recordsFrame.setResizable(false);
        recordsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        recordsFrame.setSize(500, 400);
        Properties props = new Properties();
        if (file.canRead()) {
            try {
                props.load(new FileInputStream(file));
            } catch (IOException e) {
                try {
                    if (file.createNewFile()) System.out.println("File is created!");
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            Set<Object> names = props.keySet();
            for (Object o : names) {
                String name = (String) o;
                PlayerRecord p = new PlayerRecord(name, Integer.parseInt(props.getProperty(name)));
                rating.add(p);
            }
            Collections.sort(rating);
            recordsFrame.add(new JScrollPane(new JTable(getTableModel(rating))));
            recordsFrame.setVisible(true);
        } else {
            try {
                if (file.createNewFile()) System.out.println("File is created!");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        recordsFrame.add(new JScrollPane(new JTable(getTableModel(rating))));
        recordsFrame.setVisible(true);
    }

    public RecordsTable(String winnerName, int winnerShots) {
        this.winnerName = winnerName;
        this.winnerShots = winnerShots;
        newRecord = false;
        userPlayerRecord = null;
        file = new File("src/main/resources" + System.getProperty("file.separator") + "tableOfRecords.properties");
        rating = new ArrayList<>();
        makeTable();
    }

    private void makeTable() {
        if (file.canRead()) {
            try {
                if (file.createNewFile()) System.out.println("File is created!");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(file));
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
        Collections.sort(rating);
        try {
            props.store(new FileWriter(file), null);
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

    private static DefaultTableModel getTableModel(List<PlayerRecord> rating) {
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"#", "Name", "Shots"}, 0);
        for (int i = 0; i < rating.size(); i++) {
            PlayerRecord p = rating.get(i);
            tableModel.insertRow(i, new Object[]{Integer.toString(i + 1), p.name(), Integer.toString(p.shots())});
        }
        return tableModel;
    }

    @Override
    public void dispose() {
        recordsFrame.dispose();
    }
}

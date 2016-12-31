// have to see if there's a better way to do the SQL stuff: i.e. doing it all in one chunk of code instead of two.
package com.derek.gameTracker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class gameSubmitGUI {
    private JPanel panel1;

    private JTextField gameInput;
    private JTextField platformInput;
    private JButton submitButton;

    private JLabel userGameEntryExperienceField;
    private JLabel anotherUserExperienceLabel;

    private JTable gameTable;
    private JScrollPane scrollPane;
    private JComboBox platformBox;
    private JRadioButton multiplayerButton;

    public gameSubmitGUI() {
        submitButton.addActionListener(new ActionListener() { // when the submit button is pressed, do:
            @Override
            public void actionPerformed(ActionEvent e) { // gets the text from the input and sends it to the SQL database
                // the text getting stuff
                String gameTitle = gameInput.getText();
                String platform = (String)platformBox.getSelectedItem();

                try { // the SQL stuff
                    Class.forName("com.mysql.jdbc.Driver").newInstance(); // this starts a new instance of the jdbc.Driver thing for MySQL

                    String url = "jdbc:mysql://localhost:3306/gametracker"; // the "/gametracker" is the database we use
                    String user = "game";
                    String password = "thisisgame";
                    Connection con = DriverManager.getConnection(url, user, password);

                    String query = "INSERT INTO games (title, platform, hasMultiplayer) VALUES" + "(?, ?, ?)";

                    PreparedStatement s = con.prepareStatement(query);
                    s.setString(1, gameTitle); // sets the first question mark to be the gameTitle string we got before the try
                    s.setString(2, platform); // same as above, but sets for the second question mark
                    if (multiplayerButton.isSelected()) {
                        s.setInt(3, 1);
                    }
                    else {
                        s.setInt(3, 0);
                    }
                    //s.setInt(4, radioButton2.getX());
                    //s.setInt(5, radioButton3.getX());
                    s.executeUpdate(); // executes.
                } catch (Exception err) {
                    System.out.println("error: " + err.toString()); // *hopefully* this never happens but whatever.
                                                                    // have to figure out a better way to make sure this never happens
                }
                // clears the text in the input space
                gameInput.setText("");
                // refresh the table
                refreshTable();
            }
        });
    }

    // this method creates our table from the SQL database.
    public static JTable createTable() {
        String[] columnNames = {"Title", "Platform", "Main Story Complete", "100%", "Has Multiplayer"}; // self explanatory
        Object[][] data = new Object[5000][5000]; // have to pull our data from the SQL table. has lots of room, look into making this value changeable by the user?

        try {
            // connection stuff and SQL instance starting stuff
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/gametracker";
            String user = "game";
            String password = "thisisgame";
            Connection con = DriverManager.getConnection(url, user, password);
            // end that connection stuff

            String q = "SELECT * FROM games"; // our cute query
            Statement s = con.createStatement(); // the statement that takes in our query
            ResultSet r = s.executeQuery(q); // what results from our statement
            int row = 0;
            while (r.next()) { // now we go through the results we get
                Object[] o = {r.getString("title"), r.getString("platform"), r.getInt("mainStory"), r.getInt("oneHundredPercent"), r.getInt("hasMultiplayer")};
                data[row++] = o; // self explanatory
            }
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable gameTable = new JTable();
        gameTable.setModel(model);
        gameTable.setFillsViewportHeight(true);
        return gameTable;
    }

    public void refreshTable() { // yeah this is copied from createTable(), we're doing the same thing, just refreshing the existing gameTable.
        String[] columnNames = {"Title", "Platform", "Main Story Complete", "100%", "Has Multiplayer"}; // self explanatory
        Object[][] data = new Object[5000][5000]; // have to pull our data from the SQL table. has lots of room, look into making this value changeable by the user?

        try {
            // connection stuff and SQL instance starting stuff
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/gametracker";
            String user = "game";
            String password = "thisisgame";
            Connection con = DriverManager.getConnection(url, user, password);
            // end that connection stuff

            String q = "SELECT * FROM games"; // our cute query
            Statement s = con.createStatement(); // the statement that takes in our query
            ResultSet r = s.executeQuery(q); // what results from our statement
            int row = 0;
            while (r.next()) { // now we go through the results we get
                Object[] o = {r.getString("title"), r.getString("platform"), r.getInt("mainStory"), r.getInt("oneHundredPercent"), r.getInt("hasMultiplayer")};
                data[row++] = o; // self explanatory
            }
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        gameTable.setModel(model);
    }

    private void createUIComponents() {
        // the table things
        gameTable = createTable();
        scrollPane = new JScrollPane(gameTable);

        // the ComboBox things
        String[] platforms = {"PlayStation 4", "PlayStation Vita", "Xbox One", "WiiU", "3DS", "PC", "Other"};
        platformBox = new JComboBox(platforms);
    }

    public static void main(String[] args) { // stuff that intelij made for me :)
        JFrame frame = new JFrame("gameSubmitGUI");
        frame.setContentPane(new gameSubmitGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
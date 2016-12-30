// have to see if there's a better way to do the SQL stuff: i.e. doing it all in one chunk of code instead of two.
package com.derek.gameTracker;

import javax.swing.*;
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

    public gameSubmitGUI() {
        submitButton.addActionListener(new ActionListener() { // when the submit button is pressed, do:
            @Override
            public void actionPerformed(ActionEvent e) { // gets the text from the input and sends it to the SQL database
                // the text getting stuff
                String gameTitle = gameInput.getText();
                String platform = platformInput.getText();

                try { // the SQL stuff
                    Class.forName("com.mysql.jdbc.Driver").newInstance(); // this starts a new instance of the jdbc.Driver thing for MySQL

                    String url = "jdbc:mysql://localhost:3306/gametracker"; // the "/gametracker" is the database we use
                    String user = "game";
                    String password = "thisisgame";
                    Connection con = DriverManager.getConnection(url, user, password);

                    String query = "INSERT INTO games (title, platform) VALUES" + "(?, ?)";

                    PreparedStatement s = con.prepareStatement(query);
                    s.setString(1, gameTitle); // sets the first question mark to be the gameTitle string we got before the try
                    s.setString(2, platform); // same as above, but sets for the second question mark
                    s.executeUpdate(); // executes.
                } catch (Exception err) {
                    System.out.println("error: " + err.toString()); // *hopefully* this never happens but whatever.
                                                                    // have to figure out a better way to make sure this never happens
                }
                // clears the text in the input spaces
                gameInput.setText("");
                platformInput.setText("");
                createUIComponents(); // refreshes the table
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
        JTable gameTable = new JTable(data, columnNames);
        gameTable.setFillsViewportHeight(true);
        return gameTable;
    }

    public static void main(String[] args) { // stuff that intelij made for me :)
        JFrame frame = new JFrame("gameSubmitGUI");
        frame.setContentPane(new gameSubmitGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        gameTable = createTable();
        scrollPane = new JScrollPane(gameTable);
    }
}

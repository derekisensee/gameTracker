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

    private JLabel userGameEntryExperienceField; // this is basically useless :P
    private JLabel anotherUserExperienceLabel;

    public gameSubmitGUI() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // gets the text from the input and sends it to the SQL database
                String gameTitle = gameInput.getText();
                String platform = platformInput.getText();

                try {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gametracker", "game", "thisisgame");

                    String query = "INSERT INTO games (title, platform) VALUES" + "(?, ?)";

                    PreparedStatement s = con.prepareStatement(query);
                    s.setString(1, gameTitle);
                    s.setString(2, platform);
                    s.executeUpdate();
                }
                catch (Exception err) {
                    System.out.println("error: " + err.toString());
                }
                gameInput.setText("");
                platformInput.setText("");
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("gameSubmitGUI");
        frame.setContentPane(new gameSubmitGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

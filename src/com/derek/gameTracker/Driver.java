package com.derek.gameTracker;
import java.sql.*;
/**
 * Created by derek_000 on 12/26/2016.
 */
// this file used for testing.
public class Driver {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gametracker", "game", "thisisgame");

            String query = "SELECT * FROM games";

            Statement s = con.createStatement();
            ResultSet result = s.executeQuery(query);

            while (result.next()) {
                String title = result.getString("title");
                String platform = result.getString("platform");

                System.out.println("Title: " + title + "\nPlatform: " + platform);
            }

        }
        catch (Exception err) {
            System.out.println("error: " + err.toString());
        }
    }
}

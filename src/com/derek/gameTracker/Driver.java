// this file used for testing.
package com.derek.gameTracker;
import java.sql.*;
public class Driver {
    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection c = DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\gametracker.db");

            String q = "SELECT * FROM games";
            Statement s = c.createStatement();
            ResultSet r = s.executeQuery(q);
            while (r.next()) {
                System.out.println("Title: " + r.getString("title") + "\nPlatform: " + r.getString("platform"));
            }
        } catch (Exception err) {
            System.out.println("error: " + err.toString());
        }
    }
}

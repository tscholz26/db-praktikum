package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class XMLParser {
    public static void main(String[] args) {
        try{
            //Database connection
            String url = "jdbc:postgresql://localhost:5432/mediastore";
            String username = "postgres";
            String password = "guest";

            Connection con = DriverManager.getConnection(url, username, password);
            if (con != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

            // Here you can call your XML parsing methods or classes
            // ******************************************************
            // ******************************************************
            // ******************************************************
            // ******************************************************
            // ******************************************************
            // ******************************************************

            con.close();
            System.out.println("Connection closed.");


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

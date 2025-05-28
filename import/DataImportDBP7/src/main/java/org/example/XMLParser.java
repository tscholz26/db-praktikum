package org.example;

import org.example.Parser.XMLStoreParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class XMLParser {
    public static void main(String[] args) {
        try {
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

            try{
                initDB(con, "CreateTables.sql");
            } catch (Exception e) {
                System.out.println("Error during database initialization: " + e.getMessage());
                e.printStackTrace();
            }

            // Here you can call your XML parsing methods or classes
            // ******************************************************
            // ******************************************************
            //Call Store Parsed
            XMLStoreParser.main(con);
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

    public static void initDB(Connection con, String sqlFilePath) throws Exception {
        StringBuilder sqlBuilder = new StringBuilder();

        // SQL-Datei einlesen
        try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sqlBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new IOException("Fehler beim Lesen der SQL-Datei: " + e.getMessage(), e);
        }

        String sql = sqlBuilder.toString();

        // SQL ausführen
        try (Statement stmt = con.createStatement()) {
            stmt.execute(sql);
            System.out.println("Initialisierung erfolgreich");
        } catch (SQLException e) {
            throw new SQLException("Fehler beim Ausführen der SQL-Initialisierung: " + e.getMessage(), e);
        }
    }
}

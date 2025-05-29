package org.example.Parser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class insertStatements {

    protected static void insertItem(Connection con, String asin, String title, int salesrank, String picture) throws SQLException {
        String query = "INSERT INTO produkt (pnr, titel, verkaufsrang, bild) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, asin);
            statement.setString(2, title);
            statement.setInt(3, salesrank);
            statement.setString(4, picture);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            System.out.println("Item inserted successfully");
        }
    }
    protected static void insertRezension(Connection con, String produktnr, String username, String bewertung, String rezension, String helpful, String summary, String reviewdate) throws SQLException {
        String insertRezensionSql =
                "INSERT INTO rezension (produktnr, nutzername, bewertung, rezension) VALUES (?, ?, ?, ?)";
        String insertErrorDataCSV =
                "INSERT INTO ErrorDataCSV (produkt, bewertung, helpful, reviewdate, summary, content, fehlermeldung) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
        // 3) Rezension einfügen
        try {
            PreparedStatement stmtRezension = con.prepareStatement(insertRezensionSql);
            stmtRezension.setString(1, produktnr);
            stmtRezension.setString(2, username);
            stmtRezension.setInt(3, Integer.parseInt(bewertung));
            stmtRezension.setString(4, rezension);
            stmtRezension.executeUpdate();
        } catch (SQLException e) {
            PreparedStatement stmtErrorDataCSV = con.prepareStatement(insertErrorDataCSV);
            stmtErrorDataCSV.setString(1, produktnr);
            stmtErrorDataCSV.setInt(2, Integer.parseInt(bewertung));
            stmtErrorDataCSV.setInt(3, Integer.parseInt(helpful));
            stmtErrorDataCSV.setString(4, reviewdate);
            stmtErrorDataCSV.setString(5, summary);
            stmtErrorDataCSV.setString(6, rezension);
            stmtErrorDataCSV.setString(7, "Rezension insert failed for product=" + produktnr +
                    ", user=" + username + ": " + e.getMessage());
            stmtErrorDataCSV.executeUpdate();
        }
        System.out.println("Rezension inserted successfully");
    }

    protected static void insertKunde(Connection con, String username) throws SQLException {
        String insertKundeSql =
                "INSERT INTO Kunde (nutzername) VALUES (?)";
        try {
            PreparedStatement stmtKunde = con.prepareStatement(insertKundeSql);
            stmtKunde.setString(1, username);
            stmtKunde.executeUpdate();
        } catch (SQLException e) {}
        System.out.println("Kunde inserted successfully");
    }

}

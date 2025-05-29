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
    protected static void insertRezension(Connection con, String produktnr, String username, String bewertung, String rezension, String entityname) throws SQLException {
        String insertRezensionSql =
                "INSERT INTO rezension (produktnr, nutzername, bewertung, rezension) VALUES (?, ?, ?, ?)";
        String insertErrorDataCSV =
                "INSERT INTO ErrorData (entityname, fehlermeldung, fehlerattribut) " +
                        "VALUES (?, ?, ?)";
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
            stmtErrorDataCSV.setString(1, entityname);
            stmtErrorDataCSV.setString(2, e.getMessage());
            if (e.getMessage().contains("violates foreign key constraint")) {
                stmtErrorDataCSV.setString(3, "product");
            } else if (e.getMessage().contains("violates check constraint \"rezension_bewertung_check\"")) {
                stmtErrorDataCSV.setString(3, "rating");
            }
            stmtErrorDataCSV.executeUpdate();
        }
        System.out.println("Rezension inserted successfully");
    }

    protected static void insertKunde(Connection con, String username, String entityname) throws SQLException {
        String insertKundeSql =
                "INSERT INTO Kunde (nutzername) VALUES (?)";
        String insertErrorData =
                "INSERT INTO ErrorData (entityname, fehlermeldung, fehlerattribut) " +
                        "VALUES (?, ?, ?)";
        try {
            PreparedStatement stmtKunde = con.prepareStatement(insertKundeSql);
            stmtKunde.setString(1, username);
            stmtKunde.executeUpdate();
        } catch (SQLException e) {
            PreparedStatement stmtErrorData = con.prepareStatement(insertErrorData);
            stmtErrorData.setString(1, entityname);
            stmtErrorData.setString(2, e.getMessage());
            stmtErrorData.setString(3, "username");
            stmtErrorData.executeUpdate();
        }
        System.out.println("Kunde inserted successfully");
    }

}

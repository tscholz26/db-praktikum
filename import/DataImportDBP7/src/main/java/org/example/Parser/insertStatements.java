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
    protected static void insertRezension(Connection con, String asin, int Bewertung, String Rezension) throws SQLException {
        String query = "INSERT INTO rezension (pnr, bewertung, rezension) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, asin);
            pstmt.setInt(2, Bewertung);
            pstmt.setString(3, Rezension);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            System.out.println("Rezension inserted successfully");
        }

    }

}

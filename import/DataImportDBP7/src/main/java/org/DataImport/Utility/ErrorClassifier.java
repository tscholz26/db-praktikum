package org.DataImport.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ErrorClassifier {

    private final Connection con;

    // Constructor to receive the database connection
    public ErrorClassifier(Connection con) {
        this.con = con;
    }

    // Main function to process and classify error messages
    public void classifyErrors() {
        String selectQuery = "SELECT ErrorID, Fehlermeldung FROM ErrorData";
        String updateQuery = "UPDATE ErrorData SET Fehlerklasse = ? WHERE ErrorID = ?";
        int countErrors = 0;

        try (
                PreparedStatement selectStmt = con.prepareStatement(selectQuery);
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                ResultSet rs = selectStmt.executeQuery()
        ) {
            while (rs.next()) {
                int errorId = rs.getInt("ErrorID");
                String fehlermeldung = rs.getString("Fehlermeldung");
                String fehlerklasse = classify(fehlermeldung.toLowerCase());
                if (!(fehlerklasse.equals("nicht_zugeordnet"))) {
                    countErrors++;
                };

                if (fehlerklasse != null) {
                    updateStmt.setString(1, fehlerklasse);
                    updateStmt.setInt(2, errorId);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\u001B[32m[SUCCESS] Succesfully classified " + countErrors + " Errors from table ERRORDATA.\u001B[0m");
        printErrorClasses();

    }

    // Classification logic based on substrings in Fehlermeldung
    private String classify(String fehlermeldung) {

        if (fehlermeldung.contains("attributeinvalidexception") || fehlermeldung.contains("check constraint")) {
            return "Attributwert_nicht_sinnvoll";
        } else if (fehlermeldung.contains("attributeundefinedexception")) {
            return "Unerlaubter_NULL_Wert";
        } else if (fehlermeldung.contains("doppelter schl") || fehlermeldung.contains("duplicate key")) {
            return "Doppelter_Schluesselwert";
        } else if (fehlermeldung.contains("fkey")) {
            return "Fremdschluesselbedingung_verletzt";
        } else if (fehlermeldung.contains("inhaltlich")) {
            return "Inhaltlicher_Fehler";
        } else if (fehlermeldung.contains("doppelter attr")) {
            return "Doppelter_Attributwert";
        }

        return "nicht_zugeordnet";
    }

    private void printErrorClasses() {
        String query = "SELECT Fehlerklasse, COUNT(*) AS Anzahl FROM ErrorData GROUP BY Fehlerklasse ORDER BY Anzahl DESC";
        try (
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()
        ) {
            System.out.println("\u001B[34m[INFO] Fehlerklassenzusammenfassung:");
            System.out.printf("    %-36s | %s%n", "Fehlerklasse", "Vorkommen");
            System.out.println("    -------------------------------------|----------");

            while (rs.next()) {
                String fehlerklasse = rs.getString("Fehlerklasse");
                int anzahl = rs.getInt("Anzahl");
                System.out.printf("    %-36s | %d%n", fehlerklasse, anzahl);
            }

            System.out.println("\u001B[0m"); // Reset color after the block
        } catch (SQLException e) {
            System.err.println("[ERROR] Error logging error classes to console: " + e.getMessage());
        }
    }


}
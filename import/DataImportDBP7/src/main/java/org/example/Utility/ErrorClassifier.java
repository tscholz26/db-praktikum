package org.example.Utility;

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

        try (
                PreparedStatement selectStmt = con.prepareStatement(selectQuery);
                PreparedStatement updateStmt = con.prepareStatement(updateQuery);
                ResultSet rs = selectStmt.executeQuery()
        ) {
            while (rs.next()) {
                int errorId = rs.getInt("ErrorID");
                String fehlermeldung = rs.getString("Fehlermeldung");
                String fehlerklasse = classify(fehlermeldung.toLowerCase());

                if (fehlerklasse != null) {
                    updateStmt.setString(1, fehlerklasse);
                    updateStmt.setInt(2, errorId);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\u001B[32m[SUCCESS] Classified Errors from table ERRORDATA successfully.\u001B[0m");
    }

    // Classification logic based on substrings in Fehlermeldung
    private String classify(String fehlermeldung) {

        if (fehlermeldung.contains("attributeinvalidexception")) {
            return "Attributwert_nicht_sinnvoll";
        } else if (fehlermeldung.contains("attributeundefinedexception")) {
            return "Unerlaubter_NULL_Wert";
        } else if (fehlermeldung.contains("doppelter schl")) {
            return "Doppelter_Schluesselwert";
        } else if (fehlermeldung.contains("fremdschl")) {
            return "Fremdschluesselbedingung_verletzt";
        } else if (fehlermeldung.contains("inhaltlich")) {
            return "Inhaltlicher_Fehler";
        }

        return "nicht_zugeordnet";
    }
}

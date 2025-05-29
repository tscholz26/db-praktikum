package org.example.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErrorHandler {
    public static void handleError(Connection con, String entityName, String fehlerattribut, Exception exception) {
        try {
            PreparedStatement guestFeedbackStatement = con.prepareStatement(
                    "INSERT INTO ErrorData (EntityName, fehlerattribut, fehlermeldung) " +
                            "VALUES (?, ?, ?)");

            guestFeedbackStatement.setString(1, entityName);
            guestFeedbackStatement.setString(2, fehlerattribut);
            guestFeedbackStatement.setString(3, exception.getMessage());
            guestFeedbackStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
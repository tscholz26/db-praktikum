package org.example.Utility;

import org.w3c.dom.Attr;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ErrorHandler {
    public static void handleError(Connection con, String entityName, String fehlerattribut, Exception e) {

        String message = e.getMessage();
        //Dont print duplicate PK / missing FK to console to avoid overloading
        if (!(message.contains("fkey") || message.contains("pkey") || message.contains("Doppelt"))) {
            System.out.println("\u001B[33m[WARNING] Error handler detected error, details: \u001B[0m" + message );
        }

        if (e instanceof AttributeUndefinedException) {
            String entity = ((AttributeUndefinedException) e).getEntityName();
            String attribut = ((AttributeUndefinedException) e).getAttributeName();
            insertError(con,entity,attribut,message);
        } else {
            if (e instanceof AttributeInvalidException) {
                String entity = ((AttributeInvalidException) e).getEntityName();
                String attribut = ((AttributeInvalidException) e).getAttributeName();
                insertError(con,entity,attribut,message);
            } else {
                //System.out.println("HANDLED ERROR: ENTITY: " + entityName + " ATTRIBUT: " + fehlerattribut + "MESSAGE: " + message);
                insertError(con,entityName,fehlerattribut,message);
            }
        }


    }

    public static void insertError(Connection con, String entityName, String fehlerAttribut, String message){
        try {
            PreparedStatement sql = con.prepareStatement("INSERT INTO ErrorData (EntityName, fehlerattribut, fehlermeldung) " +   "VALUES (?, ?, ?)");

            sql.setString(1, entityName);
            sql.setString(2, fehlerAttribut);
            sql.setString(3, message);
            sql.executeUpdate();

        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }
}
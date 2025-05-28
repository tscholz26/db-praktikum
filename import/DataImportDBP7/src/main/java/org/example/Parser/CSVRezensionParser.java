package org.example.Parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CSVRezensionParser {

    private static final String CSV_FILE = "data/reviews.csv";

    public static void parse(Connection con) {
        String insertKundeSql =
                "INSERT INTO Kunde (nutzername) VALUES (?)";
        String insertRezensionSql =
                "INSERT INTO rezension (produktnr, nutzername, bewertung, rezension) VALUES (?, ?, ?, ?)";
        String insertErrorDataCSV =
                "INSERT INTO ErrorDataCSV (produkt, bewertung, helpful, reviewdate, summary, content, fehlermeldung) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";



        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE));
             PreparedStatement stmtKunde = con.prepareStatement(insertKundeSql);
             PreparedStatement stmtRezension = con.prepareStatement(insertRezensionSql);
             PreparedStatement stmtErrorDataCSV = con.prepareStatement(insertErrorDataCSV)) {

            reader.readNext(); // Kopfzeile überspringen
            String[] line;
            while ((line = reader.readNext()) != null) {
                String username = line[4];
                String produktnr = line[0];
                String ratingStr = line[1];
                String rezension = line[6];
                String helpful = line[2];
                String reviewdate = line[3];
                String summary = line[5];


                // 1) Kunde einfügen

                    try {
                        stmtKunde.setString(1, username);
                        stmtKunde.executeUpdate();
                    } catch (SQLException e) {}


                    // 2) Bewertung parsen und in DB
                    int bewertung;
                    try {
                        bewertung = Integer.parseInt(ratingStr);
                    } catch (NumberFormatException e) {
                        System.err.println("Ungültiges Rating '" + ratingStr + "' – Zeile übersprungen.");
                        continue;
                    }
                    // 3) Rezension einfügen
                    try {
                        stmtRezension.setString(1, produktnr);
                        stmtRezension.setString(2, username);
                        stmtRezension.setInt(3, bewertung);
                        stmtRezension.setString(4, rezension);
                        stmtRezension.executeUpdate();
                    } catch (SQLException e) {
                        stmtErrorDataCSV.setString(1, produktnr);
                        stmtErrorDataCSV.setInt(2, bewertung);
                        stmtErrorDataCSV.setInt(3, Integer.parseInt(helpful));
                        stmtErrorDataCSV.setString(4, reviewdate);
                        stmtErrorDataCSV.setString(5, summary);
                        stmtErrorDataCSV.setString(6, rezension);
                        stmtErrorDataCSV.setString(7, "Rezension insert failed for product=" + produktnr +
                                ", user=" + username + ": " + e.getMessage());
                        stmtErrorDataCSV.executeUpdate();
                    }

            }

            System.out.println("Rezension and Kunde parsed succesfull.");
        } catch (IOException | CsvValidationException | SQLException e) {
            // Nur Fehler, die vor der Schleife auftreten (z.B. Datei nicht gefunden)
            e.printStackTrace();
        }
    }
}

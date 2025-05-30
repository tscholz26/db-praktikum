package org.example.Parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

import static org.example.Parser.insertStatements.insertKunde;
import static org.example.Parser.insertStatements.insertRezension;


public class CSVRezensionParser {

    private static final String CSV_FILE = "data/reviews.csv";

    public static void parse(Connection con) {
        // 1) CSV-Datei lesen
        try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE)))
        {


            reader.readNext(); // Kopfzeile überspringen
            String[] line;
            while ((line = reader.readNext()) != null) {
                String username = line[4];
                String produktnr = line[0];
                String bewertung = line[1];
                String rezension = line[6];



                // 2) Kunde einfügen
                    if (!username.equals("guest")) {
                        try {
                            insertKunde(con, username, "Kunde");
                        } catch (Exception e) {
                            System.err.println("Kunde insert failed");
                        }
                    }


                    // 3) Rezension einfügen
                    try {
                        insertRezension(con, produktnr, username, bewertung, rezension, "Rezension");
                    } catch (Exception e) {
                        // Fehler beim Einfügen der Rezension, in ErrorDataCSV speichern
                        String errorMessage = "Rezension insert failed for product=" + produktnr +
                                ", user=" + username + ": " + e.getMessage();
                        System.err.println(errorMessage);
                    }
            }
            System.out.println("Rezension and Kunde parsed succesfull.");
        } catch (IOException | CsvValidationException e) {
            // Nur Fehler, die vor der Schleife auftreten (z.B. Datei nicht gefunden)
            e.printStackTrace();
        }
    }
}

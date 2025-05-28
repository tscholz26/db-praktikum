package org.example.Parser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CSVRezensionParser {

    private static final String CSV_FILE = "data/reviews.csv";

    public static void parse(Connection con){
        try(CSVReader reader = new CSVReader(new FileReader(CSV_FILE))) {
            reader.readNext(); // Skip header row
            String[] line;
            while ((line = reader.readNext()) != null) {
                String username = line[4];
                if(!username.equals("guest")){
                    PreparedStatement pstmtKunde = con.prepareStatement(
                            "INSERT INTO Kunde (nutzername) VALUES (?)"
                    );
                    pstmtKunde.setString(1, username);
                    pstmtKunde.executeUpdate();
                    System.out.println("Inserted Kunde successfully");
                }

                String produktnr = line[0];
                int bewertung = Integer.parseInt(line[1]);
                String rezension = line[6];

                // Insert into rezension table
                PreparedStatement pstmtRezension = con.prepareStatement(
                        "INSERT INTO rezension (produktnr, bewertung, rezension)" +
                                " VALUES (?, ?, ?)"
                );
                pstmtRezension.setString(1, produktnr);
                pstmtRezension.setInt(2, bewertung);
                pstmtRezension.setString(3, rezension);
                pstmtRezension.executeUpdate();
                System.out.println("Inserted review successfully");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading CSV file: " + e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error inserting data into database: " + e.getMessage());
        } catch (CsvValidationException e) {
            e.printStackTrace();
            System.out.println("Error validating CSV data: " + e.getMessage());
        }
    }
}
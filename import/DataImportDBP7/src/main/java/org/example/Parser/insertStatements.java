package org.example.Parser;

import org.example.Utility.AttributeInvalidException;
import org.example.Utility.AttributeUndefinedException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.management.Attribute;
import java.math.BigDecimal;
import java.sql.*;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Arrays;

import static org.example.Utility.ErrorHandler.handleError;

public class insertStatements {

    protected static void insertStore(Connection con, String name, String street, String zip)  {

        String query = "INSERT INTO filiale (name, adresse, plz) VALUES (?, ?, ?)";

        try (PreparedStatement statement = con.prepareStatement(query)) {
            //Sanity checks
            if (name.trim().length() < 3) { throw new AttributeInvalidException("Filiale","Name",name); };
            if (street.trim().length() < 3) { throw new AttributeInvalidException("Filiale","Adresse",street); };
            if (zip.trim().length() != 5) { throw new AttributeInvalidException("Filiale","PLZ",zip); };

            statement.setString(1, name);
            statement.setString(2, street);
            statement.setString(3, zip);
            statement.executeUpdate();
        } catch (Exception e) {
            handleError(con,"Filiale","UNKNOWN",e);
        }
    }

    protected static void insertItem(Connection con, String asin, String title, Integer salesrank, String picture) throws Exception {

        //FIRST: check if a product with the same ASIN is already existing in DB (might have been parsed before in another file)
        String checkQuery = "SELECT titel FROM produkt WHERE pnr = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            checkStmt.setString(1, asin);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    // Product already exists
                    String existingTitle = rs.getString("titel");
                    //HIER WIRD ABGEBROCHEN, DA PRODUKT BEREITS EXISTIERT
                    if (titlesAreSimilar(existingTitle,title)) {
                        throw new Exception("dummy exception: da produkt schon in DB ist kann parsen hier abgebrochen werden");
                    } else {
                        //nur in diesem Fall soll der fehler gehandelt werden
                        String msg = "Produkt " + title + " existiert bereits mit titel " + existingTitle;
                        handleError(con,"Produkt","PNR",new Exception(msg));
                        throw new Exception(msg);
                    }

                }
            }
        }

        //actually insert new product
        String query = "INSERT INTO produkt (pnr, titel, verkaufsrang, bild) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {

            //Sanity checks
            if (asin.trim().length() != 10) { throw new AttributeInvalidException("Produkt","PNR",asin); };
            if (salesrank != null && salesrank < 0) { throw new AttributeInvalidException("Produkt","Salesrank",String.valueOf(salesrank)); };

            statement.setString(1, asin);
            statement.setString(2, title);

            if (salesrank != null) {
                statement.setInt(3, salesrank);
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }

            if (!picture.isEmpty()) {
                statement.setString(4, picture);
            } else {
                statement.setNull(4, java.sql.Types.VARCHAR);
            }

            statement.executeUpdate();
        } catch (Exception e) {
            handleError(con,"Produkt","UNKNOWN",e);
            throw e;
        }
    }

    protected static void deleteItem(Connection con, String asin) {
        String query = "DELETE FROM produkt WHERE (pnr = ?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, asin);
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void insertBook(Connection con, String produktnr, String isbn, Integer seitenzahl, String erscheinungsdatum, String auflage, NodeList verlaege, NodeList autoren) throws Exception {
        String insertBookSql = "INSERT INTO buch (produktnr, isbn, seitenzahl, erscheinungsdatum, auflage) VALUES (?,?,?,?,?)";

        try {
            //Sanity checks
            if (!isbn.isEmpty() && isbn.length() < 8) { throw new AttributeInvalidException("Buch","ISBN",isbn); };
            if (seitenzahl != null && seitenzahl < 0) { throw new AttributeInvalidException("Buch","Seitenzahl",String.valueOf(seitenzahl)); };
            if (!erscheinungsdatum.isEmpty()) {
                LocalDate date = LocalDate.parse(erscheinungsdatum); // assuming format is yyyy-MM-dd
                if (date.isBefore(LocalDate.of(1900, 1, 1)) || date.equals(LocalDate.of(1970, 1, 1))) {
                    throw new AttributeInvalidException("Buch", "Erscheinungsdatum", erscheinungsdatum);
                }
            }


            PreparedStatement stmt = con.prepareStatement(insertBookSql);
            stmt.setString(1, produktnr);

            if (isbn.isEmpty()) {
                throw new AttributeUndefinedException("Book","ISBN");
            }
            stmt.setString(2, isbn);

            if (seitenzahl != null) {
                stmt.setInt(3, seitenzahl);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            if (!erscheinungsdatum.isEmpty()) {
                stmt.setDate(4, Date.valueOf(erscheinungsdatum));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            stmt.setString(5, auflage);
            stmt.executeUpdate();

            if (!(verlaege.getLength() > 0)) {
                throw new AttributeUndefinedException("Book","Publisher");
            } else {
                for (int i = 0; i < verlaege.getLength(); i++) {
                    try {
                        Node node = verlaege.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element verlagElement = (Element) node;

                            // Leipzig: verlag name is stored under attribute "name" of <publisher>
                            String verlagName = verlagElement.getAttribute("name");

                            // Dresden: verlag name is stored in content of container <publisher>
                            if (verlagName.trim().isEmpty()) {
                                verlagName = verlagElement.getTextContent().trim();
                            }

                            // Only insert non-empty publisher names
                            if (verlagName != null && !verlagName.isEmpty()) {
                                insertVerlagBuch(con, produktnr, verlagName);
                            }
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }

            if (!(autoren.getLength() > 0)) {
                throw new AttributeUndefinedException("Book","Author");
            } else {
                for (int i = 0; i < autoren.getLength(); i++) {
                    try {
                        Node node = autoren.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element autorElement = (Element) node;

                            // Leipzig: author name is stored under attribute "name" of <author>
                            String autorName = autorElement.getAttribute("name");

                            // Dresden: author name is stored in content of container <author>
                            if (autorName.trim().isEmpty()) {
                                autorName = autorElement.getTextContent().trim();
                            }

                            // Only insert non-empty publisher names
                            if (autorName != null && !autorName.isEmpty()) {
                                insertBuchAutor(con, produktnr, autorName);
                            }
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                }

            }

        } catch (Exception e) {
            handleError(con,"Book","UNKNOWN",e);
            throw e;
        }
    }

    protected static void insertVerlagBuch(Connection con, String produktnr, String verlag) throws Exception {
        //TODO: ADD SANITY CHECKS
        String insertVerlagBuchSql = "INSERT INTO buch_verlag (Produktnr, verlag) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertVerlagBuchSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, verlag);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void insertBuchAutor(Connection con, String produktnr, String autor) throws Exception {
        //TODO: ADD SANITY CHECKS
        String insertBuchAutorSql = "INSERT INTO Autor (Produktnr, Name) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertBuchAutorSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, autor);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void insertMusic(Connection con, String produktnr, String erscheinungsdatum, NodeList trackList, NodeList artists, NodeList labels) throws Exception {
        String insertMusicSql = "INSERT INTO musik_cd (produktnr, erscheinungsdatum) VALUES (?,?)";
        //System.out.println("Calling insertBook with parameters: " + "produktnr: " + produktnr + " erscheinungsdatum: " + erscheinungsdatum);

        try {
            //Sanity checks
            if (!erscheinungsdatum.isEmpty()) {
                LocalDate date = LocalDate.parse(erscheinungsdatum);
                // cd was first introduced in 1979
                if (date.isBefore(LocalDate.of(1978, 1, 1)) || date.equals(LocalDate.of(1970, 1, 1))) {
                    throw new AttributeInvalidException("Musik_CD", "Erscheinungsdatum", erscheinungsdatum);
                }
            }

            PreparedStatement stmt = con.prepareStatement(insertMusicSql);
            stmt.setString(1, produktnr);

            if (!erscheinungsdatum.isEmpty()) {
                stmt.setDate(2, Date.valueOf(erscheinungsdatum));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            stmt.executeUpdate();

            if (!(artists.getLength() > 0)) {
                throw new AttributeUndefinedException("Music","Artist");
            } else {
                for (int i = 0; i < artists.getLength(); i++) {
                    try {
                        Node node = artists.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element artistElement = (Element) node;

                            // Leipzig: artist name is stored under attribute "name" of <artist>
                            String artistName = artistElement.getAttribute("name");

                            // Dresden: artist name is stored in content of container <artist>
                            if (artistName.trim().isEmpty()) {
                                artistName = artistElement.getTextContent().trim();
                            }

                            // Only insert non-empty artist names
                            if (artistName != null && !artistName.isEmpty()) {
                                insertMusicArtist(con, produktnr, artistName);
                            }
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }

            if (!(labels.getLength() > 0)) {
                throw new AttributeUndefinedException("Music","label");
            } else {
                for (int i = 0; i < labels.getLength(); i++) {
                    try {
                        Node node = labels.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element labelElement = (Element) node;

                            // Leipzig: label name is stored under attribute "name" of <label>
                            String labelName = labelElement.getAttribute("name");

                            // Dresden: artist name is stored in content of container <label>
                            if (labelName.trim().isEmpty()) {
                                labelName = labelElement.getTextContent().trim();
                            }

                            // Only insert non-empty label names
                            if (labelName != null && !labelName.isEmpty()) {
                                insertMusicLabel(con, produktnr, labelName);
                            }
                        }
                    } catch (Exception e) {
                        throw e;
                    }
                }
            }

            if (trackList.getLength() > 0) {
                for (int i = 0; i < trackList.getLength(); i++) {
                    Node node = trackList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        String title = node.getTextContent().trim();
                        // Only insert non-empty tracks
                        if (title != null && !title.isEmpty()) {
                            try {
                                insertTrack(con, produktnr, title);
                            } catch (Exception e) {
                                throw e;
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            handleError(con,"Music","UNKNOWN",e);
            throw e;
        }

    }

    protected static void insertMusicArtist(Connection con, String produktnr, String artist) throws Exception {
        //TODO: ADD SANITY CHECKS
        String insertMusicArtistSql = "INSERT INTO kuenstler (Produktnr, kuenstlername) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertMusicArtistSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, artist);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void insertMusicLabel(Connection con, String produktnr, String label) throws Exception {
        //TODO: ADD SANITY CHECKS
        String insertMusicArtistSql = "INSERT INTO label (Produktnr, labelname) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertMusicArtistSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, label);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void insertTrack(Connection con, String produktnr, String songtitel) throws Exception {
        //TODO: ADD SANITY CHECKS
        String insertTrackSql = "INSERT INTO song (Produktnr, songtitel) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertTrackSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, songtitel);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void insertDVD(Connection con, String asin, String format, Integer regioncode, Integer runningtime, NodeList actors, NodeList creators, NodeList directors) throws Exception {

        String insertDVDSql = "INSERT INTO dvd (produktnr, format, regioncode, laufzeit) VALUES (?,?,?,?)";

        try {
            //Sanity checks
            if (regioncode != null && (regioncode < 0 || regioncode > 7)) { throw new AttributeInvalidException("DVD","Regioncode",String.valueOf(regioncode)); }
            if (runningtime != null && runningtime < 0) { throw new AttributeInvalidException("DVD","Laufzeit", String.valueOf(runningtime)); };

            PreparedStatement stmt = con.prepareStatement(insertDVDSql);
            stmt.setString(1, asin);
            stmt.setString(2, format);

            if (regioncode != null) {
                stmt.setInt(3, regioncode);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            if (runningtime != null) {
                stmt.setInt(4, runningtime);
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            insertPeopleFromNodeList(con, "actor", asin, actors);
            insertPeopleFromNodeList(con, "creator", asin, creators);
            insertPeopleFromNodeList(con, "director", asin, directors);


        } catch (Exception e) {
            handleError(con,"DVD","UNKNOWN",e);
            throw e;
        }
    }

    private static void insertPeopleFromNodeList(Connection con, String tableName, String produktnr, NodeList people) throws Exception {
        //TODO: ADD SANITY CHECKS
        if (people == null) return;

        for (int i = 0; i < people.getLength(); i++) {
            Node node = people.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element personElement = (Element) node;

                // Try attribute "name"
                String name = personElement.getAttribute("name");

                // Fall back to text content
                if (name.trim().isEmpty()) {
                    name = personElement.getTextContent().trim();
                }

                if (name != null && !name.isEmpty()) {
                    insertPersonWithRole(con, tableName, produktnr, name);
                }
            }
        }
    }

    protected static void insertPersonWithRole(Connection con, String tableName, String produktnr, String name) throws Exception {
        //TODO: ADD SANITY CHECKS
        String sql = "INSERT INTO " + tableName + " (produktnr, name) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, produktnr);
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void insertSimilars(Connection con, String mainAsin, String simAsin) throws Exception {
        //TODO: ADD SANITY CHECKS
        String insertSimilarsSql = "INSERT INTO produkt_aehnlichkeit (produktnr1, produktnr2) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertSimilarsSql);
            stmt.setString(1, mainAsin);
            stmt.setString(2, simAsin);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    protected static void insertAngebot(Connection con, String asin, String state, Double price, String currency, Integer shopID) throws Exception{
        //TODO: ADD SANITY CHECKS
        String insertAngebotSql = "INSERT INTO angebot (produktnr, filialeid, zustand, preis, waehrung) VALUES (?,?,?,?,?)";

        try {
            PreparedStatement stmt = con.prepareStatement(insertAngebotSql);
            stmt.setString(1, asin);
            stmt.setInt(2, shopID);
            stmt.setString(3, state);
            stmt.setBigDecimal(4, new BigDecimal(price));
            stmt.setString(5, currency);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }


    protected static void insertRezension(Connection con, String produktnr, String username, String bewertung, String rezension, String entityname) throws Exception {
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
        } catch (Exception e) {
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
    }

    protected static void insertKunde(Connection con, String username, String entityname) throws Exception {
        String insertKundeSql =
                "INSERT INTO Kunde (nutzername) VALUES (?)";
        String insertErrorData =
                "INSERT INTO ErrorData (entityname, fehlermeldung, fehlerattribut) " +
                        "VALUES (?, ?, ?)";
        try {
            PreparedStatement stmtKunde = con.prepareStatement(insertKundeSql);
            stmtKunde.setString(1, username);
            stmtKunde.executeUpdate();
        } catch (Exception e) {
            PreparedStatement stmtErrorData = con.prepareStatement(insertErrorData);
            stmtErrorData.setString(1, entityname);
            stmtErrorData.setString(2, e.getMessage());
            stmtErrorData.setString(3, "username");
            stmtErrorData.executeUpdate();
        }
    }

    protected static Integer getShopIdByName(Connection con, String shopName) throws Exception {
        String query = "SELECT FilialeID FROM Filiale WHERE Name = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, shopName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("FilialeID");
                } else {
                    return null; // Shop not found
                }
            }
        }
    }


    public static boolean titlesAreSimilar(String title1, String title2) {
        if (title1.equals(title2)) {
            return true;
        } else {
            //ähnliche Titel die sich nur in Umlauten unterscheiden sollen auch akzeptiert werden
            //dafür wird die LEVENSHTEIN-DISTANZ berechnet, wenn sie unter 5 ist werden die Titel als gleich akzeptiert
            int dist = levenshteinDistance(title1,title2);
            return (dist < 5);
        }
    }

        private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[s1.length()][s2.length()];
    }



}

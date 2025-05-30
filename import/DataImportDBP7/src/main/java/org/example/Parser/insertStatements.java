package org.example.Parser;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Connection;
import java.sql.Date;
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
        }
    }

    protected static void deleteItem(Connection con, String asin) throws SQLException {
        String query = "DELETE FROM produkt WHERE (pnr = ?)";
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, asin);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            System.out.println("Produkt mit ASIN " + asin + " wurde gelöscht.");
        }
    }

    protected static void insertBook(Connection con, String produktnr, String isbn, Integer seitenzahl, String erscheinungsdatum, String auflage, NodeList verlaege, NodeList autoren) throws SQLException {
        String insertBookSql = "INSERT INTO buch (produktnr, isbn, seitenzahl, erscheinungsdatum, auflage) VALUES (?,?,?,?,?)";
        //System.out.println("Calling insertBook with parameters: " + "produktnr: " + produktnr + " seitenzahl: " + seitenzahl + " erscheinungsdatum: " + erscheinungsdatum + " auflage: " + auflage);
        try {
            PreparedStatement stmt = con.prepareStatement(insertBookSql);
            stmt.setString(1, produktnr);

            if (isbn.isEmpty()) {
                throw new SQLException("ISBN is empty");
            }
            stmt.setString(2, isbn);

            if (seitenzahl != null) {
                stmt.setInt(3, seitenzahl);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }

            stmt.setDate(4, Date.valueOf(erscheinungsdatum));
            stmt.setString(5, auflage);
            stmt.executeUpdate();

            if (!(verlaege.getLength() > 0)) {
                throw new SQLException("No publisher found");
            } else {
                for (int i = 0; i < verlaege.getLength(); i++) {
                    try {
                        Node node = verlaege.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element verlagElement = (Element) node;

                            // Leipzig: verlag name is stored under attribute "name" of <publisher>
                            String verlagName = verlagElement.getAttribute("name");

                            // Dresden: verlag name is stored in content of container <publisher>
                            if (verlagName == null || verlagName.trim().isEmpty()) {
                                verlagName = verlagElement.getTextContent().trim();
                            }

                            // Only insert non-empty publisher names
                            if (verlagName != null && !verlagName.isEmpty()) {
                                insertVerlagBuch(con, produktnr, verlagName);
                            }
                        }
                    } catch (SQLException e) {
                        throw e;
                    }
                }
            }

            if (!(autoren.getLength() > 0)) {
                throw new SQLException("No author found");
            } else {
                for (int i = 0; i < autoren.getLength(); i++) {
                    try {
                        Node node = autoren.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element autorElement = (Element) node;

                            // Leipzig: author name is stored under attribute "name" of <author>
                            String autorName = autorElement.getAttribute("name");

                            // Dresden: author name is stored in content of container <author>
                            if (autorName == null || autorName.trim().isEmpty()) {
                                autorName = autorElement.getTextContent().trim();
                            }

                            // Only insert non-empty publisher names
                            if (autorName != null && !autorName.isEmpty()) {
                                insertBuchAutor(con, produktnr, autorName);
                            }
                        }
                    } catch (SQLException e) {
                        throw e;
                    }
                }

            }

        } catch (SQLException e) {
            throw e;
        }
    }

    protected static void insertVerlagBuch(Connection con, String produktnr, String verlag) throws SQLException {
        String insertVerlagBuchSql = "INSERT INTO buch_verlag (Produktnr, verlag) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertVerlagBuchSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, verlag);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    protected static void insertBuchAutor(Connection con, String produktnr, String autor) throws SQLException {
        String insertBuchAutorSql = "INSERT INTO Autor (Produktnr, Name) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertBuchAutorSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, autor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    protected static void insertMusic(Connection con, String produktnr, String erscheinungsdatum, NodeList tracklist, NodeList artists, NodeList labels) throws SQLException {
        String insertMusicSql = "INSERT INTO musik_cd (produktnr, erscheinungsdatum) VALUES (?,?)";
        System.out.println("Calling insertBook with parameters: " + "produktnr: " + produktnr + " erscheinungsdatum: " + erscheinungsdatum);

        try {
            PreparedStatement stmt = con.prepareStatement(insertMusicSql);
            stmt.setString(1, produktnr);
            stmt.setDate(2, Date.valueOf(erscheinungsdatum));
            stmt.executeUpdate();

            if (!(artists.getLength() > 0)) {
                throw new SQLException("No artist found");
            } else {
                for (int i = 0; i < artists.getLength(); i++) {
                    try {
                        Node node = artists.item(i);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element artistElement = (Element) node;

                            // Leipzig: artist name is stored under attribute "name" of <artist>
                            String artistName = artistElement.getAttribute("name");

                            // Dresden: artist name is stored in content of container <artist>
                            if (artistName == null || artistName.trim().isEmpty()) {
                                artistName = artistElement.getTextContent().trim();
                            }

                            // Only insert non-empty publisher names
                            if (artistName != null && !artistName.isEmpty()) {
                                insertMusicArtist(con, produktnr, artistName);
                            }
                        }
                    } catch (SQLException e) {
                        throw e;
                    }
                }
            }

        } catch (SQLException e) {
            throw e;
        }

    }

    protected static void insertMusicArtist(Connection con, String produktnr, String artist) throws SQLException {
        String insertVerlagBuchSql = "INSERT INTO kuenstler (Produktnr, kuenstlername) VALUES (?,?)";
        try {
            PreparedStatement stmt = con.prepareStatement(insertVerlagBuchSql);
            stmt.setString(1, produktnr);
            stmt.setString(2, artist);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
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
    }

}

package org.example.Parser;

import java.sql.Connection;

import org.example.Utility.AttributeUndefinedException;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class XMLStoreParser {

    public static void main(Connection con) {
        List<ShopItem> allItems = new ArrayList<>();

        //TODO: add PARSE STORES

        parseProducts(con, "data/leipzig_transformed.xml");
        parseProducts(con, "data/dresden.xml");

        /*//Items have now been parsed and stored in Array allItems, now we need to insert them into the database
        for (ShopItem item : allItems) {

            try {
                //add item to table "produkt"
                insertStatements.insertItem(con, item.getAsin(), item.getTitle(), 1, "picture");
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                //add item to table book/dvd/music
                switch (item.getPgroup()) {
                    case "Book":
                        try {
                            insertStatements.insertBook(con, item.getAsin(), item.getIsbn(), 1, item.getPublisher(), null, "auflage xxx");

                        } catch (SQLException e) {
                            e.printStackTrace();
                            //If book failed to be inserted, remove item from "produkt" table aswell
                            try {
                                insertStatements.deleteItem(con, item.getAsin());
                            } catch (SQLException e2) {
                                e2.printStackTrace();
                            }
                        }
                        break;
                    default:
                        System.out.println("unknown category: " + item.getPgroup());
                }

            }
        }*/


    }



    private static void parseProducts(Connection con, String filepath) {
        try {
            File xmlFile = new File(filepath);
            //DocumentBuilder: Klasse, mit der man XML Dateien parsen und in Dokumente umwandeln kann
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList itemList = doc.getElementsByTagName("item");

            for (int i = 0; i < itemList.getLength(); i++) {
                //Nodes aus XML werden nacheinander durchlaufen; Node = XML-Knoten (zB ein Element, Text, Kommentar)
                Node node = itemList.item(i);

                //Nur Nodes des Typs ELEMENT werden eingelesen, Textknoten wie zB Zeilenumbrüche sind irrelevant
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    //FILTER: ITEMS DIE KEINE CHILD ELEMENTS HABEN, WERDEN NICHT EINGELESEN (zB sub-items in <similars>)
                    Element itemElement = (Element) node;
                    boolean hasChild = false;
                    NodeList children = itemElement.getChildNodes();
                    for (int j = 0; j < children.getLength(); j++) {
                        Node child = children.item(j);
                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            hasChild = true;
                            break;
                        }
                    }

                    if (hasChild) { //Falls item child elements hat: parsen kann beginnen
                        try {
                            parseItem(con, (Element) node); //Einzelne Node wird zu Typ Element gecastet und einzeln geparsed, danach zur Liste hinzugefügt
                        } catch (Exception e) {
                            System.out.println("Error Parsing item: " + e.getMessage());
                            //TODO: FEHLERBEHANDLUNG mit ERROR-TABLE
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void parseItem(Connection con, Element item) throws Exception {

        String asin = item.getAttribute("asin");
        //TODO: verhindern, dass asin ein leerer String "" ist

        String titel;
        NodeList titleNodes = item.getElementsByTagName("title");
        if (titleNodes.getLength() > 0) {
            titel = titleNodes.item(0).getTextContent();
        } else {
            throw new AttributeUndefinedException("Titel");
        }

        Integer salesrank;
        String salesrankTemp = item.getAttribute("salesrank");
        if (!salesrankTemp.isEmpty()) {
            salesrank = Integer.parseInt(salesrankTemp);
        } else {
            //TODO statt Wert 0 einen richtigen NULL-Wert eintragen
            salesrank = 0;
        }

        String image;
        if (item.getElementsByTagName("details").getLength() > 0) {
            //Dresden: image is stored under details->Attribute:img
            image = item.getElementsByTagName("details").item(0).getAttributes().getNamedItem("img").getNodeValue();
        } else {
            //Leipzig: image is stored under picture
            image = item.getAttribute("picture");
        }

        try {
            insertStatements.insertItem(con, asin, titel, salesrank, image);
        } catch (SQLException e) {
            //TODO: FEHLERBEHANDLUNG mit ERROR-TABLE
            System.out.println("Error inserting Item to produkt table. Error: " + e.getMessage());
        } finally {
            try {
                String pgroup = item.getAttribute("pgroup");
                switch (pgroup) {
                    case "Book":
                        parseBook(con, item, asin);
                        break;
                    case "Music":
                        parseMusic(con, item, asin);
                        break;
                    case "DVD":
                        parseDVD(con, item, asin);
                        break;
                    default:
                        System.out.println("unknown category: " + pgroup);
                        throw new SQLException("Unknown category: " + pgroup);
                }
            } catch (SQLException e) {
                //TODO: FEHLERBEHANDLUNG mit ERROR-TABLE
                System.out.println("Error during parsing book/music/dvd, item will now be removed from produkt table. Error: " + e.getMessage());
                try {
                    insertStatements.deleteItem(con, asin);
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        }

        /*

        // price
        NodeList priceNodes = item.getElementsByTagName("price");
        if (priceNodes.getLength() > 0) {
            Element priceSpec = (Element) priceNodes.item(0);
            shopItem.setPriceRaw(priceSpec.getTextContent().trim());
            shopItem.setPriceCurrency(priceSpec.getAttribute("currency"));
            shopItem.setPriceMult(priceSpec.getAttribute("mult"));
            shopItem.setState(priceSpec.getAttribute("state"));
        }

         // dvdspec
        NodeList dvdSpecList = item.getElementsByTagName("dvdspec");
        if (dvdSpecList.getLength() > 0) {
            Element dvdSpec = (Element) dvdSpecList.item(0);
            shopItem.setDvdFormat(getTextContent(dvdSpec, "format"));
            shopItem.setRegionCode(getTextContent(dvdSpec, "regioncode"));
            shopItem.setDvdRelease(getTextContent(dvdSpec, "releasedate"));
            shopItem.setRunningTime(getTextContent(dvdSpec, "runningtime"));
        }

         // publisher
        NodeList pubList = item.getElementsByTagName("publisher");
        if (pubList.getLength() > 0) {
            Element pub = (Element) pubList.item(0);
            shopItem.setPublisher(pub.getAttribute("name"));
        }*/


    }

    private static void parseBook(Connection con, Element item, String asin) throws Exception {
        NodeList bookspecAttributes = item.getElementsByTagName("bookspec");
        Element bookspecElement = (Element) bookspecAttributes.item(0);

        String isbn = getAttr(bookspecElement, "isbn", "val");
        if (isbn == null) {
            throw new AttributeUndefinedException("ISBN");
        }

        String seitenzahlTemp = getTextContent(bookspecElement, "pages");
        Integer seitenzahl = null;  //default wert für seitenzahl
        if (seitenzahlTemp != null && !seitenzahlTemp.trim().isEmpty()) {
            seitenzahl = Integer.parseInt(seitenzahlTemp.trim());
        }


        String erscheinungsdatum = getAttr(bookspecElement, "publication", "date");

        String auflage = getAttr(bookspecElement, "edition", "val");

        NodeList verlaege = item.getElementsByTagName("publisher");

        NodeList autoren = item.getElementsByTagName("author");

        try {
            insertStatements.insertBook(con, asin, isbn, seitenzahl, erscheinungsdatum, auflage, verlaege, autoren);
        } catch (SQLException e) {
            throw e;
        }
    }



    private static void parseMusic(Connection con, Element item, String asin) throws Exception {
        NodeList musicspecAttributes = item.getElementsByTagName("musicspec");
        Element musicspecElement = (Element) musicspecAttributes.item(0);

        String erscheinungsdatum = getTextContent(musicspecElement, "releasedate");

        Element tracklistElement = (Element) item.getElementsByTagName("tracks").item(0);
        NodeList tracklist = null;
        if (tracklistElement != null) {
            tracklist = tracklistElement.getElementsByTagName("title");
        }

        //TODO: eventuell artist daten aufräumen (Va, &, Al löschen)
        NodeList artists = item.getElementsByTagName("artist");
        // Check if <artist> tags exist and are not empty
        boolean hasValidArtists = false;
        for (int i = 0; i < artists.getLength(); i++) {
            String name = artists.item(i).getTextContent().trim();
            if (!name.isEmpty()) {
                hasValidArtists = true;
            }
        }
        // If item has no valid <artist> element, try <creator> instead
        if (!hasValidArtists) {
            artists = item.getElementsByTagName("creator");
        }

        NodeList labels = item.getElementsByTagName("label");

        try {
            insertStatements.insertMusic(con, asin, erscheinungsdatum, tracklist, artists, labels);
        } catch (SQLException e) {
            throw e;
        }

    }

    private static void parseDVD(Connection con, Element item, String asin) throws Exception {}


    // Utility function to get inner text
    private static String getTextContent(Element parent, String tag) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        }
        return null;
    }

    // Utility function to get an attribute from a nested element
    private static String getAttr(Element parent, String tag, String attr) {
        NodeList nodes = parent.getElementsByTagName(tag);
        if (nodes.getLength() > 0) {
            Element el = (Element) nodes.item(0);
            return el.getAttribute(attr);
        }
        return null;
    }


}

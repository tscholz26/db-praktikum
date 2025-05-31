package org.example.Parser;

import java.sql.Connection;

import org.example.Utility.AttributeInvalidException;
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
import java.util.Arrays;
import java.util.List;

import static org.example.Utility.ErrorHandler.handleError;


public class XMLStoreParser {

    public static void main(Connection con) {

        parseStores(con, "data/dresden.xml");
        parseStores(con, "data/leipzig_transformed.xml");

        parseProducts(con, "data/dresden.xml");
        parseProducts(con, "data/leipzig_transformed.xml");

        parseSimilars(con, "data/dresden.xml");
        parseSimilars(con, "data/leipzig_transformed.xml");

        parseAngebot(con, "data/dresden.xml");
        parseAngebot(con, "data/leipzig_transformed.xml");



    }

    private static void parseStores(Connection con, String filepath) {
        try {
            File xmlFile = new File(filepath);
            //DocumentBuilder: Klasse, mit der man XML Dateien parsen und in Dokumente umwandeln kann
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList itemList = doc.getElementsByTagName("shop");

            for (int i = 0; i < itemList.getLength(); i++) {
                //Nodes aus XML werden nacheinander durchlaufen; Node = XML-Knoten (zB ein Element, Text, Kommentar)
                Node node = itemList.item(i);
                String name = node.getAttributes().getNamedItem("name").getNodeValue();
                String street = node.getAttributes().getNamedItem("street").getNodeValue();
                String zip = node.getAttributes().getNamedItem("zip").getNodeValue();

                //Nur Nodes des Typs ELEMENT werden eingelesen, Textknoten wie zB Zeilenumbrüche sind irrelevant
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    insertStatements.insertStore(con, name, street, zip);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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
                            handleError(con, "Produkt", "?", e);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\u001B[32m[SUCCESS] Parsed products from " + filepath + " successfully.\u001B[0m");

    }

    private static void parseItem(Connection con, Element item) throws AttributeUndefinedException {

        String asin = item.getAttribute("asin");
        if (asin.isEmpty()) {
            throw new AttributeUndefinedException("Produkt","PNR");
        }

        String titel;
        NodeList titleNodes = item.getElementsByTagName("title");
        if (titleNodes.getLength() > 0) {
            titel = titleNodes.item(0).getTextContent();
        } else {
            throw new AttributeUndefinedException("Produkt","Titel");
        }

        Integer salesrank = null;   //Default-Wert
        String salesrankTemp = item.getAttribute("salesrank");
        if (!salesrankTemp.isEmpty()) {
            salesrank = Integer.parseInt(salesrankTemp);
        }

        String image;
        if (item.getElementsByTagName("details").getLength() > 0) {
            //Dresden: image is stored under details->Attribute:img
            image = item.getElementsByTagName("details").item(0).getAttributes().getNamedItem("img").getNodeValue();
        } else {
            //Leipzig: image is stored under picture
            image = item.getAttribute("picture");
        }

        boolean addingSuccessful = false;
        try {
            insertStatements.insertItem(con, asin, titel, salesrank, image);
            addingSuccessful = true;
        } catch (Exception e) {
            //No Need to proceed: item could not be added, error handling has already been done in insertStatements
        }

        if (addingSuccessful){
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
                        //Error muss manuell gehandelt werden, da im catch block kein error handling passiert da das normalerweise in insertStatements.java passiert
                        handleError(con, "Produkt","pgroup", new AttributeInvalidException("Produkt","pgroup",pgroup));
                        throw new AttributeInvalidException("Produkt","pgroup",pgroup);
                }
            } catch (Exception e) {
                //Fehlerbehandlung mit ERROR-Tabelle wurde bereits in insertItem() durchgeführt, exception wird nur gefangen um sicherzustellen dass referenziertes Produkt gelöscht wird
                insertStatements.deleteItem(con, asin);
            }
        }

    }

    private static void parseBook(Connection con, Element item, String asin) throws Exception {
        NodeList bookspecAttributes = item.getElementsByTagName("bookspec");
        Element bookspecElement = (Element) bookspecAttributes.item(0);

        String isbn = getAttr(bookspecElement, "isbn", "val");
        if (isbn == null) {
            throw new AttributeUndefinedException("Book","ISBN");
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
        } catch (Exception e) {
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
            Node node = artists.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element artistElement = (Element) node;

                // Leipzig: <artist name="NAME HERE" />
                String name = artistElement.getAttribute("name").trim();

                // Dresden: <artist> NAME HERE </artist>
                if (name.isEmpty()) {
                    name = artistElement.getTextContent().trim();
                }

                if (!name.isEmpty()) {
                    hasValidArtists = true;
                    break; // No need to check further if a valid artist is found
                }
            }
        }

        // If item has no valid <artist> element, try <creator> instead
        if (!hasValidArtists) {
            artists = item.getElementsByTagName("creator");
        }


        NodeList labels = item.getElementsByTagName("label");

        try {
            insertStatements.insertMusic(con, asin, erscheinungsdatum, tracklist, artists, labels);
        } catch (Exception e) {
            throw e;
        }

    }

    private static void parseDVD(Connection con, Element item, String asin) throws Exception {
        NodeList dvdspecAttributes = item.getElementsByTagName("dvdspec");
        Element dvdspecElement = (Element) dvdspecAttributes.item(0);

        String format = getTextContent(dvdspecElement, "format");


        String regioncodeTemp = getTextContent(dvdspecElement, "regioncode");
        Integer regioncode = null;  //default wert für regioncode
        if (regioncodeTemp != null && !regioncodeTemp.trim().isEmpty()) {
            regioncode = Integer.parseInt(regioncodeTemp.trim());
        }

        String runningtimeTemp = getTextContent(dvdspecElement, "runningtime");
        Integer runningtime = null;  //default wert für runningtime
        if (runningtimeTemp != null && !runningtimeTemp.trim().isEmpty()) {
            runningtime = Integer.parseInt(runningtimeTemp.trim());
        }

        NodeList actors = item.getElementsByTagName("actor");
        NodeList creators = item.getElementsByTagName("creator");
        NodeList directors = item.getElementsByTagName("director");

        try {
            insertStatements.insertDVD(con, asin, format, regioncode, runningtime, actors, creators, directors);
        } catch (Exception e) {
            throw e;
        }
    }

    private static void parseSimilars(Connection con, String filepath) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filepath);
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Node itemNode = items.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    // Holt die ASIN des Hauptartikels
                    String mainAsin = itemElement.getAttribute("asin");
                    if (mainAsin == null || mainAsin.isEmpty()) continue;

                    NodeList similars = itemElement.getElementsByTagName("similars");
                    if (similars.getLength() == 0) continue;

                    Element similarsElement = (Element) similars.item(0);

                    // dresden.xml: <similars><item asin="...">Title</item></similars>
                    NodeList similarItems = similarsElement.getElementsByTagName("item");
                    for (int j = 0; j < similarItems.getLength(); j++) {
                        Element simItem = (Element) similarItems.item(j);
                        String simAsin = simItem.getAttribute("asin");
                        String simTitle = simItem.getTextContent().trim();

                        if (!simAsin.isEmpty()) {
                            insertStatements.insertSimilars(con, mainAsin, simAsin, simTitle);
                        }
                    }

                    // leipzig.xml: <similars><sim_product><asin>...</asin><title>...</title></sim_product></similars>
                    NodeList simProducts = similarsElement.getElementsByTagName("sim_product");
                    for (int j = 0; j < simProducts.getLength(); j++) {
                        Element simProduct = (Element) simProducts.item(j);
                        String simAsin = null;
                        String simTitle = null;

                        NodeList asinNodes = simProduct.getElementsByTagName("asin");
                        if (asinNodes.getLength() > 0) {
                            simAsin = asinNodes.item(0).getTextContent().trim();
                        }

                        NodeList titleNodes = simProduct.getElementsByTagName("title");
                        if (titleNodes.getLength() > 0) {
                            simTitle = titleNodes.item(0).getTextContent().trim();
                        }

                        if (simAsin != null && !simAsin.isEmpty()) {
                            insertStatements.insertSimilars(con, mainAsin, simAsin, simTitle);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\u001B[32m[SUCCESS] Parsed similar items from " + filepath + " successfully.\u001B[0m");
    }


    public static void parseAngebot(Connection con, String filepath) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(filepath);
            doc.getDocumentElement().normalize();

            NodeList shops = doc.getElementsByTagName("shop");

            for (int i = 0; i < shops.getLength(); i++) {
                Element shop = (Element) shops.item(i);
                String shopName = shop.getAttribute("name");

                //get shop ID from Filiale Table in DB
                Integer shopId = insertStatements.getShopIdByName(con, shopName);
                if (shopId == null) {
                    throw new AttributeInvalidException("Angebot","Name des Shops (existiert nicht)",shopName);
                }

                NodeList items = shop.getElementsByTagName("item");

                for (int j = 0; j < items.getLength(); j++) {
                    Element item = (Element) items.item(j);
                    String asin = item.getAttribute("asin");

                    //make sure items can have several <price> nodes
                    NodeList priceNodes = item.getElementsByTagName("price");

                    for (int k = 0; k < priceNodes.getLength(); k++) {

                        Element price = (Element) priceNodes.item(k);

                        String state = price.getAttribute("state");
                        String multText = price.getAttribute("mult");
                        String currency = price.getAttribute("currency");

                        //List of currencies is incomplete for now
                        String[] possibleCurrencies = {"EUR", "USD", "GBP", "CHF", "JPY", "CNY", "AUD"};
                        if (!Arrays.asList(possibleCurrencies).contains(currency) && !currency.isEmpty()) {
                            handleError(con,"Angebot", "Waehrung",new AttributeInvalidException("Angebot","Waehrung",currency));
                            continue;
                        }

                        String priceText = price.getTextContent().trim();

                        try {
                            if (!priceText.isEmpty()) {
                                //nur wenn preis nicht leer ist, existiert ein angebot und der datensatz muss in der DB gespeichert werden
                                Double priceValue = Double.parseDouble(priceText) * Double.parseDouble(multText);
                                insertStatements.insertAngebot(con, asin, state, priceValue, currency, shopId);
                            }

                        } catch (Exception e) {
                            if (e.getMessage().contains("angebot_produktnr_fkey")) {
                                handleError(con, "Angebot", "Produktnummer", new Exception("Fremdschluesselbedingung verletzt, Angebot konnte nicht hinzugefügt werden, Produkt mit Produktnr. " + asin + " existiert nicht in der Datenbank"));
                            } else {
                                handleError(con, "Angebot", "UNKNOWN", e);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\u001B[32m[SUCCESS] Parsed available products and prices from " + filepath + " successfully.\u001B[0m");
    }



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

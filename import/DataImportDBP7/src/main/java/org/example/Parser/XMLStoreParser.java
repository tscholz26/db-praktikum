package org.example.Parser;

import java.sql.Connection;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class XMLStoreParser {

    public static void main(Connection con) {
        List<ShopItem> allItems = new ArrayList<>();
        allItems.addAll(parseFile("data/leipzig_transformed.xml"));
        allItems.addAll(parseFile("data/dresden.xml"));

        //for (ShopItem item : allItems) {  System.out.println(item);  }

        //Items have now been parsed and stored in Array allItems, now we need to insert them into the database
        for (ShopItem item : allItems) {
            try {
                insertItem(con, item.getAsin(), item.getTitle(), 1, "picture");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    private static void insertItem(Connection con, String asin, String title, int salesrank, String picture) throws SQLException {
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

    private static List<ShopItem> parseFile(String filepath) {
        List<ShopItem> items = new ArrayList<>();

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
                    //Einzelne Node wird zu Typ Element gecastet und einzeln geparsed, danach zur Liste hinzugefügt
                    ShopItem parsedItem = parseItem((Element) node);
                    if (parsedItem != null) {
                        items.add(parsedItem);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    private static ShopItem parseItem(Element itemElement) {
        ShopItem item = new ShopItem();

        String pgroup = itemElement.getAttribute("pgroup");

        //Achtung: <item> tritt auch verschachtelt in <item> auf, zB in <similars>, deswegen werden hier nur die uebernommen die eine pggroup haben weil
        //das die eigentlichen items sind und keine nested items innerhalb der richtigen items
        if (pgroup.isEmpty()) {
            return null;    //nested item gefunden
        } else {

            item.setPgroup(pgroup);
            item.setAsin(itemElement.getAttribute("asin"));

            // title
            NodeList titleNodes = itemElement.getElementsByTagName("title");
            if (titleNodes.getLength() > 0) {
                item.setTitle(titleNodes.item(0).getTextContent());
            }

            // price
            NodeList priceNodes = itemElement.getElementsByTagName("price");
            if (priceNodes.getLength() > 0) {
                Element priceSpec = (Element) priceNodes.item(0);
                item.setPriceRaw(priceSpec.getTextContent().trim());
                item.setPriceCurrency(priceSpec.getAttribute("currency"));
                item.setPriceMult(priceSpec.getAttribute("mult"));
                item.setState(priceSpec.getAttribute("state"));
            }

            // bookspec
            NodeList bookSpecList = itemElement.getElementsByTagName("bookspec");
            if (bookSpecList.getLength() > 0) {
                Element bookSpec = (Element) bookSpecList.item(0);
                item.setBinding(getTextContent(bookSpec, "binding"));
                item.setIsbn(getAttr(bookSpec, "isbn", "val"));
                item.setPages(getTextContent(bookSpec, "pages"));
                item.setPubDate(getAttr(bookSpec, "publication", "date"));
            }

            // dvdspec
            NodeList dvdSpecList = itemElement.getElementsByTagName("dvdspec");
            if (dvdSpecList.getLength() > 0) {
                Element dvdSpec = (Element) dvdSpecList.item(0);
                item.setDvdFormat(getTextContent(dvdSpec, "format"));
                item.setRegionCode(getTextContent(dvdSpec, "regioncode"));
                item.setDvdRelease(getTextContent(dvdSpec, "releasedate"));
                item.setRunningTime(getTextContent(dvdSpec, "runningtime"));
            }

            // musicspec
            NodeList musicSpecList = itemElement.getElementsByTagName("musicspec");
            if (musicSpecList.getLength() > 0) {
                Element musicSpec = (Element) musicSpecList.item(0);
                item.setMusicBinding(getTextContent(musicSpec, "binding"));
                item.setMusicFormat(getAttr(musicSpec, "format", "value"));
                item.setMusicRelease(getTextContent(musicSpec, "releasedate"));
                item.setUpc(getTextContent(musicSpec, "upc"));
            }

            // publisher
            NodeList pubList = itemElement.getElementsByTagName("publisher");
            if (pubList.getLength() > 0) {
                Element pub = (Element) pubList.item(0);
                item.setPublisher(pub.getAttribute("name"));
            }

            return (item);
        }

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

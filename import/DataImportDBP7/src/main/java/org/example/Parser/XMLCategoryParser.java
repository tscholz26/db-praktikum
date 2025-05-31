package org.example.Parser;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.example.Utility.ErrorHandler.handleError;

public class XMLCategoryParser {

    private static final Map<String, Integer> categoryNameToIdMap = new HashMap<>();

    public static void main(Connection con, String filepath) {

        try {
            File xmlFile = new File(filepath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList rootCategories = doc.getElementsByTagName("categories");

            for (int i = 0; i < rootCategories.getLength(); i++) {
                Node rootNode = rootCategories.item(i);
                if (rootNode.getNodeType() == Node.ELEMENT_NODE) {
                    NodeList childNodes = rootNode.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node node = childNodes.item(j);
                        if (node.getNodeType() == Node.ELEMENT_NODE && node.getNodeName().equals("category")) {
                            traverseCategory(con, node, null); // Start recursion with no parent
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\u001B[32m[SUCCESS] Parsed categories from " + filepath + " successfully.\u001B[0m");
    }


    private static void traverseCategory(Connection con, Node categoryNode, String parentCategory) {
        String currentCategory = extractCategoryName(categoryNode);

        try {
            createCategory(con, currentCategory);
        } catch (Exception e) {
            System.out.println("Error creating category: " + e.getMessage());
        }

        if (parentCategory != null) {
            try {
                addSubcategory(con, parentCategory, currentCategory);
            } catch (Exception e) {
                System.out.println("Error adding subcategory: " + e.getMessage());
            }
        }

        NodeList children = categoryNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child.getNodeName().equals("item")) {
                    String item = child.getTextContent().trim();
                    try {
                        addItemToCategory(con, item, currentCategory);
                    } catch (Exception e) {
                        //error wurde bereits in SQL-Methode gehandelt
                    }
                } else if (child.getNodeName().equals("category")) {
                    traverseCategory(con, child, currentCategory);
                }
            }
        }
    }

    private static String extractCategoryName(Node categoryNode) {
        NodeList children = categoryNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                String text = child.getTextContent().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return "UNKNOWN_CATEGORY";
    }

    private static void createCategory(Connection con, String name) throws Exception{
        if (categoryNameToIdMap.containsKey(name)) return;

        String sql = "INSERT INTO Kategorie (Name) VALUES (?) RETURNING KategorieID";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt(1);
                categoryNameToIdMap.put(name, id);
            }
        } catch (Exception e) {
            handleError(con,"Kategorie","Name",e);
            throw e;
        }
    }

    private static void addSubcategory(Connection con, String parentName, String childName) throws Exception {
        Integer parentId = categoryNameToIdMap.get(parentName);
        Integer childId = categoryNameToIdMap.get(childName);

        if (parentId == null || childId == null) {
            System.err.println("Missing category IDs for: " + parentName + " or " + childName);
            return;
        }

        String sql = "UPDATE Kategorie SET OberkategorieID = ? WHERE KategorieID = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, parentId);
            stmt.setInt(2, childId);
            stmt.executeUpdate();
        } catch (Exception e) {
            handleError(con,"Kategorie","OberkategorieID",e);
            throw e;
        }
    }

    private static void addItemToCategory(Connection con, String item, String categoryName) throws Exception {
        Integer categoryId = categoryNameToIdMap.get(categoryName);

        if (categoryId == null) {
            throw new Exception("Category ID not found for categoryname: " + categoryName);
        }

        String sql = "INSERT INTO Produkt_Kategorie (ProduktNr, KategorieID) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, item);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
        } catch (Exception e) {
            // Optional: Ignore duplicate insert (if ProduktNr/KategorieID already exists)
            if (!(e.getMessage().contains("pkey") || e.getMessage().contains("doppelter Schl"))) {
                if (e.getMessage().contains("fkey")) {
                    handleError(con, "Produkt_Kategorie", "ProduktNr", e);
                } else {
                    handleError(con, "Produkt_Kategorie", "UNKNOWN", e);
                }
            }
        }
    }

}

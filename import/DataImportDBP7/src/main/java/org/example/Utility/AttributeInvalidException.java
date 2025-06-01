package org.example.Utility;

public class AttributeInvalidException extends Exception {

    private final String entityName;
    private final String attributeName;
    private final String attributeValue;

    public AttributeInvalidException(String entityName, String attributeName, String attributeValue) {
        this.entityName = entityName;
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }

    @Override
    public String getMessage() {
        return "AttributeInvalidException: Attributwert fuer Attribut \"" + attributeName + "\" des Entity-Typs \"" + entityName + "\" ist  \"" + attributeValue + "\"";
    }

    public String getEntityName() {
        return entityName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}

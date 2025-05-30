package org.example.Utility;

public class AttributeUndefinedException extends Exception {

    private final String entityName;
    private final String attributeName;

    public AttributeUndefinedException(String entityName, String attributeName) {
        this.entityName = entityName;
        this.attributeName = attributeName;
    }

    @Override
    public String getMessage() {
        return "AttributeUndefinedException: Attribute \"" + attributeName + "\" for entity of type \"" + entityName + "\" is NULL";
    }

    public String getEntityName() {
        return entityName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}

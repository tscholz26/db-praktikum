package org.example.Utility;

public class AttributeUndefinedException extends Exception {
    private final String attributeName;
    public AttributeUndefinedException(String attributeName) {
        this.attributeName = attributeName;
    }

    @Override
    public String getMessage() {
        return attributeName + " is Empty";
    }
}

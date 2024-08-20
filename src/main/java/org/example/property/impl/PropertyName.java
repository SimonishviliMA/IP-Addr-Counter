package org.example.property.impl;

/**
 * Contains config names which are being used in the app
 */
public enum PropertyName {
    FILE_NAME("file.name", true),
    QUANTITY_OF_THREADS("threads.quantity", true);

    private final String propertyPath;
    private final boolean mandatory;

    PropertyName(String propertyPath, boolean mandatory) {
        this.propertyPath = propertyPath;
        this.mandatory = mandatory;
    }

    /**
     * @return Property path
     */
    public String getPropertyPath() {
        return propertyPath;
    }

    /**
     * @return True if that property is mandatory otherwise false
     */
    public boolean isMandatory() {
        return mandatory;
    }
}

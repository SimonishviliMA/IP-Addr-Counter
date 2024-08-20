package org.example.property.impl;

/**
 * Contains config names which are being used in the app
 */
public enum PropertyName {
    FILE_NAME("file.name", true),
    QUANTITY_OF_THREADS("threads.quantity", true);

    private final String propertyKey;
    private final boolean mandatory;

    PropertyName(String propertyKey, boolean mandatory) {
        this.propertyKey = propertyKey;
        this.mandatory = mandatory;
    }

    /**
     * @return Property key
     */
    public String getPropertyKey() {
        return propertyKey;
    }

    /**
     * @return True if that property is mandatory otherwise false
     */
    public boolean isMandatory() {
        return mandatory;
    }
}

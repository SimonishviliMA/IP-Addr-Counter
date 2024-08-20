package org.example.property.impl;

public enum PropertyName {
    FILE_NAME("file.name", true),
    QUANTITY_OF_THREADS("threads.quantity", true);

    private final String name;
    private final boolean mandatory;

    PropertyName(String name, boolean mandatory) {
        this.name = name;
        this.mandatory = mandatory;
    }

    public String getName() {
        return name;
    }

    public boolean isMandatory() {
        return mandatory;
    }
}

package org.example.property.impl;

public enum PropertyName {
    FILE_NAME("file.name"),
    QUANTITY_OF_THREADS("threads.quantity");

    private final String name;

    PropertyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

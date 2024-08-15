package org.example.property.impl;

public enum PropertyName {
    FILE_NAME("file.name");

    private final String name;

    PropertyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

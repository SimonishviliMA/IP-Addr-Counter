package org.example.property.impl;

public enum PropertyName {
    FILE_NAME("file.name"),
    QUEUE_MAX_CAPACITY("queue.max-capacity"),
    QUEUE_DELTA("queue.delta");

    private final String name;

    PropertyName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

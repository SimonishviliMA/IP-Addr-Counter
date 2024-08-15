package org.example.queue;

public interface TransportBlockingQueue {

    void put(long elem) throws InterruptedException;

    long take() throws InterruptedException;

    boolean isNotEmpty();

    void finish();
}

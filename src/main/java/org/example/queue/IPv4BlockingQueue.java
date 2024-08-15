package org.example.queue;

public interface IPv4BlockingQueue {

    void put(long ip) throws InterruptedException;

    long take() throws InterruptedException;

    boolean isEmpty();

    void finish();
}

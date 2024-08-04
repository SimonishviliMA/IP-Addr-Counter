package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransportBlockingQueue {

    private static TransportBlockingQueue instance = null;

    private final BlockingQueue<Long> queue = new LinkedBlockingQueue<>();


    public static synchronized TransportBlockingQueue getInstance() {
        if (instance == null) {
            return instance = new TransportBlockingQueue();
        }
        return instance;
    }

    public void put(Long elem) throws InterruptedException {
        queue.put(elem);
    }

    public Long take() throws InterruptedException {
        return queue.take();
    }

    public boolean isNotEmpty() {
        return !queue.isEmpty();
    }
}
package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TransportBlockingQueue {

    private static final int MAX_BUFFER_SIZE = 30;
    private static TransportBlockingQueue instance = null;

    private final BlockingQueue<Long> queue = new ArrayBlockingQueue<>(MAX_BUFFER_SIZE);


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
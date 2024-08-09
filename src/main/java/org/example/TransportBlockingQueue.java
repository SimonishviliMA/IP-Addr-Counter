package org.example;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransportBlockingQueue {

    private static TransportBlockingQueue instance = null;

    private final IPv4BlockingQueue queue = new IPv4BlockingQueue(
            (int) (4_294_967_296L / 5),
            500_000
    );


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
        boolean empty = queue.isEmpty();
        if (empty) {
            System.out.println("queue is empty. Time : " + LocalDateTime.now());
        }
        return !empty;
    }

    public void finish() {
        queue.finish();
        System.out.println("Process finished. Time : " + LocalDateTime.now());
    }
}
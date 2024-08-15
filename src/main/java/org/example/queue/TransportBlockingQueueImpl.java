package org.example.queue;

import java.time.LocalDateTime;

public class TransportBlockingQueueImpl implements TransportBlockingQueue {

    private static TransportBlockingQueueImpl instance = null;

    //TODO убрать хардкодинг значений
    private final IPv4BlockingQueue queue = new IPv4BlockingQueue(
            (int) (4_294_967_296L / 5),
            500_000
    );


    public static synchronized TransportBlockingQueueImpl getInstance() {
        if (instance == null) {
            return instance = new TransportBlockingQueueImpl();
        }
        return instance;
    }

    /**
     * @param elem - element which will be putting to the queue
     */
    @Override
    public void put(long elem) throws InterruptedException {
        queue.put(elem);
    }

    /**
     * @return element from the queue
     */
    @Override
    public long take() throws InterruptedException {
        return queue.take();
    }

    /**
     * @return if true queue is not empty else false
     */
    @Override
    public boolean isNotEmpty() {
        boolean empty = queue.isEmpty();
        if (empty) {
            System.out.println("queue is empty. Time : " + LocalDateTime.now());
        }
        return !empty;
    }

    /**
     * Finishing process
     */
    @Override
    public void finish() {
        queue.finish();
        System.out.println("Process finished. Time : " + LocalDateTime.now());
    }
}
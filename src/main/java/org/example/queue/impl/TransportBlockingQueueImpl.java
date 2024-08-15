package org.example.queue.impl;

import org.example.SingletonObjectFactory;
import org.example.property.impl.PropertyName;
import org.example.queue.TransportBlockingQueue;

public class TransportBlockingQueueImpl implements TransportBlockingQueue {

    private static TransportBlockingQueueImpl instance = null;

    private final IPv4BlockingQueue queue = new IPv4BlockingQueue(
            Integer.parseInt(SingletonObjectFactory.getInstanceOfAppProperty().getProperty(PropertyName.QUEUE_MAX_CAPACITY)),
            Integer.parseInt(SingletonObjectFactory.getInstanceOfAppProperty().getProperty(PropertyName.QUEUE_DELTA))
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
        return !queue.isEmpty();
    }

    /**
     * Finishing process
     */
    @Override
    public void finish() {
        queue.finish();
    }
}
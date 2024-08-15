package org.example;

import org.example.queue.TransportBlockingQueue;
import org.example.queue.TransportBlockingQueueImpl;

public abstract class ObjectFactory {

    public static TransportBlockingQueue createTransportBlockingQueue() {
        return TransportBlockingQueueImpl.getInstance();
    }
}

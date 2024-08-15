package org.example;

import org.example.property.AppProperty;
import org.example.property.impl.AppPropertyImpl;
import org.example.queue.TransportBlockingQueue;
import org.example.queue.impl.TransportBlockingQueueImpl;

public abstract class ObjectFactory {

    public static AppProperty createAppProperty() {
        return AppPropertyImpl.getInstance();
    }

    public static TransportBlockingQueue createTransportBlockingQueue() {
        return TransportBlockingQueueImpl.getInstance();
    }
}

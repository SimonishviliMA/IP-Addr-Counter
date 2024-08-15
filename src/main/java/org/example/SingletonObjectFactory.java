package org.example;

import org.example.property.AppProperty;
import org.example.property.impl.AppPropertyImpl;
import org.example.queue.TransportBlockingQueue;
import org.example.queue.impl.TransportBlockingQueueImpl;

public abstract class SingletonObjectFactory {

    public static AppProperty getInstanceOfAppProperty() {
        return AppPropertyImpl.getInstance();
    }

    public static TransportBlockingQueue getInstanceOfTransportBlockingQueue() {
        return TransportBlockingQueueImpl.getInstance();
    }
}

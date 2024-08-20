package org.example.factory;

import org.example.property.AppProperty;
import org.example.property.impl.AppPropertyImpl;
import org.example.service.IPv4AddrContainer;
import org.example.service.impl.IPv4AddrContainerImpl;

/**
 * Creates instances of singleton classes
 */
public abstract class SingletonObjectFactory {

    /**
     * @return Instance of {@link AppProperty}
     */
    public static AppProperty getInstanceOfAppProperty() {
        return AppPropertyImpl.getInstance();
    }

    /**
     * @return Instance of {@link IPv4AddrContainer}
     */
    public static IPv4AddrContainer getInstanceOfIPv4AddrContainer() {
        return IPv4AddrContainerImpl.getInstance();
    }

}

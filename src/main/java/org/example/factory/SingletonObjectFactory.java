package org.example.factory;

import org.example.property.AppProperty;
import org.example.property.impl.AppPropertyImpl;
import org.example.service.IPv4AddrContainer;
import org.example.service.impl.IPv4AddrContainerImpl;

public abstract class SingletonObjectFactory {

    public static AppProperty getInstanceOfAppProperty() {
        return AppPropertyImpl.getInstance();
    }

    public static IPv4AddrContainer getInstanceOfIPv4AddrContainer() {
        return IPv4AddrContainerImpl.getInstance();
    }

}

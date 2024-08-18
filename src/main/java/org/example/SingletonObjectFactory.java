package org.example;

import org.example.property.AppProperty;
import org.example.property.impl.AppPropertyImpl;

public abstract class SingletonObjectFactory {

    public static AppProperty getInstanceOfAppProperty() {
        return AppPropertyImpl.getInstance();
    }

}

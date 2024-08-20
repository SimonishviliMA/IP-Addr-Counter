package org.example.property;

import org.example.property.impl.PropertyName;

public interface AppProperty {

    /**
     * @param propertyName Name of the property which is contained in {@link PropertyName}
     * @return Value of that property is in config.properties file
     */
    String getProperty(PropertyName propertyName);
}

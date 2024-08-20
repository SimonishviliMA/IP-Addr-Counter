package org.example.property.impl;

import org.example.Main;
import org.example.property.AppProperty;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

public class AppPropertyImpl implements AppProperty {

    private static AppPropertyImpl instance = null;

    private final Properties properties;

    private AppPropertyImpl() {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("Sorry, unable to find config.properties");
            }

            Properties properties = new Properties();

            properties.load(input);

            this.properties = properties;

            checkMandatoryProperties();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return instance of AppProperty
     */
    public synchronized static AppPropertyImpl getInstance() {
        if (instance == null) {
            return instance = new AppPropertyImpl();
        }
        return instance;
    }

    /**
     * @param propertyName - name of property which contains in {@link PropertyName}
     * @return value of that property in config.properties file
     */
    public String getProperty(PropertyName propertyName) {
        return properties.getProperty(propertyName.getName());
    }

    private void checkMandatoryProperties() {
        Arrays.stream(PropertyName.values())
                .filter(PropertyName::isMandatory)
                .forEach(propertyName ->
                    Optional.ofNullable(getProperty(propertyName))
                            .orElseThrow(() -> new RuntimeException("Property " + propertyName.getName() + " is mandatory. Please check your config file"))
                );
    }
}

package org.example.property;

import org.example.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperty {

    private static AppProperty instance = null;

    private final Properties properties;

    private AppProperty() {
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {

            //TODO переписать, слишком спизжена и добавить ошибку
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                throw new RuntimeException();
            }

            Properties properties = new Properties();

            properties.load(input);

            this.properties = properties;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return instance of AppProperty
     */
    public static AppProperty getInstance() {
        if (instance == null) {
            return instance = new AppProperty();
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
}

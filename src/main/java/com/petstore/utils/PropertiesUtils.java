package com.petstore.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

public class PropertiesUtils {

    public Optional<Properties> getPropValues() {
        Properties properties = new Properties();
        String propFileName = "dbconnection.properties";
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        if (Objects.nonNull(inputStream)) {
            try {
                properties.load(inputStream);
                return Optional.of(properties);
            } catch (IOException e) {
                return Optional.empty();
            }
        }else{
            return Optional.empty();
        }
    }
}

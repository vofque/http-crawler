package org.vfq.httpcrawler.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final String FILE_NAME = "config.xml";

    private final Properties properties;

    public Config() {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(FILE_NAME);
            Properties properties = new Properties();
            properties.loadFromXML(is);
            this.properties = properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}

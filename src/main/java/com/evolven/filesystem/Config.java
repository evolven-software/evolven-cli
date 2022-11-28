package com.evolven.filesystem;

import java.io.*;
import java.util.Properties;

public class Config {

    File config;

    public Config(File config) {
        this.config = config;
    }

    private Properties getProperties() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fi = new FileInputStream(config)) {
            properties.load(fi);
        }
        return properties;
    }

    public String get(String property) throws IOException {
        return getProperties().getProperty(property);
    }

    public void remove(String property) throws IOException {
        set(property, null);
    }

    public void set(String property, String value) throws IOException {
        Properties properties = getProperties();
        if (value == null) {
            properties.remove(property);
        } else {
            properties.setProperty(property, value);
        }
        try (FileOutputStream fo = new FileOutputStream(config)) {
            properties.store(fo, "Properties");
        }
    }
}

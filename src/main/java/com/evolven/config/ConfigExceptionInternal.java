package com.evolven.config;

public class ConfigExceptionInternal extends ConfigException {

    public ConfigExceptionInternal(String msg) {
        super("Internal Config Exception (bug). " +  msg);
    }
}

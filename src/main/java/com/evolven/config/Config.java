package com.evolven.config;

public interface Config {


    public String get(String key);
    public void set(String key, String value);
    public String get(String environment, String key);
    public void set(String environment, String key, String value);

}

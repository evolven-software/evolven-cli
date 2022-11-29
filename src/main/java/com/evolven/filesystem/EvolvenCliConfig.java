package com.evolven.filesystem;

import java.io.IOException;

public class EvolvenCliConfig {
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String API_KEY_KEY = "api-key";
    private static final String API_KEY_TIMEOUT_KEY = "api-key-timeout";
    private FileSystemManager fileSystemManager;
    private Config config;

    EvolvenCliConfig(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
        if (fileSystemManager.configFile != null) {
            config = new Config(fileSystemManager.configFile);
        }
    }

    public void setApiKey(String apiKey) {
        set(API_KEY_KEY, apiKey);
    }

    public String getApiKey() {
        return get(API_KEY_KEY);
    }

    public void setApiKeyTimeout(int timeout) {
        setApiKeyTimeout(Integer.toString(timeout));
    }

    public void setApiKeyTimeout(String  timeout) {
        set(API_KEY_TIMEOUT_KEY, timeout);
    }

    public String getApiKeyTimeout() {
        return get(API_KEY_TIMEOUT_KEY);
    }

    private void set(String key, String value) {
        if (config == null) return;
        try {
            config.set(key, value);
        } catch (IOException e) {
            //loger
        }
    }

    private String get(String key) {
        if (config == null) return null;
        try {
            return config.get(key);
        } catch (IOException e) {
            //loger
        }
        return null;
    }

    public void setUsername(String username) {
        set(USERNAME_KEY, username);
    }

    public String getUsername() {
        return get(USERNAME_KEY);
    }

    public void setHost(String s) {
        
    }

    public void setPort(String s) {
    }

    public void setUrl(String s) {
    }

    public String getBaseUrl() {
        return null;
    }

    public String getHost() {
        return null;
    }

    public Integer getPort() {
        return null;
    }
}

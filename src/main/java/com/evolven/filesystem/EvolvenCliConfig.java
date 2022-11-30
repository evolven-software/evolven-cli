package com.evolven.filesystem;

import com.evolven.common.StringUtils;

import java.io.IOException;

public class EvolvenCliConfig {
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String SCHEMA_KEY = "schema";
    private static final String BASE_URL_KEY = "base-url";

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
        if (StringUtils.isNullOrBlank(key)) return;
        if (StringUtils.isNullOrBlank(value)) return;
        try {
            config.set(key, value);
        } catch (IOException e) {
            //loger
        }
    }

    private String get(String key) {
        if (config == null) return null;
        if (StringUtils.isNullOrBlank(key)) return null;
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

    public void setHost(String host) {
        set(HOST_KEY, host);
    }

    public void setPort(String port) {
        set(PORT_KEY, port);
    }

    public void setUrl(String url) {
        set(BASE_URL_KEY, url);
    }

    public String getBaseUrl() {
        return get(BASE_URL_KEY);
    }

    public String getHost() {
        return get(HOST_KEY);
    }

    public String getPort() {
        String port = get(PORT_KEY);
        if (port == null) return null;
        return port;
    }

    public String getPassword() {
        return get(PASSWORD_KEY);
    }

    public void setPassword(String password) {
        set(PASSWORD_KEY, password);
    }

}

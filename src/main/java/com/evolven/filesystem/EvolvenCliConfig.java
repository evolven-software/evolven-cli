package com.evolven.filesystem;

import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
import com.evolven.config.ConfigExceptionInternal;
import com.evolven.config.YAMLFileConfig;

import java.io.File;
import java.io.IOException;

public class EvolvenCliConfig {
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private static final String HOST_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String SCHEMA_KEY = "schema";
    private static final String BASE_URL_KEY = "base-url";

    private static final String API_KEY_KEY = "api-key";
    private static final String ENVIRONMENT_KEY = "active-env";
    File yamlFile;

    YAMLFileConfig config;

    String environment;
    public EvolvenCliConfig(File yamlFile) {
        this.yamlFile = yamlFile;
    }


    public String getActiveEnvironment() throws ConfigException {
        return getInternal(null, ENVIRONMENT_KEY);
    }

    public void setActiveEnvironment(String activeEnvironment) throws ConfigException {
        setInternal(null, ENVIRONMENT_KEY, activeEnvironment);
    }

    public void setCurrentAndCachedEnvironment(String activeEnvironment) throws ConfigException {
        setInternal(null, ENVIRONMENT_KEY, activeEnvironment);
        setEnvironment(activeEnvironment);
    }

    public void setEnvironment(String environment) {
        System.out.println("setting env: " + environment);
        this.environment = environment;
    }

    public void setEnvironment() throws ConfigException {
        System.out.println("setEnvironment");
        String activeEnvironment = getActiveEnvironment();
        if (activeEnvironment == null) {
            throw new ConfigException("Active Environment is not set (need to login?).");
        }
        setEnvironment(activeEnvironment);
    }

    public void unsetEnvironment() {
        environment = null;
    }

    public void open() throws ConfigException {
        if (config != null) return;
        config = new YAMLFileConfig(yamlFile);
        config.open();
    }

    public void close() {
        if (config != null) {
            try {
                config.close();
            } catch (IOException e) {
                //TODO log
                throw new RuntimeException(e);
            }
        }
        config = null;
    }

    public void setApiKey(String apiKey) throws ConfigException {
        if (StringUtils.isNullOrBlank(environment)) {
            throw new ConfigException("Active environment is not set.");
        };
        set(environment, API_KEY_KEY, apiKey);
    }

    public void setApiKey(String environment,String apiKey) throws ConfigException {
        set(environment, API_KEY_KEY, apiKey);
    }


    public void throwIfNoEnvironment() throws ConfigExceptionInternal {
        System.out.println("env: " + environment);
        if (StringUtils.isNullOrBlank(environment)) {
            throw new ConfigExceptionInternal("Environment is not set");
        }

    }

    public String getApiKey() throws ConfigException {
        throwIfNoEnvironment();
        return get(environment, API_KEY_KEY);
    }

    public String getApiKey(String environment) throws ConfigException {
        return get(environment, API_KEY_KEY);
    }

    private void set(String key, String value) throws ConfigException {
        set(null, key, value);
    }

    private void setInternal(String namespace, String key, String value) throws ConfigException {
        boolean initiallyOpened = config != null;
        if (!initiallyOpened) open();
        if (StringUtils.isNullOrBlank(namespace)) {
            config.set(key, value);
        } else {
            config.set(namespace, key, value);
        }
        if (!initiallyOpened) close();
    }

    private void set(String namespace, String key, String value) throws ConfigException {
        if (StringUtils.isNullOrBlank(key)) return;
        if (StringUtils.isNullOrBlank(value)) return;
        if (StringUtils.isNullOrBlank(namespace)) return;
        setInternal(namespace, key, value);
    }

    private String get(String namespace, String key) throws ConfigException {
        if (StringUtils.isNullOrBlank(key)) return null;
        if (StringUtils.isNullOrBlank(namespace)) return null;
        return getInternal(namespace, key);
    }

    private String getInternal(String namespace, String key) throws ConfigException {
        boolean initiallyOpened = config != null;
        if (!initiallyOpened) open();
        String value = null;
        if (StringUtils.isNullOrBlank(namespace)) {
            value = config.get(key);
        } else {
            value = config.get(namespace, key);
        }
        if (!initiallyOpened) close();
        return value;
    }

    private String get(String key) throws ConfigException {
        if (StringUtils.isNullOrBlank(key)) return null;
        return getInternal(null, key);
    }

    public void setUsername(String environment, String username) throws ConfigException {
        set(environment, USERNAME_KEY, username);
    }

    public void setUsername(String username) throws ConfigException {
        throwIfNoEnvironment();
        set(environment, USERNAME_KEY, username);
    }

    public String getUsername() throws ConfigException {
        throwIfNoEnvironment();
        return get(environment, USERNAME_KEY);
    }

    public void setHost(String host) throws ConfigException {
        throwIfNoEnvironment();
        set(environment, HOST_KEY, host);
    }

    public void setPort(String port) throws ConfigException {
        throwIfNoEnvironment();
        set(environment, PORT_KEY, port);
    }

    public void setUrl(String url) throws ConfigException {
        throwIfNoEnvironment();
        set(environment, BASE_URL_KEY, url);
    }

    public String getBaseUrl() throws ConfigException {
        throwIfNoEnvironment();
        return get(environment, BASE_URL_KEY);
    }

    public String getHost() throws ConfigException {
        throwIfNoEnvironment();
        return get(environment, HOST_KEY);
    }

    public String getPort() throws ConfigException {
        throwIfNoEnvironment();
        return get(environment, PORT_KEY);
    }

    public String getPassword() throws ConfigException {
        throwIfNoEnvironment();
        return get(environment, PASSWORD_KEY);
    }

    public void setPassword(String password) throws ConfigException {
        throwIfNoEnvironment();
        set(environment, PASSWORD_KEY, password);
    }

}

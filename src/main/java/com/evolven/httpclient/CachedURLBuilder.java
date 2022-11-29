package com.evolven.httpclient;

import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.httpclient.http.URLBuilder;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class CachedURLBuilder {
    EvolvenCliConfig config;

    public CachedURLBuilder(EvolvenCliConfig config) {
        this.config = config;
    }

    private String scheme = "https";
    private String host;
    private Integer port;
    private String baseUrl;
    private String path;

    private Map<String, String> params = new HashMap<String, String>();



    public String build() throws MalformedURLException {
        URLBuilder urlBuilder = new URLBuilder();
        if (baseUrl == null) {
            baseUrl = config.getBaseUrl();
        }
        urlBuilder.setBaseUrl(baseUrl);
        if (host == null) {
            host = config.getHost();
        }
        urlBuilder.setHost(host);
        if (port == null) {
            port = config.getPort();
        }
        urlBuilder.setPort(port);
        urlBuilder.setPath(path);
        urlBuilder.addParameters(params);
        return urlBuilder.build();

    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = Integer.parseInt(port);
    }

    public String getBaseUrl() {
        return null;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package com.evolven.httpclient;

import com.evolven.command.InvalidParameterException;
import com.evolven.common.StringUtils;
import com.evolven.config.ConfigException;
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


    public static String createBaseUrl(EvolvenCliConfig config) throws MalformedURLException, ConfigException {
        return new CachedURLBuilder(config).build();
    }

    public String build() throws MalformedURLException, ConfigException {
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
                String portValue = config.getPort();
                if (portValue != null) {
                    try {
                        port = Integer.parseInt(portValue);
                    } catch (NumberFormatException e) {
                        throw new MalformedURLException("Invalid cached value for port: " + port);
                    }
                }
        }

        if (port != null) {
            urlBuilder.setPort(port);
        }
        urlBuilder.setPath(path);
        urlBuilder.addParameters(params);
        return urlBuilder.build();

    }

    public void setScheme(String scheme) {
        if (StringUtils.isNullOrBlank(scheme)) return;
        this.scheme = scheme;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) throws InvalidParameterException {
        if (StringUtils.isNullOrBlank(port)) return;
        try {
            this.port = Integer.parseInt(port);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("port", port, "Can't parse value to integer.");
        }
    }

}

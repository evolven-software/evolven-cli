package com.evolven.httpclient.http;

import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class URLBuilder {

    private String scheme = "https";
    private String host;
    private Integer port;
    private String baseUrl;
    private String path;

    private Map<String, String> params = new HashMap<String, String>();


   private URIBuilder constructBaseUrl() throws MalformedURLException {
       URIBuilder builder = null;

       if (baseUrl != null) {
           try {
               builder = new URIBuilder(baseUrl);
           } catch (URISyntaxException e) {
               throw new MalformedURLException(e.getMessage());
           }
       } else {
           builder = new URIBuilder();
           builder.setScheme(scheme);
           builder.setHost(host);
           if (port != null) {
               builder.setPort(port);
           }
       }
       return builder;
   }


    private URIBuilder addParameters(URIBuilder uriBuilder) throws MalformedURLException {
        params.keySet().stream().forEach(key -> uriBuilder.addParameter(key, params.get(key)));
        return uriBuilder;
    }

    public void addParameters(Map<String, String> params) {
       this.params.putAll(params);
    }

    public String build() throws MalformedURLException {

        URIBuilder builder = constructBaseUrl();
        if (path != null) {
            builder.setPath(path);
        }
        addParameters(builder);
        return toString( builder);
    }

    private String toString(URIBuilder builder) throws MalformedURLException {
        try {
            return builder.build().toURL().toString();
        } catch (URISyntaxException e) {
            throw new MalformedURLException(e.getMessage());
        }

    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
       if (port != null) this.port = Integer.parseInt(port);
    }

    public void setPort(Integer port) {
       this.port = port;
    }

    public String getBaseUrl() throws MalformedURLException {
       return toString(constructBaseUrl());
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

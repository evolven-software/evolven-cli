package com.evolven.httpclient;

import com.evolven.filesystem.FileSystemManager;
import com.evolven.httpclient.http.HttpClient;
import com.evolven.httpclient.http.HttpRequestResult;
import com.evolven.httpclient.http.URLBuilder;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EvolvenHttpClient {
    private String baseUrl;

    public EvolvenHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HttpRequestResult login(String user, String password) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/api");
            builder.setParameter("action", "login");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new HttpRequestResult("Failed to login to Evolven server. " + e.getMessage());
        }
        Map<String, String> body = Stream.of(new String[][] {
                {"json", "true"},
                {"user", user},
                {"pass", password},
                {"isEncrypted", "false"},
                {"ForceIP", "true"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        return HttpClient.post(url, body);
    }

    public HttpRequestResult getPolicies(String apiKey) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/policy");
            builder.setParameter("action", "get");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new HttpRequestResult("Failed to login to Evolven server. " + e.getMessage());
        }
        Map<String, String> body = Stream.of(new String[][] {
                {"EvolvenSessionKey", apiKey},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        return HttpClient.post(url, body);
    }
}

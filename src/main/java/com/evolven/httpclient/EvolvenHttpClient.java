package com.evolven.httpclient;

import com.evolven.httpclient.http.HttpClient;
import com.evolven.httpclient.http.HttpRequestResult;
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

    public HttpRequestResult getPolicies(String apiKey, EvolvenHttpRequestFilter evolvenHttpRequestFilter) {
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
        if (!evolvenHttpRequestFilter.isEmpty()) {
           body.put(evolvenHttpRequestFilter.getFilterName(), evolvenHttpRequestFilter.getFilterValue());
        }
        return HttpClient.post(url, body);
    }

    public HttpRequestResult updatePolicy(String apiKey, Map<String, String> body) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/policy");
            builder.setParameter("action", "update");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new HttpRequestResult("Failed to login to Evolven server. " + e.getMessage());
        }

        body.put("EvolvenSessionKey", apiKey);
        body.put("updateExisting", "true");
        return HttpClient.post(url, body);
    }

    public HttpRequestResult pushPolicy(String apiKey, Map<String, String> body) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/policy");
            builder.setParameter("action", "create");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new HttpRequestResult("Failed to login to Evolven server. " + e.getMessage());
        }

        body.put("EvolvenSessionKey", apiKey);
        body.put("updateExisting", "true");
        body.put("envId", "de");
        return HttpClient.post(url, body);
    }

}

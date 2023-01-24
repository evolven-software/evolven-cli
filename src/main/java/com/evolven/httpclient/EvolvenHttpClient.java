package com.evolven.httpclient;

import com.evolven.common.StringUtils;
import com.evolven.httpclient.http.HttpClient;
import com.evolven.httpclient.http.IHttpRequestResult;
import com.evolven.logging.LoggerManager;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EvolvenHttpClient {
    private String baseUrl;
    private Logger logger = LoggerManager.getLogger(this);

    public EvolvenHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public IHttpRequestResult login(String user, String password) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/api");
            builder.setParameter("action", "login");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new EvolvenHttpRequestResult("Failed to construct url. " + e.getMessage());
        }
        Map<String, String> body = new HashMap<String, String>() {{
                put("json", "true");
                put("user", user);
                put("pass", password);
                put("isEncrypted", "false");
                put("ForceIP", "true");
        }};

        return post(url, body);
    }

    public IHttpRequestResult getPolicies(String apiKey, EvolvenHttpRequestFilter evolvenHttpRequestFilter) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/policy");
            builder.setParameter("action", "get");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new EvolvenHttpRequestResult("Failed to construct url. " + e.getMessage());
        }
        Map<String, String> body = Stream.of(new String[][] {
                {"EvolvenSessionKey", apiKey},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        if (!evolvenHttpRequestFilter.isEmpty()) {
           body.put(evolvenHttpRequestFilter.getFilterName(), evolvenHttpRequestFilter.getFilterValue());
        }
        return post(url, body);
    }

    public IHttpRequestResult updatePolicy(String apiKey, Map<String, String> body) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/policy");
            builder.setParameter("action", "update");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new EvolvenHttpRequestResult("Failed to construct url. " + e.getMessage());
        }

        body.put("EvolvenSessionKey", apiKey);
        body.put("updateExisting", "true");
        return post(url, body);
    }

    public IHttpRequestResult testPolicy(String apiKey, Map<String, String> body) {
        return testPolicy(apiKey, body, null);
    }

    public IHttpRequestResult testPolicy(String apiKey, Map<String, String> body, String envId) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/benchmark");
            builder.setParameter("action", "create");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new EvolvenHttpRequestResult("Failed to construct url. " + e.getMessage());
        }
        body.put("EvolvenSessionKey", apiKey);
        body.put("policy", "true");
        body.put("wait", "true");
        body.put("SkipMissing", "false");
        if (StringUtils.isNullOrBlank(envId)) envId = "de";
        body.put("envId", envId);

        body.entrySet().stream().forEach(es -> {
            System.out.println(es.getKey() + ":" + es.getValue());
        });
        return post(url, body);
    }

    public IHttpRequestResult search(String apiKey, String query) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/Environments");
            builder.setParameter("action", "search");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new EvolvenHttpRequestResult("Failed to construct url. " + e.getMessage());
        }

        Map<String, String> body = Stream.of(new String[][] {
                {"EvolvenSessionKey", apiKey},
                {"crit", query},
                {"envId", "de"},
        }).collect(Collectors.toMap(data -> data[0], data -> data[1]));
        return post(url, body);
    }


    static private IHttpRequestResult post(URL url, Map<String, String> body) {
        return new EvolvenHttpRequestResult(HttpClient.post(url, body));
    }

    public IHttpRequestResult pushPolicy(String apiKey, Map<String, String> body) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/next/policy");
            builder.setParameter("action", "create");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            return new EvolvenHttpRequestResult("Failed to construct url. " + e.getMessage());
        }

        body.put("EvolvenSessionKey", apiKey);
        body.put("updateExisting", "true");
        body.put("envId", "de");
        return post(url, body);
    }

    public IHttpRequestResult uploadPlugin(String apiKey, String base64Zip) {
        URL url =  null;
        try {
            URIBuilder builder = new URIBuilder(baseUrl);
            builder.setPath("/enlight.server/html/scripts/plugin-manager.jsp");
            builder.setParameter("action", "upload");
            builder.setParameter("json", "true");
            url = builder.build().toURL();
            logger.fine("url: " + url);
        } catch (URISyntaxException | MalformedURLException e) {
            return new EvolvenHttpRequestResult("Failed to construct url. " + e.getMessage());
        }
        Map<String, String> body = new HashMap<String, String>() {{
            put("EvolvenSessionKey", apiKey);
            put("zipfile", base64Zip);
        }};
        return post(url, body);
    }

}
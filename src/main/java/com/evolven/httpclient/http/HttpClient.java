package com.evolven.httpclient.http;

import com.evolven.logging.LoggerManager;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HttpClient {

    private static final Logger logger = LoggerManager.getLogger(HttpClient.class);

    private static HttpRequestResult executeRequest(CloseableHttpClient httpClient, HttpUriRequest request) {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String result = new BufferedReader(new InputStreamReader(entity.getContent()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            EntityUtils.consume(entity);
            return new HttpRequestResult(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), result);
        } catch (IOException e) {
            return new HttpRequestResult("Failed to execute request. " + e.getMessage());
        }
    }

    private static HttpRequestResult post(URL url, HttpEntity httpEntity) {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url.toString());
            httpPost.setEntity(httpEntity);
            return executeRequest(httpclient, httpPost);
        } catch (IOException e) {
            return new HttpRequestResult("Failed to execute POST request. " + e.getMessage());
        }
    }

    private static List<NameValuePair> toNameValuePairList(Map<String, String> map) {
        return map
                .keySet()
                .stream()
                .map(key -> new BasicNameValuePair(key, map.get(key)))
                .collect(Collectors.toList());
    }

    public static HttpRequestResult post(URL url, Map<String, String> form) {
        logger.fine("method: POST; url: " + url.toString());
        UrlEncodedFormEntity urlEncodedFormEntity = null;
        try {
            urlEncodedFormEntity = new UrlEncodedFormEntity(toNameValuePairList(form));
        } catch (UnsupportedEncodingException e) {
            return new HttpRequestResult("Failed  to execute POST request. " + e.getMessage());
        }
        return post(url, urlEncodedFormEntity);
    }

    public static HttpRequestResult post(URL url, String body) {
        logger.fine("method: POST; url: " + url.toString() + "; body: " + body);

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(body);
        } catch (UnsupportedEncodingException e) {
            return new HttpRequestResult("Failed to execute POST request. " + e.getMessage());
        }
        return post(url, stringEntity);
    }

    public static void loginTest() {

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://host13.evolven.com/enlight.server/next/api?action=login");
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("json", "true"));
            nvps.add(new BasicNameValuePair("user", "evolven"));
            nvps.add(new BasicNameValuePair("pass", "Mdls1997"));
            nvps.add(new BasicNameValuePair("isEncrypted", "false"));
            nvps.add(new BasicNameValuePair("ForceIP", "true"));
            try {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps);
                String input = new BufferedReader(new InputStreamReader(urlEncodedFormEntity.getContent()))
                        .lines().collect(Collectors.joining("&"));
                httpPost.setEntity(urlEncodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
                System.out.println(response2.getStatusLine().getStatusCode() + " " + response2.getStatusLine().getReasonPhrase());
                HttpEntity entity2 = response2.getEntity();
                InputStream inputStream = entity2.getContent();
                String result = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
                EntityUtils.consume(entity2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getPoliciesTest() {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://host13.evolven.com/enlight.server/next/policy?action=get&json=true");
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("EvolvenSessionKey", "f641f8e507c14e734ce3e2740e7fd2a88b1bcc73"));
            try {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps);
                String input = new BufferedReader(new InputStreamReader(urlEncodedFormEntity.getContent()))
                        .lines().collect(Collectors.joining("&"));
                httpPost.setEntity(urlEncodedFormEntity);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            try (CloseableHttpResponse response2 = httpclient.execute(httpPost)) {
                System.out.println(response2.getStatusLine().getStatusCode() + " " + response2.getStatusLine().getReasonPhrase());
                HttpEntity entity2 = response2.getEntity();
                InputStream inputStream = entity2.getContent();
                String result = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
                System.out.println(result);
                EntityUtils.consume(entity2);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

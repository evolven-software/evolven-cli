package com.evolven.httpclient.http;

import com.evolven.logging.LoggerManager;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
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

    static CloseableHttpClient createHttpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String skipSSLCheck = System.getenv("EVOLVEN_CLI_SKIP_SSL_CHECK");
        if (skipSSLCheck == null) {
            return HttpClients.createDefault();
        }
        return HttpClients
                .custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    private static HttpRequestResult post(URL url, HttpEntity httpEntity) {
        try (CloseableHttpClient httpClient = createHttpClient()) {
            HttpPost httpPost = new HttpPost(url.toString());
            httpPost.setEntity(httpEntity);
            return executeRequest(httpClient, httpPost);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
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

}

package com.evolven.httpclient;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EvolvenHttpRequestFilter {

    public static final String KEY = "crit";
    public static final String NAME_KEY = "name";

    private Map<String, String> fiters = new HashMap<>();

    public void add(String key, String value) {
        fiters.put(key, value);
    }

    public void addName(String name) {
        add(NAME_KEY, name);
    }

    public boolean isEmpty() {
        return fiters.size() == 0;
    }

    public String getFilterValue() {
        StringBuilder sb = new StringBuilder("\"");
        fiters.keySet().stream().forEach(k -> sb.append(k).append("=").append(fiters.get(k)));
        sb.append("\"");
        return sb.toString();
    }

    public String getFilterName() {
        return KEY;
    }


}

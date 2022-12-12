package com.evolven.config;

import com.evolven.common.YAMLUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class JsonObjectConfig implements Config {

    ObjectNode root;

    public  JsonObjectConfig(ObjectNode root) {
        this.root = root;
    }

    void dump(File file) throws IOException {
        YAMLUtils.dump(root, file);
    }

    @Override
    public String get(String key) {
        JsonNode node = root.get(key);
        if (node == null) return null;
        return node.asText();
    }

    @Override
    public void set(String key, String value) {
        if (value == null) {
            root.remove(key);
        } else {
            root.put(key, value);
        }
    }

    @Override
    public String get(String environment, String key) {
        JsonNode node = root.with(environment).get(key);
        if (node == null) return null;
        return node.asText();
    }

    @Override
    public void set(String environment, String key, String value) {
        if (value == null) {
            root.with(environment).remove(key);
        } else {
            root.with(environment).put(key, value);
        }

    }

}

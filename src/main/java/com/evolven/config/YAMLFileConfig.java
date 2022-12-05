package com.evolven.config;

import com.evolven.common.YAMLUtils;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.File;
import java.io.IOException;

public class YAMLFileConfig implements Config, AutoCloseable {

    JsonObjectConfig config;
    File file;
   public YAMLFileConfig(File file)  {
       this.file = file;
   }

   public void open() throws ConfigException {
       if (config != null) return;
       if (!file.exists()) {
           config = new JsonObjectConfig(JsonNodeFactory.instance.objectNode());
           return;
       }
       try {
           config = new JsonObjectConfig(YAMLUtils.load(file));
       } catch (IOException e) {
           throw new ConfigException("Failed to open config file (" + file.getAbsolutePath() + "). " + e.getMessage());
       }
   }

   public void close() throws IOException {
       config.dump(file);
   }

    @Override
    public String get(String key) {
       return config.get(key);
    }

    @Override
    public void set(String key, String value) {
        config.set(key, value);
    }

    @Override
    public String get(String namespace, String key) {
        return config.get(namespace, key);
    }

    @Override
    public void set(String namespace, String key, String value) {
        config.set(namespace, key, value);
    }


}

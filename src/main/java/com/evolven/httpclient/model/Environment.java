package com.evolven.httpclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Environment {

    @JsonProperty("envId")
    String envId;
    @JsonProperty("Name")
    String name;
    @JsonProperty("Host")
    String host;

    @JsonProperty("Compliance")
    boolean compliance;

    public boolean isCompliance() {
        return compliance;
    }

    public String getEnvId() {
        return envId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getHost() {
        return host;
    }
}

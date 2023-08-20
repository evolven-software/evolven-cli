package com.evolven.httpclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentStatus {

    @JsonProperty("ID")
    String ID;
    @JsonProperty("envId")
    String envId;
    @JsonProperty("Name")
    String name;
    @JsonProperty("Host")
    String host;

    @JsonProperty("Status")
    String status;

    @JsonProperty("Version")
    String version;

    public static final Map<String , String> STATUS = new HashMap<String , String>() {{
        put("33100", "Not supported");
        put("33101", "Available");
        put("33102", "Unavailable");
        put("18102", "Need Attention...");
        put("18100", "OK...");
        put("18101", "Validating...");
        put("18103", "Missing Credentials...");
        put("31112", "Service Stopped");
        put("31113", "Clearing Cache");
        put("31114", "Upgrade Required");
        put("31108", "Update Failed (Stopped)");
        put("31109", "Installing Update");
        put("31110", "Update (Pending)");
        put("31111", "Update (Succeeded)");
        put("31104", "Idle");
        put("31105", "Stopping");
        put("31106", "Unmanaged");
        put("31107", "Downloading Update");
        put("31102", "Running");
        put("31103", "Starting");
        put("31115", "Invalid Certificate");
        put("31100", "Connection Lost");
        put("31117", "Uninstalled");
        put("31116", "Missing Agent");
        put("31101", "Initializing");
    }};

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return STATUS.get(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        if (value == null) return "";
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @JsonProperty("Value")
    String value;

    public String getEnvId() {
        return envId;
    }

    public void setEnvId(String envId) {
        this.envId = envId;
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

    public void setHost(String host) {
        this.host = host;
    }

}

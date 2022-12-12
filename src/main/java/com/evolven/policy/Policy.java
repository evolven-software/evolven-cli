package com.evolven.policy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;
import java.util.stream.Collectors;

public class Policy {
    private static final String NAME_KEY = "Name";
    private JsonNode policy;

    public Policy(JsonNode policy) {
        this.policy = policy;
    }

    public String getName() {
        JsonNode nameNode = policy.get(NAME_KEY);
        if (nameNode == null) return null;
        return nameNode.asText();
    }

    public Map<String, String > flatten(PolicyConfig config) {
        return config.getEditablePolicyFields()
                .stream()
                .filter(f -> policy.get(f) != null)
                .collect(Collectors.toMap(f -> f, f -> policy.get(f).asText()));
    }

    protected JsonNode getPolicy() {
        return policy;
    }
}

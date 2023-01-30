package com.evolven.policy;

import com.evolven.common.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.stream.Collectors;

public class Policy {
    private JsonNode policy;

    public Policy(JsonNode policy) {
        JsonNode valueNode = policy.get(PolicyConfigDefault.VALUE_FIELD);
        if (valueNode != null) {
            String value = valueNode.asText();
            if (!StringUtils.isNullOrBlank(value)) {
                ((ObjectNode) policy).put(PolicyConfigDefault.VALUE_FIELD, StringUtils.stripQuotes(value));
            }
        }
        this.policy = policy;
    }

    public String getName() {
        JsonNode nameNode = policy.get(PolicyConfigDefault.NAME_FIELD);
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

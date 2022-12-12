package com.evolven.httpclient.response;

import com.evolven.policy.Policy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

public class PullPolicyResponse {

    String json;

    public PullPolicyResponse(String json) {
        this.json = json;
    }

    public Iterator<Policy> iterator() {
        JsonNode responseNode = null;
        try {
            responseNode = new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            return Collections.emptyIterator();
        }
        JsonNode nextNode = responseNode.get("Next");
        if (nextNode == null) return Collections.emptyIterator();
        JsonNode ruleNode = nextNode.get("Rule");
        if (ruleNode == null) return Collections.emptyIterator();
        Iterator<JsonNode> iterator = null;
        if (ruleNode.isArray()) {
            iterator = ruleNode.iterator();
        } else {
            iterator = (new ArrayList<>(Arrays.asList(ruleNode))).iterator();
        }
        final Iterator<JsonNode> finalIterator = iterator;
        return new Iterator<Policy>() {
            @Override
            public boolean hasNext() {
                return finalIterator.hasNext();
            }

            @Override
            public Policy next() {
                return new Policy(finalIterator.next());
            }
        };
    }
}

package com.evolven.httpclient.response;

import com.evolven.httpclient.model.AgentStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class AgentStatusResponse {

    String json;

    public AgentStatusResponse(String json) {
        this.json = json;
    }

    public Iterator<AgentStatus> iterator() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode responseNode = null;
        try {
            responseNode = new ObjectMapper().readTree(json);
        } catch (JsonProcessingException e) {
            return Collections.emptyIterator();
        }
        JsonNode nextNode = responseNode.get("Next");
        if (nextNode == null) return Collections.emptyIterator();
        JsonNode hostNode = nextNode.get("Host");
        if (hostNode == null) return Collections.emptyIterator();
        Iterator<JsonNode> iterator = null;
        if (hostNode.isArray()) {
            iterator = hostNode.iterator();
        } else {
            iterator = (new ArrayList<>(Arrays.asList(hostNode))).iterator();
        }
        final Iterator<JsonNode> finalIterator = iterator;
        return new Iterator<AgentStatus>() {
            @Override
            public boolean hasNext() {
                return finalIterator.hasNext();
            }

            public AgentStatus next() {
                try {
                    return mapper.treeToValue(finalIterator.next(), AgentStatus.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

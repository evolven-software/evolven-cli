package com.evolven.httpclient.response;

import com.evolven.httpclient.model.Environment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class SearchEnvironmentResponse {

    String json;

    public SearchEnvironmentResponse(String json) {
        this.json = json;
    }

    public Iterator<Environment> iterator() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode responseNode = null;
        try {
            responseNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            return Collections.emptyIterator();
        }
        JsonNode nextNode = responseNode.get("Next");
        if (nextNode == null) return Collections.emptyIterator();
        JsonNode environmentNode = nextNode.get("Environment");
        if (environmentNode == null) return Collections.emptyIterator();
        Iterator<JsonNode> iterator = null;
        if (environmentNode.isArray()) {
            iterator = environmentNode.iterator();
        } else {
            iterator = (new ArrayList<>(Arrays.asList(environmentNode))).iterator();
        }
        final Iterator<JsonNode> finalIterator = iterator;
        return new Iterator<Environment>() {
            @Override
            public boolean hasNext() {
                return finalIterator.hasNext();
            }

            @Override
            public Environment next() {
                try {
                    return mapper.treeToValue(finalIterator.next(), Environment.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

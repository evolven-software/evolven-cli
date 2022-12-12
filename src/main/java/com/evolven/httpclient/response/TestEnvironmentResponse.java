package com.evolven.httpclient.response;
import com.evolven.httpclient.model.BenchmarkResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class TestEnvironmentResponse {

    String json;

    public TestEnvironmentResponse(String json) {
        this.json = json;
    }

    public Iterator<BenchmarkResult> iterator() {
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JsonNode responseNode = null;
        try {
            responseNode = mapper.readTree(json);
        } catch (JsonProcessingException e) {
            return Collections.emptyIterator();
        }
        JsonNode resultNode = responseNode.get("Next");
        //JsonNode nextNode = responseNode.get("Next");
        //if (nextNode == null) return Collections.emptyIterator();
        //JsonNode resultNode = nextNode.get("NumericValue");
        if (resultNode == null) return Collections.emptyIterator();
        Iterator<JsonNode> iterator = null;
        if (resultNode.isArray()) {
            iterator = resultNode.iterator();
        } else {
            iterator = (new ArrayList<>(Arrays.asList(resultNode))).iterator();
        }
        final Iterator<JsonNode> finalIterator = iterator;
        return new Iterator<BenchmarkResult>() {
            @Override
            public boolean hasNext() {
                return finalIterator.hasNext();
            }
            @Override
            public BenchmarkResult next() {
                try {
                    return mapper.treeToValue(finalIterator.next(), BenchmarkResult.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}

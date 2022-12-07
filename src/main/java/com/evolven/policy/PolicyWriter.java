package com.evolven.policy;

import com.evolven.common.YAMLUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.stream.Collectors;

public class PolicyWriter {

    PolicyConfig config;

    YAMLMapper mapper;

    public PolicyWriter(PolicyConfig config) {
        this.config = config;
        mapper = new YAMLMapper(YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build());
    }

    public void write(File file, Policy policy) throws IOException {
        ObjectNode node = YAMLUtils.createObjectNode();
        JsonNode policyNode = policy.getPolicy();
        config.getEditablePolicyFields()
                .stream()
                .map(f -> new Pair<>(f, policyNode.get(f)))
                .filter(p -> p.getValue() != null)
                .forEach(p -> node.put(p.getKey(), p.getValue().asText()));
        mapper.writeValue(file, node);
    }

}

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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.*;
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
        JsonNode policyNode = policy.getPolicy();
        if (config.isSkipReadonly()) {
            ObjectNode node = YAMLUtils.createObjectNode();
            config.getGroupedEditablePolicyFields()
                    .stream()
                    .map(f -> new Pair<>(f, policyNode.get(f)))
                    .filter(p -> p.getValue() != null)
                    .forEach(p -> node.put(p.getKey(), p.getValue().asText()));
            mapper.writeValue(file, node);
            if (config.hasGroupings()) {
                // the editable fields were added in the grouped order 
                addGroupingsComment(file, config.getEditablePolicyFields(), config.getGroupings(), config.getComments());
            }
        } else {
            mapper.writeValue(file, policyNode);
        }
        if (config.isAppendOriginalPolicyAsComment()) {
            appendJsonNodeAsComment(file, policyNode);
        }
    }

    private void addGroupingsComment(File file, List<String> editablePolicyFields,
                                     LinkedHashMap<String, List<String>> groupings,
                                     Map<String, String> comments) {

        // the assumption is that editable fields were added in the grouped order
        List<String> lines = null;
        try {
            lines = Files.readAllLines(file.toPath());
        } catch (IOException e) {
            return;
        }
        int ix = 0;
        for (String key: groupings.keySet()) {
            String comment = "# " + (comments.get(key) != null ? comments.get(key) : key);
            lines.add(ix, comment);
            ix++;
            Set<String> yamlKeys = groupings.get(key).stream().map(f -> f + ": ").collect(Collectors.toSet());
            for (String line : lines.subList(ix, lines.size())) {
                if (yamlKeys.stream().noneMatch(line::startsWith)) break;
                ix++;
            }
        }
        try {
            Files.write(file.toPath(), lines, Charset.defaultCharset());
        } catch (IOException e) {}
    }

    private void appendJsonNodeAsComment(File file, JsonNode node) throws IOException {
            StringBuilder sb = new StringBuilder("\n\n### --------------- Original Policy --------------- ");
            Arrays.stream(mapper.writeValueAsString(node).split("\\R")).forEach(l -> sb.append("\n#").append(l));
            sb.append("\n");
            Files.write(file.toPath(), sb.toString().getBytes(), new StandardOpenOption[]{StandardOpenOption.APPEND});
    }

}

package com.evolven.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class YAMLUtils {

    public static ObjectNode load(File file) throws IOException {
        JsonNode jsonNode = (new YAMLMapper()).readTree(file);
        if (jsonNode instanceof MissingNode) {
            return null;
        }
        return (ObjectNode) jsonNode;
    }

    public static ObjectNode createObjectNode()  {
        return new YAMLMapper().createObjectNode();
    }

    public static ArrayNode createArrayNode()  {
        return new YAMLMapper().createArrayNode();
    }

    public static ArrayNode createArrayNode(List<String> values)  {
        ArrayNode arrayNode = createArrayNode();
        values.stream().forEach(v -> arrayNode.add(v));
        return arrayNode;
    }


    public static void dump(ObjectNode node, File file) throws IOException {
        YAMLMapper mapper = new YAMLMapper(YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build());
        String yamlString = mapper.writeValueAsString(node);
        Files.write(file.toPath(), yamlString.getBytes());
    }


}

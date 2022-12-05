package com.evolven.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.MissingNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class YAMLUtils {

    public static ObjectNode load(File file) throws IOException {
        JsonNode jsonNode = (new YAMLMapper()).readTree(file);
        if (jsonNode instanceof MissingNode) {
            return createObjectNode();
        }
        return (ObjectNode) jsonNode;
    }

    public static ObjectNode createObjectNode()  {
        return JsonNodeFactory.instance.objectNode();
    }

    public static void dump(ObjectNode node, File file) throws IOException {
        YAMLMapper mapper = new YAMLMapper(YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build());
        String yamlString = mapper.writeValueAsString(node);
        Files.write(file.toPath(), yamlString.getBytes());
    }


}

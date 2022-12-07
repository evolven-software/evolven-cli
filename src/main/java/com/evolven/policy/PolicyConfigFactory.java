package com.evolven.policy;

import com.evolven.common.StringUtils;
import com.evolven.common.YAMLUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PolicyConfigFactory {
    File file;

    public static final String EDITABLE_FIELDS_KEY = "editable-fields";

    public static final Set<String> editablePolicyFields = new HashSet<>(Arrays.asList(
            "Comment",
            "Enabled",
            "EnvironmentType",
            "EnvironmentName ",
            "Folder",
            "Name",
            "Path",
            "Tags",
            "Token",
            "Value"));

    public PolicyConfigFactory() {}

    public PolicyConfigFactory(File file) {
        this.file = file;
    }

    Set<String> loadEditablePolicyFields() {
        if (file == null || !file.exists()) return null;
        ObjectNode node = null;
        try {
            node = YAMLUtils.load(file);
        } catch (IOException e) {}
        if (node == null) return null;
        JsonNode editableFieldsNode = node.get(EDITABLE_FIELDS_KEY);
        if (editableFieldsNode == null) return null;
        Set<String> editableFields = new HashSet<>();
        for (JsonNode fieldNode : editableFieldsNode) {
            String field = fieldNode.asText();
            if (StringUtils.isNullOrBlank(field)) continue;
            editableFields.add(field);
        }
        return editableFields;
    }

    Set<String> getEditablePolicyFields() {
        Set<String> editablePolicyFields = loadEditablePolicyFields();
        if (editablePolicyFields == null || editablePolicyFields.isEmpty()) {
            return this.editablePolicyFields;
        }
        return editablePolicyFields;
    }

    public static void dumpInitialConfig(File file) throws IOException {
       ObjectNode root = YAMLUtils.createArrayNode(EDITABLE_FIELDS_KEY, editablePolicyFields);
       YAMLUtils.dump(root, file);
    }


    public static String getInitialConfig() {
        final StringBuilder sb = new StringBuilder(EDITABLE_FIELDS_KEY);
        sb.append("\n");
        Consumer<String> consumer = (s) -> sb.append("- ").append(s).append("\n");
        editablePolicyFields.stream().forEach(consumer);
        return sb.toString();
    }

    public PolicyConfig createConfig() {
        return new PolicyConfig(getEditablePolicyFields());
    }

    public static PolicyConfig createConfig(File file) {
        return new PolicyConfigFactory(file).createConfig();
    }

}

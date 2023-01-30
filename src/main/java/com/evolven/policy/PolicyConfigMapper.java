package com.evolven.policy;

import com.evolven.common.StringUtils;
import com.evolven.common.YAMLUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.util.*;
import java.io.File;
import java.io.IOException;

class PolicyConfigMapper {

    public static final String EDITABLE_FIELDS_KEY = "editable-fields";
    public static final String GROUPINGS_KEY = "groupings";
    public static final String GROUPINGS_NAME_KEY = "name";
    public static final String GROUPINGS_COMMENT_KEY = "comment";
    public static final String GROUPINGS_FIELDS_KEY = "fields";
    public static final String SKIP_READONLY_KEY = "skip-readonly";
    public static final String APPEND_ORIGINAL_POLICY_AS_COMMENT_KEY = "append-original-policy-as-comment";

    private static final PolicyConfigModel emptyResult = new PolicyConfigModel();

    public static PolicyConfigModel read(File file) {
        ObjectNode configRoot = null;
        try {
            configRoot = YAMLUtils.load(file);
        } catch (IOException e) {}
        if (configRoot == null) return emptyResult;
        return new PolicyConfigModel(
              getEditablePolicyFields(configRoot),
              getGroupings(configRoot),
              getComments(configRoot),
              getSkipReadOnly(configRoot),
              getAppendOriginalPolicyAsCommentKey(configRoot)
        );
    }

    public static void write(File file, PolicyConfigModel model) throws IOException {
        YAMLMapper mapper = new YAMLMapper(YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build());
        write(file, model, mapper);
    }

    private static void write(File file, PolicyConfigModel model, YAMLMapper mapper) throws IOException {
        mapper.writeValue(file, map(model, mapper));
    }

    private static JsonNode map(PolicyConfigModel model, YAMLMapper mapper) {
        return map(model, mapper.createObjectNode(), mapper);
    }

    private static JsonNode map(PolicyConfigModel model, ObjectNode node, YAMLMapper mapper) {
        ArrayNode editableFieldsNode = mapper.createArrayNode();
        model.editablePolicyFields.stream().forEach(v -> editableFieldsNode.add(v));
        node.put(EDITABLE_FIELDS_KEY, editableFieldsNode);
        ArrayNode groupingsArrayNode = mapper.createArrayNode();
        model.groupings.keySet().stream().forEach(grouping -> {
            ObjectNode groupingNode = mapper.createObjectNode();
            groupingNode.put(GROUPINGS_NAME_KEY, grouping);
            groupingNode.put(GROUPINGS_COMMENT_KEY, model.comments.get(grouping));
            ArrayNode groupingsFieldsNode = mapper.createArrayNode();
            model.groupings.get(grouping).stream().forEach(field -> groupingsFieldsNode.add(field));
            groupingNode.put(GROUPINGS_FIELDS_KEY, groupingsFieldsNode);
            groupingsArrayNode.add(groupingNode);
        });
        node.put(GROUPINGS_KEY, groupingsArrayNode);
        node.put(APPEND_ORIGINAL_POLICY_AS_COMMENT_KEY, model.appendOriginalPolicyAsComment);
        node.put(SKIP_READONLY_KEY, model.skipReadonly);
        return node;
    }

    private static Boolean getSkipReadOnly(JsonNode node) {
        JsonNode skipReadOnly = node.get(SKIP_READONLY_KEY);
        if (skipReadOnly == null) return null;
        return skipReadOnly.asBoolean();
    }

    private static Boolean getAppendOriginalPolicyAsCommentKey(JsonNode node) {
        JsonNode appendOriginalPolicyAsCommentKey = node.get(APPEND_ORIGINAL_POLICY_AS_COMMENT_KEY);
        if (appendOriginalPolicyAsCommentKey == null) return null;
        return appendOriginalPolicyAsCommentKey.asBoolean();
    }

    private static List<String> getEditablePolicyFields(JsonNode node) {
        JsonNode editableFieldsNode = node.get(EDITABLE_FIELDS_KEY);
        if (editableFieldsNode == null) return null;
        List<String> editableFields = new ArrayList<>();
        for (JsonNode fieldNode : editableFieldsNode) {
            String field = fieldNode.asText();
            if (StringUtils.isNullOrBlank(field)) continue;
            editableFields.add(field);
        }
        return editableFields;
    }

    private static Map<String, String> getComments(ObjectNode node) {
        JsonNode groupingsNode = node.get(GROUPINGS_KEY);
        if (groupingsNode == null) return null;
        Map<String, String> comments = new LinkedHashMap<>();
        for (JsonNode groupingNode : groupingsNode) {
            JsonNode groupingCommentNode = groupingNode.get(GROUPINGS_COMMENT_KEY);
            if (groupingCommentNode == null) continue;
            String comment = groupingCommentNode.asText();
            if (StringUtils.isNullOrBlank(comment)) continue;
            JsonNode groupingNameNode = groupingNode.get(GROUPINGS_NAME_KEY);
            if (groupingNameNode == null) continue;
            String groupingName = groupingNameNode.asText();
            if (StringUtils.isNullOrBlank(groupingName)) continue;
            comments.put(groupingName, comment);
        }
        return comments;
    }

    private static LinkedHashMap<String, List<String>> getGroupings(ObjectNode node) {
        JsonNode groupingsNode = node.get(GROUPINGS_KEY);
        if (groupingsNode == null) return null;
        LinkedHashMap<String, List<String>> groupings = new LinkedHashMap<>();
        for (JsonNode groupingNode : groupingsNode) {
            JsonNode groupingFieldsNode = groupingNode.get(GROUPINGS_FIELDS_KEY);
            if (groupingFieldsNode == null) continue;
            List<String> editableFields = new ArrayList<>();
            for (JsonNode groupingFieldNode : groupingFieldsNode) {
                String field = groupingFieldNode.asText();
                if (StringUtils.isNullOrBlank(field)) continue;
                editableFields.add(field);
            }
            JsonNode groupingName = groupingNode.get(GROUPINGS_NAME_KEY);
            groupings.put(groupingName.asText(), editableFields);
        }
        return groupings;
    }

    static class PolicyConfigModel {

        public final List<String> editablePolicyFields;
        public final LinkedHashMap<String, List<String>> groupings;
        public final Map<String, String> comments;

        public final Boolean skipReadonly;
        public final Boolean appendOriginalPolicyAsComment;

        private PolicyConfigModel() {
            this.groupings = null;
            this.comments = null;
            this.editablePolicyFields = null;
            this.skipReadonly = null;
            this.appendOriginalPolicyAsComment = null;
        }

        protected PolicyConfigModel(List<String> editablePolicyFields,
                                  LinkedHashMap<String, List<String>> groupings,
                                  Map<String, String> comments,
                                  Boolean skipReadonly,
                                  Boolean appendOriginalPolicyAsComment) {
            this.groupings = groupings;
            this.comments = comments;
            this.editablePolicyFields = editablePolicyFields;
            this.skipReadonly = skipReadonly;
            this.appendOriginalPolicyAsComment = appendOriginalPolicyAsComment;
        }
    }
}

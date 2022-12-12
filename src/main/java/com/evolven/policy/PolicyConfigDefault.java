package com.evolven.policy;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

class PolicyConfigDefault extends PolicyConfig {

    public static final String NAME_FIELD = "Name";
    public static final String COMMENT_FIELD = "Comment";
    public static final String ENABLED_FIELD = "Enabled";
    public static final String ENVIRONMENT_TYPE_FIELD = "EnvironmentType";
    public static final String ENVIRONMENT_NAME_FIELD = "EnvironmentName";
    public static final String FOLDER_FIELD = "Folder";
    public static final String PATH_FIELD = "Path";
    public static final String VALUE_FIELD = "Value";
    public static final String TAGS_FIELD = "Tags";
    public static final String TOKEN_FIELD = "Token";
    public static final String GENERAL_GROUPING_FIELD = "general";
    public static final String RULE_GROUPING_FIELD = "rule";
    public static final String SCOPE_GROUPING_FIELD = "scope";

    public static final List<String> editablePolicyFields = new ArrayList<>(Arrays.asList(
            NAME_FIELD,
            TAGS_FIELD,
            COMMENT_FIELD,
            ENABLED_FIELD,
            FOLDER_FIELD,
            PATH_FIELD,
            VALUE_FIELD,
            TOKEN_FIELD,
            ENVIRONMENT_TYPE_FIELD,
            ENVIRONMENT_NAME_FIELD));

    public static final LinkedHashMap<String, List<String>> groupings = new LinkedHashMap<String, List<String>>() {{
        put(GENERAL_GROUPING_FIELD, new ArrayList<>(Arrays.asList(NAME_FIELD, TAGS_FIELD, COMMENT_FIELD, ENABLED_FIELD, FOLDER_FIELD)));
        put(RULE_GROUPING_FIELD, new ArrayList<>(Arrays.asList(PATH_FIELD, VALUE_FIELD, TOKEN_FIELD)));
        put(SCOPE_GROUPING_FIELD, new ArrayList<>(Arrays.asList(ENVIRONMENT_TYPE_FIELD, ENVIRONMENT_NAME_FIELD)));
    }};

    public static final Map<String, String> comments = new LinkedHashMap<String, String>() {{
        put(GENERAL_GROUPING_FIELD, "General");
        put(RULE_GROUPING_FIELD, "Rule");
        put(SCOPE_GROUPING_FIELD, "Scope");
    }};
    public PolicyConfigDefault() {
        super(editablePolicyFields, groupings, comments, true, true);
    }
}

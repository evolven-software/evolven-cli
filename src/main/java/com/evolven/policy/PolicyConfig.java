package com.evolven.policy;

import java.util.Set;

public class PolicyConfig {

    private Set<String> editablePolicyFields;

    private boolean parseEditableFieldsOnly = false;

    public PolicyConfig(Set<String> editablePolicyFields) {
       this.editablePolicyFields = editablePolicyFields;
    }

    public Set<String> getEditablePolicyFields() {
        return editablePolicyFields;
    }

    public boolean isParseEditableFieldsOnly() {
        return parseEditableFieldsOnly;
    }

    public void setParseEditableFieldsOnly(boolean parseEditableFieldsOnly) {
        this.parseEditableFieldsOnly = parseEditableFieldsOnly;
    }
}

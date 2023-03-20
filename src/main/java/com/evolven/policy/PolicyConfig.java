package com.evolven.policy;

import java.io.PrintStream;
import java.util.*;

public class PolicyConfig {

    private final List<String> editablePolicyFields;

    private List<String> groupedEditablePolicyFields = null;
    private final LinkedHashMap<String, List<String>> groupings;
    private final Map<String, String> comments;

    private final boolean skipReadonly;
    private final boolean appendOriginalPolicyAsComment;

    public PolicyConfig(List<String> editablePolicyFields,
                        LinkedHashMap<String, List<String>> groupings,
                        Map<String, String> comments,
                        boolean skipReadonly,
                        boolean appendOriginalPolicyAsComment
                        ) {
        this.groupings = groupings;
        this.comments = comments;
        this.editablePolicyFields = editablePolicyFields;
        this.skipReadonly = skipReadonly;
        this.appendOriginalPolicyAsComment = appendOriginalPolicyAsComment;
    }

    boolean hasGroupings() {
        return groupings.size() > 0;
    }

    List<String> getGroupedEditablePolicyFields() {
       if (groupedEditablePolicyFields == null) {
           Set<String> groupedFields = new LinkedHashSet<>();
           groupings.keySet().stream().forEach(k -> groupedFields.addAll(groupings.get(k)));
           groupedFields.addAll(editablePolicyFields);
           groupedEditablePolicyFields = new ArrayList<>(groupedFields);
       }
       return groupedEditablePolicyFields;
    }

    public List<String> getEditablePolicyFields() {
        return editablePolicyFields;
    }

    public boolean isSkipReadonly() {
        return skipReadonly;
    }

    public boolean isAppendOriginalPolicyAsComment() {
        return appendOriginalPolicyAsComment;
    }

    public LinkedHashMap<String, List<String>> getGroupings() {
        return groupings;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public void print(PrintStream out) {
        out.println("Groupings:");
        groupings.keySet().stream().forEach(g -> {
            out.println(g + ":");
            groupings.get(g).forEach(f -> out.println("- " + f));
        });

        out.println("Editable fields:");
        editablePolicyFields.stream().forEach(f -> out.println("- " + f));
        out.println("Groupings Comment:");
        comments.keySet().stream().forEach(k -> out.println(k + ": " + comments.get(k)));
        out.println("Skip readonly fields: " + isSkipReadonly());
        out.println("Append original policy as comment: " + isAppendOriginalPolicyAsComment());
    }
}

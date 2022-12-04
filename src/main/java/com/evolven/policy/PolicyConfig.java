package com.evolven.policy;

import com.evolven.filesystem.FileSystemManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class PolicyConfig {


    FileSystemManager fileSystemManager;
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

    public String getInitialConfig() {
        final StringBuilder sb = new StringBuilder("fields\n");
        Consumer<String> consumer = (s) -> sb.append("- ").append(s).append("\n");
        editablePolicyFields.stream().forEach(consumer);
        return sb.toString();

    }


    public Set<String> getEditablePolicyFields() {
        return editablePolicyFields;
    }

    public PolicyConfig(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    public File createInitialConfigs() throws IOException {
        return this.fileSystemManager.createInitialPolicyConfig(getInitialConfig());
    }
}

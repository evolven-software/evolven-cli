package com.evolven.policy;

import com.evolven.filesystem.FileSystemManager;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Consumer;

public class PolicyConfig {


    String[] initialPolicyFields = new String[] {
            "Comment",
            "Enabled",
            "EnvironmentType",
            "Folder",
            "Guid",
            "IsPublic",
            "Name",
            "Path",
            "SkipMissing",
            "Tags",
            "Token",
            "Value",
    };

    public String getInitialConfig() {
        final StringBuilder sb = new StringBuilder("fields\n");
        Consumer<String> consumer = (s) -> sb.append("- ").append(s).append("\n");
        Arrays.stream(initialPolicyFields).forEach(consumer);
        return sb.toString();

    }

    FileSystemManager fileSystemManager;

    public PolicyConfig(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }

    public File createInitialConfigs() throws IOException {
        return this.fileSystemManager.createInitialPolicyConfig(getInitialConfig());
    }
}

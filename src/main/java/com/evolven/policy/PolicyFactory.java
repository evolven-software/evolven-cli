package com.evolven.policy;

import com.evolven.common.YAMLUtils;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

public class PolicyFactory {

    public static Policy loadPolicy(File file) throws IOException {
        JsonNode policy = YAMLUtils.load(file);
        if (policy == null) return null;
        return new Policy(policy);
    }

    public static Policy createPolicy(JsonNode policy) {
        if (policy == null) return null;
        return new Policy(policy);
    }

}

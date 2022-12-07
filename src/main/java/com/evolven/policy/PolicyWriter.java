package com.evolven.policy;

import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.File;
import java.io.IOException;

public class PolicyWriter {

    PolicyConfig config;

    YAMLMapper mapper;

    public PolicyWriter(PolicyConfig config) {
        this.config = config;
        mapper = new YAMLMapper(YAMLFactory.builder()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .build());
    }

    public void write(File file, Policy policy) throws IOException {
        mapper.writeValue(file, policy.getPolicy());
    }

}

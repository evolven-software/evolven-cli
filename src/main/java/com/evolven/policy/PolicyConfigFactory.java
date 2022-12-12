package com.evolven.policy;


import java.io.File;
import java.io.IOException;

public class PolicyConfigFactory {

    private File configFile;
    private static PolicyConfigDefault initialPolicyConfig = null;

    public PolicyConfigFactory(File configFile) {
        this.configFile = configFile;
    }

    public static void dumpInitialConfig(File file) throws IOException {
        getInitialConfig();
        PolicyConfigMapper.PolicyConfigModel policyConfigModel = new PolicyConfigMapper.PolicyConfigModel(
                initialPolicyConfig.getEditablePolicyFields(),
                initialPolicyConfig.getGroupings(),
                initialPolicyConfig.getComments(),
                initialPolicyConfig.isSkipReadonly(),
                initialPolicyConfig.isAppendOriginalPolicyAsComment());
        new PolicyConfigMapper().write(file, policyConfigModel);
    }

    static public PolicyConfigDefault getInitialConfig() {
        if (initialPolicyConfig == null) {
            initialPolicyConfig = new PolicyConfigDefault();
        }
        return initialPolicyConfig;
    }

    public PolicyConfig createConfig() {
        PolicyConfig initialConfig = getInitialConfig();
        if (configFile == null) return initialConfig;
        PolicyConfigMapper.PolicyConfigModel configModel = new PolicyConfigMapper().read(configFile);
        return new PolicyConfig(
                configModel.editablePolicyFields == null ?
                        initialConfig.getEditablePolicyFields() : configModel.editablePolicyFields,
                configModel.groupings == null ?
                        initialConfig.getGroupings() : configModel.groupings,
                configModel.comments == null ?
                        initialConfig.getComments() : configModel.comments,
                configModel.skipReadonly == null ?
                        initialConfig.isSkipReadonly() : configModel.skipReadonly,
                configModel.appendOriginalPolicyAsComment == null ?
                        initialConfig.isAppendOriginalPolicyAsComment() : configModel.appendOriginalPolicyAsComment
                );
    }

    public static PolicyConfig createConfig(File file) {
        return new PolicyConfigFactory(file).createConfig();
    }
}

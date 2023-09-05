package com.evolven.command;

import com.evolven.common.StringUtils;
import com.evolven.filesystem.EvolvenCliConfig;
import com.evolven.filesystem.FileSystemManager;

import java.util.*;

public abstract class Command {
    public static final String OPTION_OUTPUT = "output";
    public static final String OPTION_FILENAME = "filename";
    public static final String OPTION_FORMAT = "format";
    public static final String OPTION_NAME = "name";
    public static final String OPTION_KEY = "key";
    public static final String OPTION_ENV = "env";
    public static final String OPTION_VALUE = "value";
    public static final String OPTION_ACTIVE_ENV = "activeEnv";
    public static final String OPTION_ID = "id";
    public static final String OPTION_QUERY = "query";
    public static final String OPTION_PATH = "path";
    public static final String OPTION_HOST = "host";
    public static final String OPTION_VERSION = "version";
    public static final String OPTION_SCHEMA = "schema";
    public static final String OPTION_PORT = "port";
    public static final String OPTION_URL = "url";
    public static final String OPTION_USERNAME = "username";
    public static final String OPTION_PASSWORD = "password";
    public static final String OPTION_COLLECTION = "collection";
    public static final String OPTION_POLICY = "policy";
    public static final String OPTION_DASHBOARD = "dashboard";
    public static final String OPTION_POLICY_NAME = OPTION_NAME;
    public static final String OPTION_POLICY_FILENAME = OPTION_FILENAME;
    public static final String OPTION_PLUGIN_ID = OPTION_ID;
    public static final String OPTION_PLUGIN_PATH = OPTION_PATH;
    public static final String OPTION_AGENT_HOST = OPTION_HOST;
    public static final String OPTION_AGENT_HOST_ID = OPTION_HOST;
    public static final String OPTION_AGENT_VERSION = OPTION_VERSION;

    protected Map<String, String> options = new HashMap<>();
    protected Map<String, Boolean> flags = new HashMap<>();
    protected List<String> parameters;
    private int numParams = 0;
    
    protected final FileSystemManager fileSystemManager;
    
    public Command(FileSystemManager fileSystemManager) {
        this.fileSystemManager = fileSystemManager;
    }
    
    protected EvolvenCliConfig getConfig() {
        return fileSystemManager.getConfig();
    }
    
    private void registerOption(String option) {
        this.options.put(option, "");
    }
    
    protected void registerOptions(String ... options) {
        for (String option : options) {
            registerOption(option);
        }
    }

    protected void registerOptions(Collection<String> options) {
        for (String option : options) {
            registerOption(option);
        }
    }
    
    private void registerFlag(String flag) {
        this.flags.put(flag, false);
    }
    
    protected void registerFlags(String ... flags) {
        for (String flag : flags) {
            registerFlag(flag);
        }
    }

    protected void registerFlags(Collection<String> flags) {
        for (String flag : flags) {
            registerFlag(flag);
        }
    }

    protected void registerParams(int numParams) {
        this.numParams = numParams;
        if (numParams != 0) {
            parameters = new ArrayList<>();
        }
    }

    public abstract void execute() throws CommandException;

    public void addOption(String key, String value) throws InvalidParameterException {
        if (!StringUtils.isNullOrBlank(key) && !options.containsKey(key)) throw new InternalInvalidParameterException(key);
        options.put(key, value);
    }

    public void addParameters(String[] params) throws InternalInvalidParameterException {
        if (numParams == 0) throw new InternalInvalidParameterException("<positional>");
        parameters.addAll(Arrays.asList(params));
    }

    public void addFlag(String flag) throws InvalidParameterException {
        if (!StringUtils.isNullOrBlank(flag) && !flags.containsKey(flag)) throw new InternalInvalidParameterException(flag);
        flags.put(flag, true);
    }

    public void addFlag(String flag, boolean value) throws InternalInvalidParameterException {
        if (!StringUtils.isNullOrBlank(flag) && !flags.containsKey(flag)) throw new InternalInvalidParameterException(flag);
        flags.put(flag, value);
    }

    public abstract String getName();
}

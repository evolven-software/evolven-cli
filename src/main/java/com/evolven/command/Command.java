package com.evolven.command;

import com.evolven.common.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Command {

    protected Map<String, String> options = new HashMap<>();
    protected Map<String, Boolean> flags = new HashMap<>();
    private int numParams = 0;

    private List<String> parameters;

    protected void registerOptions(String[] options) {
        Arrays.stream(options).sequential().forEach(option -> this.options.put(option, ""));
    }

    protected void registerOption(String option) {
        this.options.put(option, "");
    }

    protected void registerFlags(String[] flags) {
        Arrays.stream(flags).sequential().forEach(f -> this.flags.put(f, false));
    }

    protected void registerFlag(String flag) {
        flags.put(flag, false);
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

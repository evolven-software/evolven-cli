package com.evolven.cli.main;

import com.evolven.cli.exception.EvolvenCommandException;
import com.evolven.cli.exception.EvolvenCommandExceptionLogin;
import com.evolven.command.*;
import com.evolven.common.Enum;
import com.evolven.logging.LoggerManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.logging.Logger;

public abstract class EvolvenCommand implements Runnable {
    private Command command;
    private static final Logger logger = LoggerManager.getLogger(EvolvenCommand.class);

    public EvolvenCommand() {}

    public EvolvenCommand(Command command) {
        addExecutor(command);
    }

    public EvolvenCommand addExecutor(Command command) {
        this.command = command;
        return this;
    }

    protected abstract void execute() throws CommandException;

    protected void invokeHandler() throws CommandException {
        command.execute();
    }
    
    protected void addOption(Object option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), (String) option);
    }
    
    protected void addOption(Enum option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), option.getName());
    }
    
    protected void addOption(String name, String value) throws InvalidParameterException {
        if (value == null) return;
        command.addOption(name, value);
    }
    
    protected void addOption(String name, Short value) throws InvalidParameterException {
        if (value == null) return;
        command.addOption(name, Short.toString(value));
    }
    
    protected void addOption(Long option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), Long.toString(option));
    }
    
    protected void addOption(Short option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), Short.toString(option));
    }
    
    protected void addOption(File option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        try {
            command.addOption(getFieldName(option, parent), option.getCanonicalPath());
        } catch (IOException e) {
            throw new InvalidParameterException("Can't convert the path " + option.getPath() +  " to canonical path.");
        }
    }
    
    protected void addOption(Integer option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), Integer.toString(option));
    }

    protected void addFlag(String name, Boolean flag) throws InvalidParameterException {
        if (flag == null) flag = false;
        command.addFlag(name, flag);
    }

    protected void addFlag(Boolean flag, Object parent) throws InvalidParameterException {
        if (flag == null) return;
        String fn = getFieldName(flag, parent);
        command.addFlag(fn, flag);
    }

    protected static Field getField(Object fieldObject, Object parent) {
        Class<?> stop = Object.class;
        Class<?> clazz = parent.getClass();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object currentFieldObject = field.get(parent);
                    if (System.identityHashCode(fieldObject) == System.identityHashCode(currentFieldObject)) {
                        return field;
                    }
                } catch (Exception e) {}
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null && !clazz.equals(stop));
        logger.severe("field not found!");
        return null;
    }
    protected static String getFieldName(Object fieldObject, Object parent) {
        Field field = getField(fieldObject, parent);
        if (field == null) return null;
        return field.getName();
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (CommandFailure e) {
            throw new EvolvenCommandException();
        } catch (CommandExceptionNotLoggedIn e) {
            throw new EvolvenCommandExceptionLogin();
        } catch (CommandException e) {
            throw new EvolvenCommandException(e);
        }
    }
}

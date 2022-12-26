package com.evolven.cli;

import com.evolven.cli.exception.EvolvenCommandException;
import com.evolven.cli.exception.EvolvenCommandExceptionLogin;
import com.evolven.command.*;
import com.evolven.common.Enum;
import javafx.fxml.LoadException;

import java.io.File;
import java.io.IOException;

public abstract class EvolvenCommand implements Runnable {
    private Command command;

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

    void addOption(String option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), option);
    }

    void addOption(Enum option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), option.getName());
    }

    void addOption(String name, String value) throws InvalidParameterException {
        if (name == null) return;
        command.addOption(name, value);
    }

    void addOption(Short option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), Short.toString(option));
    }

    void addOption(File option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        try {
            command.addOption(getFieldName(option, parent), option.getCanonicalPath());
        } catch (IOException e) {
            throw new InvalidParameterException("Can't convert the path " + option.getPath() +  " to canonical path.");
        }
    }

    void addOption(Integer option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), Integer.toString(option));
    }

    protected void  addFlag(Boolean flag, Object parent) throws InvalidParameterException {
        if (flag == null) return;
        String fn = getFieldName(flag, parent);
        command.addFlag(fn, flag);
    }

    protected static java.lang.reflect.Field getField(Object fieldObject, Object parent) {

        java.lang.reflect.Field[] allFields = parent.getClass().getDeclaredFields();
        for (java.lang.reflect.Field field : allFields) {
            Object currentFieldObject;
            try {
                currentFieldObject = field.get(parent);
            } catch (Exception e) {
                return null;
            }
            if (fieldObject.equals(currentFieldObject)) {
                return field;
            }
        }
        return null;
    }
    protected static String getFieldName(Object fieldObject, Object parent) {
        java.lang.reflect.Field field = getField(fieldObject, parent);
        if (field == null) return null;
        return field.getName();
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (CommandFailure e) {
            throw new EvolvenCommandException(e);
        } catch (CommandExceptionLogin e) {
            throw new EvolvenCommandExceptionLogin();
        } catch (CommandException e) {
            throw new EvolvenCommandException(e);
        }
    }
}

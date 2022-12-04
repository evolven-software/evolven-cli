package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;
import com.evolven.common.Enum;

import java.io.File;
import java.io.IOException;

public class EvolvenCommand {
    private Command command;
    void addExecutor(Command command) {
        this.command = command;

    }

    protected void execute() throws CommandException {
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

    void addOption(File option, Object parent) throws InvalidParameterException, IOException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), option.getCanonicalPath());
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
}

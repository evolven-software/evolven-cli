package com.evolven.cli;

import com.evolven.command.Command;
import com.evolven.command.CommandException;
import com.evolven.command.InvalidParameterException;

import java.util.Optional;

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

    void addOption(Short option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), Short.toString(option));
    }

    void addOption(Integer option, Object parent) throws InvalidParameterException {
        if (option == null) return;
        command.addOption(getFieldName(option, parent), Integer.toString(option));
    }

    protected void  addFlag(Boolean flag, Object parent) throws InvalidParameterException {
        if (flag == null) return;
        String fn = getFieldName(flag, parent);
        System.out.println("adding flag:" + fn);
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

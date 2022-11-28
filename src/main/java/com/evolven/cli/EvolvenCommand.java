package com.evolven.cli;

import com.evolven.command.Command;

public class EvolvenCommand {
    Command command;
    void addExecutor(Command command) {
        this.command = command;

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

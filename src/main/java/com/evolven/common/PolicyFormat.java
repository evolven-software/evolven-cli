package com.evolven.common;

public enum PolicyFormat implements Enum {
    JSON,
    YAML,
    ROW;

    public String getName() {
        return this.name();
    }
}

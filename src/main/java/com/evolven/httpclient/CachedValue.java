package com.evolven.httpclient;

import com.evolven.command.CommandException;
import com.evolven.common.StringUtils;
import com.evolven.filesystem.EvolvenCliConfig;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CachedValue {
    Map<String, String> bank;
    public CachedValue(Map<String, String> bank) {
        this.bank = bank;
    }

    public String getOrThrow(String key, Supplier<String> supplier, CommandException e) throws CommandException {
        String value = bank.getOrDefault(key, null);
        if (StringUtils.isNullOrBlank(value)) {
            value = supplier.get();
        }
        if (StringUtils.isNullOrBlank(value)) {
            if (e != null) throw e;
            return null;
        }
        return value;

    }
    public String get(String key, Supplier<String> supplier) {
        try {
            return getOrThrow(key, supplier, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void set(String key, Consumer<String> consumer) {
        if (StringUtils.isNullOrBlank(bank.get(key))) return;
        consumer.accept(bank.get(key));
    }

}

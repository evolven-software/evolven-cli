package com.evolven.common;

public class StringUtils {
    public static boolean isNullOrBlank(String s) {
        return s == null || s.isEmpty() || s.trim().isEmpty();
    }
}

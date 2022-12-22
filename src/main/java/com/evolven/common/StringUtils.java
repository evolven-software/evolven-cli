package com.evolven.common;

public class StringUtils {
    public static boolean isNullOrBlank(String s) {
        return s == null || s.isEmpty() || s.trim().isEmpty();
    }

    public static String replaceNonPathCompatibleChars(String s) {
        return replaceNonPathCompatibleChars(s, "_");
    }

    public static String replaceNonPathCompatibleChars(String s, String replacement) {
        return s.replaceAll("[^a-zA-Z0-9\\.\\-]", replacement);
    }


    public static String repeat(String s, int repetitions) {
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < repetitions; i++) {
           sb.append(s);
        }
        return sb.toString();
    }

    public static String repeat(char c, int repetitions) {
        return repeat(Character.toString(c), repetitions);
    }
}

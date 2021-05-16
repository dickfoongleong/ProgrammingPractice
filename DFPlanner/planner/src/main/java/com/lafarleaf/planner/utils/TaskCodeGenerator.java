package com.lafarleaf.planner.utils;

import java.util.Random;

public class TaskCodeGenerator {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase();
    private static final String NUMERIC = "0123456789";
    private static final String ALPHA_NUM = UPPER + LOWER + NUMERIC;
    private static final char[] CODE_CHARS = ALPHA_NUM.toCharArray();

    private static final int SIZE = 15;

    public String generate() {
        StringBuilder builder = new StringBuilder("");
        Random random = new Random();
        while (builder.length() < SIZE) {
            char c = CODE_CHARS[random.nextInt(CODE_CHARS.length)];
            builder.append(c);
        }
        return builder.toString();
    }
}

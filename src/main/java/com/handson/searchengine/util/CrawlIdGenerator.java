package com.handson.searchengine.util;

import java.security.SecureRandom;

public class CrawlIdGenerator {
    private static final int ID_LENGTH = 6;
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generate() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < ID_LENGTH; i++) {
            res.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
        }
        return res.toString();
    }
}

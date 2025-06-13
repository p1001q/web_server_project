package com.babpool.utils;

import java.security.MessageDigest;

public class PasswordUtil {
    public static String hashPassword(String password) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes("UTF-8"));

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
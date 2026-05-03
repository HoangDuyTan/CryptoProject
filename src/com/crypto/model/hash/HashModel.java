package com.crypto.model.hash;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class HashModel {

    public String hashText(String input, String algorithm, String format) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

        return formatOutput(hashBytes, format);
    }

    public String hashFile(String input, String algorithm, String format) throws Exception {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        try (FileInputStream fis = new FileInputStream(input)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hashBytes = digest.digest();

        return formatOutput(hashBytes, format);
    }

    private String formatOutput(byte[] hashBytes, String format) {
        if ("Base64".equalsIgnoreCase(format)) {
            return Base64.getEncoder().encodeToString(hashBytes);
        } else {
            StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
    }
}
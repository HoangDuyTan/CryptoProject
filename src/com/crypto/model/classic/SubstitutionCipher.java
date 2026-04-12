package com.crypto.model.classic;

import com.crypto.utils.AlphabetUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SubstitutionCipher {
    public static String genKey(String key, boolean isVI) throws Exception {
        String alphabet = AlphabetUtils.getAlphabet(false, isVI);
        StringBuilder fullKey = new StringBuilder();

        String keyLower = key.toLowerCase();

        for (char c : keyLower.toCharArray()) {
            if (alphabet.indexOf(c) != -1 && fullKey.indexOf(String.valueOf(c)) == -1) {
                fullKey.append(c);
            }
        }

        if (fullKey.isEmpty()) {
            throw new Exception("Khóa Substitution phải chứa ít nhất 1 chữ cái");
        }

        for (char c : alphabet.toCharArray()) {
            if (fullKey.indexOf(String.valueOf(c)) == -1) {
                fullKey.append(c);
            }
        }

        return fullKey.toString();
    }

    public static String encrypt(String plainText, String key, boolean isVI) throws Exception {
        String fullKey = genKey(key, isVI);
        if (plainText == null || plainText.isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            result.append(AlphabetUtils.substituteCharacter(c, fullKey, isVI, true));
        }
        return result.toString();
    }

    public static String encryptBase64(String plainText, String key, boolean isVI) throws Exception {
        String cipherText = encrypt(plainText, key, isVI);
        return Base64.getEncoder().encodeToString(cipherText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String cipherText, String key, boolean isVI) throws Exception {
        String fullKey = genKey(key, isVI);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);

        StringBuilder result = new StringBuilder();
        for (char c : decodedText.toCharArray()) {
            result.append(AlphabetUtils.substituteCharacter(c, fullKey, isVI, false));
        }
        return result.toString();
    }
}

package com.crypto.model.classic;

import com.crypto.utils.AlphabetUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CaesarCipher {
    public static String encrypt(String plainText, int shift, boolean isVI) {
        if (plainText == null || plainText.isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            result.append(AlphabetUtils.shiftCharacter(c, shift, isVI));
        }
        return result.toString();
    }

    public static String encryptBase64(String plainText, int shift, boolean isVI) {
        String cipherText = encrypt(plainText, shift, isVI);
        return Base64.getEncoder().encodeToString(cipherText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String cipherText, int shift, boolean isVI) {
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);
        return encrypt(decodedText, -shift, isVI);
    }
}

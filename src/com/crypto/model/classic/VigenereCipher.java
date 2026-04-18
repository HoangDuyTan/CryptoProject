package com.crypto.model.classic;

import com.crypto.utils.AlphabetUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class VigenereCipher {
    public static String formatKey(String key, boolean isVI) throws Exception {
        StringBuilder validKey = new StringBuilder();

        for (char c : key.toCharArray()) {
            if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                validKey.append(c);
            }
        }

        if (validKey.isEmpty()) {
            throw new Exception("Khóa Vigenere phải chứa ít nhất 1 chữ cái");
        }
        return validKey.toString();
    }

    public static String encrypt(String plainText, String key, boolean isVI) throws Exception {
        String formattedKey = formatKey(key, isVI);
        int keyIndex = 0;
        int keyLength = formattedKey.length();

        StringBuilder result = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                char keyChar = formattedKey.charAt(keyIndex);
                int shift = AlphabetUtils.getCharValue(keyChar, isVI);
                result.append(AlphabetUtils.shiftCharacter(c, shift, isVI));
                keyIndex = (keyIndex + 1) % keyLength;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    public static String encryptBase64(String plainText, String key, boolean isVI) throws Exception {
        String cipherText = encrypt(plainText, key, isVI);
        return Base64.getEncoder().encodeToString(cipherText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String cipherText, String key, boolean isVI) throws Exception {
        String formattedKey = formatKey(key, isVI);
        int keyIndex = 0;
        int keyLength = formattedKey.length();

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);

            StringBuilder result = new StringBuilder();
            for (char c : decodedText.toCharArray()) {
                if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                    char keyChar = formattedKey.charAt(keyIndex);
                    int shift = AlphabetUtils.getCharValue(keyChar, isVI);
                    result.append(AlphabetUtils.shiftCharacter(c, -shift, isVI));
                    keyIndex = (keyIndex + 1) % keyLength;
                } else {
                    result.append(c);
                }
            }

            return result.toString();
        } catch (IllegalArgumentException e) {
            throw new Exception("Đầu vào không phải là chuỗi Base64 hợp lệ!");
        }
    }
}

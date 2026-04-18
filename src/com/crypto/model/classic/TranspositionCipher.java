package com.crypto.model.classic;

import com.crypto.utils.AlphabetUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TranspositionCipher {
    public static int[] getColumnOrder(String key, boolean isVI) throws Exception {
        StringBuilder validKey = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                validKey.append(c);
            }
        }

        int len = validKey.length();
        if (len == 0) {
            throw new Exception("Khóa hoán vị phải chứa ít nhất 1 chữ cái hợp lệ!");
        }

        int[] order = new int[len];
        boolean[] used = new boolean[len];

        for (int i = 0; i < len; i++) {
            int minVal = Integer.MAX_VALUE;
            int minIndex = -1;
            for (int j = 0; j < len; j++) {
                if (!used[j]) {
                    int val = AlphabetUtils.getCharValue(validKey.charAt(j), isVI);
                    if (val < minVal) {
                        minVal = val;
                        minIndex = j;
                    }
                }
            }
            order[i] = minIndex;
            used[minIndex] = true;
        }
        return order;
    }

    public static String encrypt(String plainText, String key, boolean isVI) throws Exception {
        int[] order = getColumnOrder(key, isVI);
        int cols = order.length;

        StringBuilder validText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                validText.append(c);
            }
        }

        while (validText.length() % cols != 0) {
            validText.append(isVI ? 'a' : 'x');
        }

        int rows = validText.length() / cols;
        char[][] grid = new char[rows][cols];

        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = validText.charAt(idx++);
            }
        }

        StringBuilder encryptedAlpha = new StringBuilder();
        for (int i = 0; i < cols; i++) {
            int c = order[i];
            for (int r = 0; r < rows; r++) {
                encryptedAlpha.append(grid[r][c]);
            }
        }

        StringBuilder result = new StringBuilder();
        int alphaIndex = 0;
        for (char c : plainText.toCharArray()) {
            if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                result.append(encryptedAlpha.charAt(alphaIndex++));
            } else {
                result.append(c);
            }
        }

        while (alphaIndex < encryptedAlpha.length()) {
            result.append(encryptedAlpha.charAt(alphaIndex++));
        }

        return result.toString();
    }

    public static String encryptBase64(String plainText, String key, boolean isVI) throws Exception {
        String cipherText = encrypt(plainText, key, isVI);
        return Base64.getEncoder().encodeToString(cipherText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String cipherText, String key, boolean isVI) throws Exception {
        int[] order = getColumnOrder(key, isVI);
        int cols = order.length;

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);

            StringBuilder validText = new StringBuilder();
            for (char c : decodedText.toCharArray()) {
                if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                    validText.append(c);
                }
            }

            if (validText.length() % cols != 0) {
                throw new Exception("Văn bản giải mã bị hỏng hoặc thiếu chữ cái để xếp thành ma trận!");
            }

            int rows = validText.length() / cols;
            char[][] grid = new char[rows][cols];

            int idx = 0;
            for (int i = 0; i < cols; i++) {
                int c = order[i];
                for (int r = 0; r < rows; r++) {
                    grid[r][c] = validText.charAt(idx++);
                }
            }

            StringBuilder decryptedAlpha = new StringBuilder();
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    decryptedAlpha.append(grid[r][c]);
                }
            }

            StringBuilder result = new StringBuilder();
            int alphaIndex = 0;
            for (char c : decodedText.toCharArray()) {
                if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                    result.append(decryptedAlpha.charAt(alphaIndex++));
                } else {
                    result.append(c);
                }
            }

            while (alphaIndex < decryptedAlpha.length()) {
                result.append(decryptedAlpha.charAt(alphaIndex++));
            }

            return result.toString();
        } catch (IllegalArgumentException e) {
            throw new Exception("Đầu vào không phải là chuỗi Base64 hợp lệ!");
        }
    }
}

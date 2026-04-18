package com.crypto.model.classic;

import com.crypto.utils.AlphabetUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HillCipher {
    public static int modInverse(int a, int m) {
        a = a % m;
        if (a < 0) a += m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) return x;
        }
        return -1;
    }

    public static int[][] parseKey(String key, int m, boolean isVI) throws Exception {
        StringBuilder validKey = new StringBuilder();
        for (char c : key.toCharArray()) {
            if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                validKey.append(c);
            }
        }

        if (validKey.length() != 4) {
            throw new Exception("Khóa Hill Cipher phải chứa ĐÚNG 4 chữ cái hợp lệ (để tạo ma trận 2x2)!");
        }

        int[][] k = new int[2][2];
        k[0][0] = AlphabetUtils.getCharValue(validKey.charAt(0), isVI);
        k[0][1] = AlphabetUtils.getCharValue(validKey.charAt(1), isVI);
        k[1][0] = AlphabetUtils.getCharValue(validKey.charAt(2), isVI);
        k[1][1] = AlphabetUtils.getCharValue(validKey.charAt(3), isVI);

        // ad - bc
        int det = (k[0][0] * k[1][1] - k[0][1] * k[1][0]) % m;
        if (det < 0) det += m;

        if (modInverse(det, m) == -1) {
            throw new Exception("Khóa không hợp lệ! Định thức của ma trận không có nghịch đảo modulo " + m);
        }
        return k;
    }

    public static int[][] getInverseMatrix(int[][] k, int m) {
        int det = (k[0][0] * k[1][1] - k[0][1] * k[1][0]) % m;
        if (det < 0) det += m;

        int inverseDet = modInverse(det, m);
        int[][] inverseK = new int[2][2];
        inverseK[0][0] = (k[1][1] * inverseDet) % m;
        inverseK[0][1] = (-k[0][1] * inverseDet) % m;
        inverseK[1][0] = (-k[1][0] * inverseDet) % m;
        inverseK[1][1] = (k[0][0] * inverseDet) % m;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (inverseK[i][j] < 0) inverseK[i][j] += m;
            }
        }

        return inverseK;
    }

    public static String encrypt(String plainText, String key, boolean isVI) throws Exception {
        int m = isVI ? 89 : 26;
        int[][] k = parseKey(key, m, isVI);

        StringBuilder validText = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                validText.append(c);
            }
        }

        if (validText.length() % 2 != 0) {
            validText.append(isVI ? 'a' : 'x');
        }

        StringBuilder encryptedAlpha = new StringBuilder();
        for (int i = 0; i < validText.length(); i += 2) {
            int p1 = AlphabetUtils.getCharValue(validText.charAt(i), isVI);
            int p2 = AlphabetUtils.getCharValue(validText.charAt(i + 1), isVI);

            // c = p * k mod m
            int c1 = (k[0][0] * p1 + k[0][1] * p2) % m;
            int c2 = (k[1][0] * p1 + k[1][1] * p2) % m;

            boolean upper1 = AlphabetUtils.isUpperCase(validText.charAt(i), isVI);
            boolean upper2 = AlphabetUtils.isUpperCase(validText.charAt(i + 1), isVI);

            encryptedAlpha.append(AlphabetUtils.getCharByIndex(c1, upper1, isVI));
            encryptedAlpha.append(AlphabetUtils.getCharByIndex(c2, upper2, isVI));
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

        if (alphaIndex < encryptedAlpha.length()) {
            result.append(encryptedAlpha.charAt(alphaIndex));
        }

        return result.toString();
    }

    public static String encryptBase64(String plainText, String key, boolean isVI) throws Exception {
        String cipherText = encrypt(plainText, key, isVI);
        return Base64.getEncoder().encodeToString(cipherText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(String cipherText, String key, boolean isVI) throws Exception {
        int m = isVI ? 89 : 26;
        int[][] k = parseKey(key, m, isVI);
        int[][] inverseK = getInverseMatrix(k, m);

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);

            StringBuilder validText = new StringBuilder();
            for (char c : decodedText.toCharArray()) {
                if (AlphabetUtils.getCharValue(c, isVI) != -1) {
                    validText.append(c);
                }
            }

            StringBuilder decryptedAlpha = new StringBuilder();
            for (int i = 0; i < validText.length(); i += 2) {
                int c1 = AlphabetUtils.getCharValue(validText.charAt(i), isVI);
                int c2 = AlphabetUtils.getCharValue(validText.charAt(i + 1), isVI);

                // p = c * k^-1 mod m
                int p1 = (inverseK[0][0] * c1 + inverseK[0][1] * c2) % m;
                int p2 = (inverseK[1][0] * c1 + inverseK[1][1] * c2) % m;

                boolean upper1 = AlphabetUtils.isUpperCase(validText.charAt(i), isVI);
                boolean upper2 = AlphabetUtils.isUpperCase(validText.charAt(i + 1), isVI);

                decryptedAlpha.append(AlphabetUtils.getCharByIndex(p1, upper1, isVI));
                decryptedAlpha.append(AlphabetUtils.getCharByIndex(p2, upper2, isVI));
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

            if (alphaIndex < decryptedAlpha.length()) {
                result.append(decryptedAlpha.charAt(alphaIndex));
            }

            return result.toString();
        } catch (IllegalArgumentException e) {
            throw new Exception("Đầu vào không phải là chuỗi Base64 hợp lệ!");
        }
    }
}

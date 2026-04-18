package com.crypto.model.classic;

import com.crypto.utils.AlphabetUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;
import java.util.StringTokenizer;

public class AffineCipher {
    public static int gcd(int a, int b) {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    public static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }

    public static int[] parseKey(String key, int m) throws Exception {
        StringTokenizer st = new StringTokenizer(key, ",");
        if (st.countTokens() != 2) {
            throw new Exception("Khóa Affine phải có định dạng 'a,b' (ví dụ: 5,8)");
        }

        try {
            int a = Integer.parseInt(st.nextToken().trim());
            int b = Integer.parseInt(st.nextToken().trim());

            if (gcd(a, m) != 1) {
                throw new Exception("Khóa 'a':" + a + " không hợp lệ! Nó phải là số nguyên tố cùng nhau với độ dài bảng chữ cái " + m);
            }
            return new int[]{a, b};
        } catch (NumberFormatException e) {
            throw new Exception("Khóa a và b phải là các số nguyên!");
        }
    }

    public static String genKey(boolean isVI) {
        int m = isVI ? 89 : 26;
        Random random = new Random();
        int a, b;

        do {
            a = random.nextInt(m - 1) + 1;
        } while (gcd(a, m) != 1);

        b = random.nextInt(m);
        return a + "," + b;
    }

    public static String encrypt(String plainText, String key, boolean isVI) throws Exception {
        int m = isVI ? 89 : 26;
        int[] keys = parseKey(key, m);
        int a = keys[0];
        int b = keys[1];

        StringBuilder result = new StringBuilder();
        for (char c : plainText.toCharArray()) {
            int x = AlphabetUtils.getCharValue(c, isVI);
            if (x != -1) {
                int encryptedValue = (a * x + b) % m;
                encryptedValue = (encryptedValue + m) % m;

                boolean isUpper = AlphabetUtils.isUpperCase(c, isVI);
                result.append(AlphabetUtils.getCharByIndex(encryptedValue, isUpper, isVI));
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
        int m = isVI ? 89 : 26;
        int[] keys = parseKey(key, m);
        int a = keys[0];
        int b = keys[1];

        int aInverse = modInverse(a, m);
        if (aInverse == -1) {
            throw new Exception("Không thể tìm thấy nghịch đảo của a, giải mã thất bại");
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
            String decodedText = new String(decodedBytes, StandardCharsets.UTF_8);

            StringBuilder result = new StringBuilder();
            for (char c : decodedText.toCharArray()) {
                int y = AlphabetUtils.getCharValue(c, isVI);
                if (y != -1) {
                    int decryptedValue = (aInverse * (y - b)) % m;
                    decryptedValue = (decryptedValue + m) % m;

                    boolean isUpper = AlphabetUtils.isUpperCase(c, isVI);
                    result.append(AlphabetUtils.getCharByIndex(decryptedValue, isUpper, isVI));
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

package com.crypto.model.symmetric;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AES {
    private SecretKey key;
    private IvParameterSpec ivSpec;
    private String mode;
    private String padding;

    public AES(String mode, String padding) {
        this.mode = mode;
        this.padding = padding;
    }

    public static SecretKey genKey(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keySize, new SecureRandom());
        return keyGen.generateKey();
    }

    public void loadKey(SecretKey key) {
        this.key = key;
    }

    public static IvParameterSpec genIV() {
        byte[] ivBytes = new byte[16];
        new SecureRandom().nextBytes(ivBytes);
        return new IvParameterSpec(ivBytes);
    }

    public void loadIV(IvParameterSpec iv) {
        this.ivSpec = iv;
    }

    private Cipher getCipher(int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/" + mode + "/" + padding);
        if ("ECB".equals(mode)) {
            cipher.init(opmode, this.key);
        } else if (this.ivSpec == null) {
            throw new Exception("Chế độ " + mode + " yêu cầu phải có IV!");
        } else {
            cipher.init(opmode, this.key, ivSpec);
        }
        return cipher;
    }

    public byte[] encrypt(String plainText) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
        byte[] data = plainText.getBytes(StandardCharsets.UTF_8);
        return cipher.doFinal(data);
    }

    public byte[] expand(byte[] data, byte[] expand, int limit) {
        if (data == null) {
            byte[] out = new byte[limit];
            System.arraycopy(expand, 0, out, 0, limit);
            return out;
        }
        byte[] out = new byte[data.length + expand.length];
        System.arraycopy(data, 0, out, 0, data.length);
        System.arraycopy(expand, 0, out, data.length, limit);
        return out;
    }

    public String encryptBase64(String text) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(text));
    }

    public String decrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
        byte[] bytes = cipher.doFinal(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public boolean encryptFile(String src, String des) throws Exception {
        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(src));
             BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(des));
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, bytesRead);
            }
            return true;
        }
    }

    public boolean decryptFile(String src, String des) throws Exception {
        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

        try (BufferedInputStream fis = new BufferedInputStream(new FileInputStream(src));
             CipherInputStream cis = new CipherInputStream(fis, cipher);
             BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(des))) {

            int bytesRead;
            byte[] buffer = new byte[1024];

            while ((bytesRead = cis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
            return true;
        }
    }
}

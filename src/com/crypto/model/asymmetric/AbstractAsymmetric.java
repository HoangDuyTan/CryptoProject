package com.crypto.model.asymmetric;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class AbstractAsymmetric {
    protected PublicKey publicKey;
    protected PrivateKey privateKey;
    protected int keySize;
    protected String algorithmName;
    protected String mode;
    protected String padding;
    protected String symAlgorithm;

    public AbstractAsymmetric(String algorithmName, String mode, String padding) {
        this.algorithmName = algorithmName;
        this.mode = mode;
        this.padding = padding;
    }

    public void genKey(String algorithm, int keySize) throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(keySize);
        KeyPair keyPair = generator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void loadPublicKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithmName);
        this.publicKey = keyFactory.generatePublic(keySpec);
    }

    public void loadPrivateKey(String base64Key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithmName);
        this.privateKey = keyFactory.generatePrivate(keySpec);
    }

    public String getPublicKeyBase64() {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPrivateKeyBase64() {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setPadding(String padding) {
        this.padding = padding;
    }

    public void setSymAlgorithm(String symAlgorithm) {
        this.symAlgorithm = symAlgorithm;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    public byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName + "/" + mode + "/" + padding);
        byte[] in = data.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] out = cipher.doFinal(in);
        return out;
    }

    public String encryptBase64(String data) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(data));
    }

    public String decrypt(String base64) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName + "/" + mode + "/" + padding);
        byte[] in = Base64.getDecoder().decode(base64);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] out = cipher.doFinal(in);
        return new String(out, StandardCharsets.UTF_8);
    }

    public boolean encryptFile(String src, String des) throws Exception {
        String symTrans = getSymTransformation(this.symAlgorithm);
        int symKeySize = getSymKeySize(this.symAlgorithm);

        KeyGenerator genKey = KeyGenerator.getInstance(this.symAlgorithm);
        genKey.init(symKeySize);
        SecretKey symKey = genKey.generateKey();

        Cipher symCipher = Cipher.getInstance(symTrans);
        symCipher.init(Cipher.ENCRYPT_MODE, symKey);
        byte[] iv = symCipher.getIV();

        Cipher cipher = Cipher.getInstance(algorithmName + "/" + mode + "/" + padding);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedSymKey = cipher.doFinal(symKey.getEncoded());

        byte[] encryptedIv = null;
        if (iv != null) {
            encryptedIv = cipher.doFinal(iv);
        }

        try (FileOutputStream fos = new FileOutputStream(des);
             DataOutputStream dos = new DataOutputStream(fos)) {
            String fileName = new File(src).getName();
            String extension = "";
            int i = fileName.lastIndexOf('.');
            if (i > 0) extension = fileName.substring(i);
            dos.writeUTF(extension);

            dos.writeInt(this.keySize);
            dos.writeUTF(symTrans);

            dos.writeInt(encryptedSymKey.length);
            dos.write(encryptedSymKey);

            if (encryptedIv != null) {
                dos.writeInt(encryptedIv.length);
                dos.write(encryptedIv);
            } else {
                dos.writeInt(0);
            }

            try (FileInputStream fis = new FileInputStream(src);
                 CipherOutputStream cos = new CipherOutputStream(fos, symCipher)) {
                int bytesRead;
                byte[] buffer = new byte[8192];
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                }
            }
            return true;
        }
    }

    public boolean decryptFile(String src, String des) throws Exception {
        try (FileInputStream fis = new FileInputStream(src);
             DataInputStream dis = new DataInputStream(fis)) {
            String extension = dis.readUTF();
            int keySize = dis.readInt();
            String symTrans = dis.readUTF();

            int symKeyLen = dis.readInt();
            byte[] encryptedSymKey = new byte[symKeyLen];
            dis.readFully(encryptedSymKey);

            int encryptedIvLen = dis.readInt();
            byte[] encryptedIv = null;
            if (encryptedIvLen > 0) {
                encryptedIv = new byte[encryptedIvLen];
                dis.readFully(encryptedIv);
            }

            Cipher cipher = Cipher.getInstance(algorithmName + "/" + mode + "/" + padding);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] symKeyBytes = cipher.doFinal(encryptedSymKey);

            byte[] iv = null;
            if (encryptedIv != null) {
                iv = cipher.doFinal(encryptedIv);
            }

            String baseAlgo = symTrans.split("/")[0];
            SecretKey symKey = new SecretKeySpec(symKeyBytes, baseAlgo);

            Cipher symCipher = Cipher.getInstance(symTrans);
            if (iv != null) {
                symCipher.init(Cipher.DECRYPT_MODE, symKey, new IvParameterSpec(iv));
            } else {
                symCipher.init(Cipher.DECRYPT_MODE, symKey);
            }

            try (CipherInputStream cis = new CipherInputStream(fis, symCipher);
                 FileOutputStream fos = new FileOutputStream(des)) {
                int bytesRead;
                byte[] buffer = new byte[8192];
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            return true;
        }
    }

    private String getSymTransformation(String algorithm) {
        if (algorithm.equals("ARCFOUR") || algorithm.equals("ChaCha20")) return algorithm;
        return algorithm + "/CBC/PKCS5Padding";
    }

    private int getSymKeySize(String algorithm) {
        switch (algorithm) {
            case "DES":
                return 56;

            case "DESede":
                return 168;

            case "Blowfish":
            case "RC2":
            case "ARCFOUR":
                return 128;

            default:
                return 256;
        }
    }

    public String getFileExtension(String encryptedFilePath) throws Exception {
        try (FileInputStream fis = new FileInputStream(encryptedFilePath);
             DataInputStream dis = new DataInputStream(fis)) {
            return dis.readUTF();
        }
    }
}

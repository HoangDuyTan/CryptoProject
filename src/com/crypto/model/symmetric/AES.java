package com.crypto.model.symmetric;

public class AES extends AbstractSymmetric{

    public AES(String mode, String padding) {
        super("AES", mode, padding);
    }
}

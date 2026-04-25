package com.crypto.model.symmetric;

public class ARCFOUR extends AbstractSymmetric{
    public ARCFOUR(String mode, String padding) {
        super("ARCFOUR", mode, padding);
    }
}

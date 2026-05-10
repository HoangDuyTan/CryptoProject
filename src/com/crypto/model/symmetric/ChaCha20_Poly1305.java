package com.crypto.model.symmetric;

public class ChaCha20_Poly1305 extends AbstractSymmetric{
    public ChaCha20_Poly1305(String mode, String padding) {
        super("ChaCha20-Poly1305", mode, padding);
    }
}

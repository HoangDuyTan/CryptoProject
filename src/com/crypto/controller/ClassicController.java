package com.crypto.controller;

import com.crypto.model.classic.CaesarCipher;
import com.crypto.view.BasicAlgorithmView;

import javax.swing.*;

public class ClassicController {
    private BasicAlgorithmView view;

    public ClassicController(BasicAlgorithmView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.getEncryptBtn().addActionListener(e -> process(true));
        view.getDecryptBtn().addActionListener(e -> process(false));
    }

    private void process(boolean isEncrypt) {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        String alphabet = view.getCbAlphabet().getSelectedItem().toString();
        String key = view.getTfKey().getText();

        String input = view.getTxtInput().getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập văn bản đầu vào!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean isVI = alphabet.equals("Tiếng Việt");

        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập key", "Cảnh báo",  JOptionPane.WARNING_MESSAGE);
        }

        try {
            String result = "";

            switch (algorithm) {
                case "Caesar":
                    int shift = Integer.parseInt(key);
                    result = isEncrypt ? CaesarCipher.encryptBase64(input, shift, isVI) : CaesarCipher.decrypt(input, shift, isVI);
                    break;
                case "Vigenere":
                    break;
                case "Affine":
                    break;
                case "Substitution":
                    break;
                case "Hill":
                    break;
                case "Transposition":
                    break;
                default:
                    JOptionPane.showMessageDialog(view, "Có lỗi xảy ra, vui lòng thử lại", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    break;
            }

            view.getTxtOutput().setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

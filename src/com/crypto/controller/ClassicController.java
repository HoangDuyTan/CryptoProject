package com.crypto.controller;

import com.crypto.model.classic.*;
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
            JOptionPane.showMessageDialog(view, "Vui lòng nhập key", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String result = "";
            switch (algorithm) {
                case "Caesar":
                    try {
                        int shift = Integer.parseInt(key);
                        result = isEncrypt ? CaesarCipher.encryptBase64(input, shift, isVI) : CaesarCipher.decrypt(input, shift, isVI);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(view, "Khóa của thuật toán Caesar phải là 1 số nguyên", "Cảnh báo nhập liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;

                case "Vigenere":
                    try {
                        result = isEncrypt ? VigenereCipher.encryptBase64(input, key, isVI) : VigenereCipher.decrypt(input, key, isVI);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(view, e.getMessage(), "Cảnh báo nhập liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;

                case "Affine":
                    try {
                        result = isEncrypt ? AffineCipher.encryptBase64(input, key, isVI) : AffineCipher.decrypt(input, key, isVI);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(view, e.getMessage(), "Cảnh báo nhập liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;

                case "Substitution":
                    try {
                        result = isEncrypt ? SubstitutionCipher.encryptBase64(input, key, isVI) : SubstitutionCipher.decrypt(input, key, isVI);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(view, e.getMessage(), "Cảnh báo nhập liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;

                case "Hill":
                    try {
                        result = isEncrypt ? HillCipher.encryptBase64(input, key, isVI) : HillCipher.decrypt(input, key, isVI);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(view, e.getMessage(), "Cảnh báo nhập dữ liệu", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;

                case "Transposition":
                    break;

                default:
                    JOptionPane.showMessageDialog(view, "Có lỗi xảy ra, vui lòng thử lại", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    break;
            }

            view.getTxtOutput().setText(result);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}

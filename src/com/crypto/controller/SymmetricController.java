package com.crypto.controller;

import com.crypto.model.symmetric.AES;
import com.crypto.view.SymmetricView;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Base64;

public class SymmetricController {
    private SymmetricView view;

    public SymmetricController(SymmetricView view) {
        this.view = view;
        initController();
    }

    private void initController() {
        view.getEncryptBtn().addActionListener(e -> process(true));
        view.getDecryptBtn().addActionListener(e -> process(false));

        view.getGenKeyBtn().addActionListener(e -> handleGenKey());
        view.getGenIVBtn().addActionListener(e -> handleGenIV());

        view.getImportKeyBtn().addActionListener(e -> importFromFile(view.getTfKey()));
        view.getExportKeyBtn().addActionListener(e -> exportToFile(view.getTfKey()));

        view.getImportFileBtn().addActionListener(e -> importFromFile(view.getTxtInput()));
        view.getExportFileBtn().addActionListener(e -> exportToFile(view.getTxtOutput()));
    }

    private void process(boolean isEncrypt) {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        String mode = view.getCbMode().getSelectedItem().toString();
        String padding = view.getCbPadding().getSelectedItem().toString();

        String ivBase64 = view.getTfIV().getText();
        String keyBase64 = view.getTfKey().getText();
        String input = view.getTxtInput().getText();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập văn bản đầu vào!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (keyBase64.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập key", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String result = "";
            SecretKey secretKey = parseKey(keyBase64, algorithm);
            IvParameterSpec ivSpec = parseIv(ivBase64);

            switch (algorithm) {
                case "AES":
                    AES aes = new AES(mode, padding);
                    aes.loadKey(secretKey);
                    if (ivSpec != null) aes.loadIV(ivSpec);
                    result = isEncrypt ? aes.encryptBase64(input) : aes.decrypt(Base64.getDecoder().decode(input));
                    break;
                default:
                    return;
            }
            view.getTxtOutput().setText(result);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private SecretKey parseKey(String keyBase64, String algorithm) {
        byte[] decodedKey = Base64.getDecoder().decode(keyBase64);
        return new SecretKeySpec(decodedKey, algorithm);
    }

    private IvParameterSpec parseIv(String ivBase64) {
        if (ivBase64.isEmpty()) return null;
        byte[] decodedIV = Base64.getDecoder().decode(ivBase64);
        return new IvParameterSpec(decodedIV);
    }

    private void handleGenKey() {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        String keySizeStr = view.getCbKeySize().getSelectedItem().toString();
        int keySize = Integer.parseInt(keySizeStr.split(" ")[0]);
        String genKey = "";

        try {
            switch (algorithm) {
                case "AES":
                    SecretKey secretKey = AES.genKey(keySize);
                    genKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
                    break;

                default:
                    return;
            }
            view.getTfKey().setText(genKey);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo khóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleGenIV() {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        String genIV = "";

        try {
            switch (algorithm) {
                case "AES":
                    IvParameterSpec ivSpec = AES.genIV();
                    genIV = Base64.getEncoder().encodeToString(ivSpec.getIV());
                    break;

                default:
                    return;
            }
            view.getTfIV().setText(genIV);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo IV: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importFromFile(JTextComponent des) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                String content = new String(Files.readAllBytes(file.toPath()));
                des.setText(content);
                JOptionPane.showMessageDialog(view, "Đọc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi đọc file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToFile(JTextComponent src) {
        if (src.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không có dữ liệu để lưu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if(chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                Files.write(file.toPath(), src.getText().getBytes(), StandardOpenOption.CREATE);
                JOptionPane.showMessageDialog(view, "Lưu file thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi lưu file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

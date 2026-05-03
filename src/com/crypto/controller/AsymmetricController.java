package com.crypto.controller;

import com.crypto.model.asymmetric.AbstractAsymmetric;
import com.crypto.view.AsymmetricView;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class AsymmetricController {
    AsymmetricView view;
    AbstractAsymmetric model;

    public AsymmetricController(AsymmetricView view) {
        this.view = view;
        this.model = null;
        initController();
    }

    private void initController() {
        view.getCbAlgorithm().addActionListener(e -> updateUIBasedOnAlgorithm());

        view.getEncryptBtn().addActionListener(e -> process(true));
        view.getDecryptBtn().addActionListener(e -> process(false));

        view.getGenKeyBtn().addActionListener(e -> handleGenKey());
        view.getLoadPublicKeyBtn().addActionListener(e -> importFromFile(view.getTxtPublicKey()));
        view.getSavePublicKeyBtn().addActionListener(e -> exportToFile(view.getTxtPublicKey(), ".public"));
        view.getLoadPrivateKeyBtn().addActionListener(e -> importFromFile(view.getTxtPrivateKey()));
        view.getSavePrivateKeyBtn().addActionListener(e -> exportToFile(view.getTxtPrivateKey(), ".private"));

        view.getImportFileBtn().addActionListener(e -> importFromFile(view.getTxtInput()));
        view.getExportFileBtn().addActionListener(e -> exportToFile(view.getTxtOutput(), ".txt"));
    }

    private void process(boolean isEncrypt) {
        String input = view.getTxtInput().getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập văn bản hoặc cho file đầu vào!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            setupModelFromUI(isEncrypt);

            File file = new File(input);
            if (file.exists() && file.isFile()) {
                processAsFile(isEncrypt, file);
            } else {
                processAsText(isEncrypt, input);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupModelFromUI(boolean isEncrypt) throws Exception {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        String mode = "ECB";
        String padding = view.getCbPadding().getSelectedItem().toString();
        int keySize = Integer.parseInt(view.getCbKeySize().getSelectedItem().toString().split(" ")[0]);
        String symAlgorithm = view.getCbSymAlgorithm().getSelectedItem().toString();
        String publicKeyBase64 = view.getTxtPublicKey().getText();
        String privateKeyBase64 = view.getTxtPrivateKey().getText();

        model = new AbstractAsymmetric(algorithm, mode, padding);
        model.setSymAlgorithm(symAlgorithm);
        model.setKeySize(keySize);

        if (isEncrypt) {
            if (publicKeyBase64.isEmpty()) {
                throw new Exception("Vui lòng khởi tạo hoặc tải Public Key để Mã hóa!");
            }
            model.loadPublicKey(publicKeyBase64);
        } else {
            if (privateKeyBase64.isEmpty()) {
                throw new Exception("Vui lòng khởi tạo hoặc tải Private Key để Giải mã!");
            }
            model.loadPrivateKey(privateKeyBase64);
        }
    }

    private void processAsFile(boolean isEncrypt, File srcFile) throws Exception {
        JFileChooser saver = new JFileChooser();
        saver.setDialogTitle(isEncrypt ? "Chọn nơi lưu File mã hóa" : "Chọn nơi lưu File giải mã");

        String fullName = srcFile.getName();
        int dotIndex = fullName.lastIndexOf('.');
        String fileName = (dotIndex == -1) ? fullName : fullName.substring(0, dotIndex);
        if(isEncrypt) {
            saver.setSelectedFile(new File(srcFile.getParentFile(), fileName + ".enc"));
        } else {
            String pathName = fileName + model.getFileExtension(srcFile.getAbsolutePath());
            saver.setSelectedFile(new File(srcFile.getParentFile(), pathName));
        }

        if (saver.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File desFile = saver.getSelectedFile();

            if (desFile.exists()) {
                int confirm = JOptionPane.showConfirmDialog(view,
                        "File này đã tồn tại. Bạn có chắc chắn muốn ghi đè lên nó không?",
                        "Xác nhận ghi đè",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            boolean success = isEncrypt
                    ? model.encryptFile(srcFile.getAbsolutePath(), desFile.getAbsolutePath())
                    : model.decryptFile(srcFile.getAbsolutePath(), desFile.getAbsolutePath() + model.getFileExtension(srcFile.getAbsolutePath()));

            if (success) {
                view.getTxtOutput().setText("ĐÃ XỬ LÝ FILE THÀNH CÔNG!\nĐường dẫn: " + desFile.getAbsolutePath());
                JOptionPane.showMessageDialog(view, "Xử lý file thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void processAsText(boolean isEncrypt, String input) throws Exception {
        String result = isEncrypt ? model.encryptBase64(input) : model.decrypt(input);
        view.getTxtOutput().setText(result);
    }

    private void updateUIBasedOnAlgorithm() {

    }

    public void handleGenKey() {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        String keySizeStr = view.getCbKeySize().getSelectedItem().toString();
        int keySize = Integer.parseInt(keySizeStr.split(" ")[0]);

        try {
            if (model == null) {
                model = new AbstractAsymmetric(algorithm, "ECB", view.getCbPadding().getSelectedItem().toString());
            }

            model.genKey(algorithm, keySize);

            view.getTxtPublicKey().setText(model.getPublicKeyBase64());
            view.getTxtPrivateKey().setText(model.getPrivateKeyBase64());
        } catch (Exception e){
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo khóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importFromFile(JTextComponent des) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                String fileName = file.getName();

                if (fileName.endsWith(".txt") || fileName.endsWith(".public") || fileName.endsWith(".private")) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    des.setText(content);
                } else {
                    des.setText(file.getAbsolutePath());
                }

                JOptionPane.showMessageDialog(view, "Đọc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi đọc file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToFile(JTextComponent src, String ext) {
        if (src.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không có dữ liệu để lưu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                file = new File(file.getAbsolutePath() + ext);

                if (file.exists()) {
                    JOptionPane.showMessageDialog(view, "File này đã tồn tại! Vui lòng chọn một tên khác để tạo file mới", "Từ chối ghi đè", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Files.write(file.toPath(), src.getText().getBytes(), StandardOpenOption.CREATE_NEW);
                JOptionPane.showMessageDialog(view, "Lưu file thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi lưu file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

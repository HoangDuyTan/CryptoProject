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
        view.getCbSymAlgorithm().addActionListener(e -> updateUIBasedOnAlgorithm());
        view.getCbSymMode().addActionListener(e -> updatePaddingBasedOnMode());
        updateUIBasedOnAlgorithm();

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
        String padding = view.getCbPadding().getSelectedItem().toString();
        int keySize = Integer.parseInt(view.getCbKeySize().getSelectedItem().toString().split(" ")[0]);

        String symAlgorithm = view.getCbSymAlgorithm().getSelectedItem().toString();
        String symMode = view.getCbSymMode().getSelectedItem().toString();
        String symPadding = view.getCbSymPadding().getSelectedItem().toString();
        int symKeySize = Integer.parseInt(view.getCbSymKeySize().getSelectedItem().toString().split(" ")[0]);

        String publicKeyBase64 = view.getTxtPublicKey().getText();
        String privateKeyBase64 = view.getTxtPrivateKey().getText();

        model = new AbstractAsymmetric(algorithm, "ECB", padding);
        model.setKeySize(keySize);

        model.setSymAlgorithm(symAlgorithm);
        model.setSymMode(symMode);
        model.setSymPadding(symPadding);
        model.setSymKeySize(symKeySize);

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

    // Giải thuật đối xứng
    private void updateUIBasedOnAlgorithm() {
        String algorithm = view.getCbSymAlgorithm().getSelectedItem().toString();
        JComboBox<String> cbMode = view.getCbSymMode();
        JComboBox<String> cbPadding = view.getCbSymPadding();
        JComboBox<String> cbKeySize = view.getCbSymKeySize();

        cbMode.removeAllItems();
        cbPadding.removeAllItems();
        cbKeySize.removeAllItems();

        cbMode.setEnabled(true);
        cbPadding.setEnabled(true);

        switch (algorithm) {
            case "AES":
                cbMode.addItem("ECB");
                cbMode.addItem("CBC");
                cbMode.addItem("CFB");
                cbMode.addItem("OFB");
                cbMode.addItem("CTR");
                cbMode.addItem("GCM");
                cbPadding.addItem("PKCS5Padding");
                cbPadding.addItem("NoPadding");
                cbPadding.addItem("ISO10126Padding");
                cbKeySize.addItem("128 bits");
                cbKeySize.addItem("192 bits");
                cbKeySize.addItem("256 bits");
                break;

            case "DES":
                cbMode.addItem("ECB");
                cbMode.addItem("CBC");
                cbMode.addItem("CFB");
                cbMode.addItem("OFB");
                cbPadding.addItem("PKCS5Padding");
                cbPadding.addItem("NoPadding");
                cbKeySize.addItem("56 bits");
                break;

            case "DESede":
                cbMode.addItem("ECB");
                cbMode.addItem("CBC");
                cbMode.addItem("CFB");
                cbMode.addItem("OFB");
                cbPadding.addItem("PKCS5Padding");
                cbPadding.addItem("NoPadding");
                cbKeySize.addItem("112 bits");
                cbKeySize.addItem("168 bits");
                break;

            case "Blowfish":
                cbMode.addItem("ECB");
                cbMode.addItem("CBC");
                cbMode.addItem("CFB");
                cbMode.addItem("OFB");
                cbMode.addItem("CTR");
                cbPadding.addItem("PKCS5Padding");
                cbPadding.addItem("NoPadding");
                cbKeySize.addItem("128 bits");
                cbKeySize.addItem("256 bits");
                cbKeySize.addItem("448 bits");
                break;

            case "RC2":
                cbMode.addItem("ECB");
                cbMode.addItem("CBC");
                cbMode.addItem("CFB");
                cbMode.addItem("OFB");
                cbPadding.addItem("PKCS5Padding");
                cbPadding.addItem("NoPadding");
                cbKeySize.addItem("40 bits");
                cbKeySize.addItem("64 bits");
                cbKeySize.addItem("128 bits");
                break;

            case "ARCFOUR":
                cbMode.addItem("NONE");
                cbPadding.addItem("NoPadding");
                cbKeySize.addItem("40 bits");
                cbKeySize.addItem("128 bits");
                cbMode.setEnabled(false);
                cbPadding.setEnabled(false);
                break;

            case "ChaCha20":
                cbMode.addItem("NONE");
                cbPadding.addItem("NoPadding");
                cbKeySize.addItem("256 bits");
                cbMode.setEnabled(false);
                cbPadding.setEnabled(false);
                break;

            case "Twofish":
            case "Serpent":
            case "Camellia":
                cbMode.addItem("ECB");
                cbMode.addItem("CBC");
                cbMode.addItem("CFB");
                cbMode.addItem("OFB");
                cbMode.addItem("CTR");
                cbPadding.addItem("PKCS5Padding");
                cbPadding.addItem("NoPadding");
                cbKeySize.addItem("128 bits");
                cbKeySize.addItem("192 bits");
                cbKeySize.addItem("256 bits");
                break;
        }
    }

    private void updatePaddingBasedOnMode() {
        if (view.getCbSymMode().getSelectedItem() == null) return;

        String mode = view.getCbSymMode().getSelectedItem().toString();
        JComboBox<String> cbPadding = view.getCbSymPadding();

        if (mode.equals("CTR") || mode.equals("CFB") || mode.equals("OFB") || mode.equals("GCM") || mode.equals("NONE")) {
            cbPadding.setSelectedItem("NoPadding");
            cbPadding.setEnabled(false);
        } else {
            cbPadding.setEnabled(true);
        }
    }
}

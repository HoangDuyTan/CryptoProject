package com.crypto.controller;

import com.crypto.model.symmetric.*;
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
    private AbstractSymmetric model;

    public SymmetricController(SymmetricView view) {
        this.view = view;
        this.model = null;
        initController();
    }

    private void initController() {
        view.getCbAlgorithm().addActionListener(e -> updateUIBasedOnAlgorithm());
        view.getCbMode().addActionListener(e -> updatePaddingBasedOnMode());
        updateUIBasedOnAlgorithm();

        view.getEncryptBtn().addActionListener(e -> process(true));
        view.getDecryptBtn().addActionListener(e -> process(false));

        view.getGenKeyBtn().addActionListener(e -> handleGenKey());
        view.getGenIVBtn().addActionListener(e -> handleGenIV());

        view.getImportIvBtn().addActionListener(e -> importFromFile(view.getTfIV()));
        view.getExportIvBtn().addActionListener(e -> exportToFile(view.getTfIV(), ".iv"));

        view.getImportKeyBtn().addActionListener(e -> importFromFile(view.getTfKey()));
        view.getExportKeyBtn().addActionListener(e -> exportToFile(view.getTfKey(), ".key"));

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
            setupModelFromUI();

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

    private void setupModelFromUI() throws Exception {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        String mode = view.getCbMode().getSelectedItem().toString();
        String padding = view.getCbPadding().getSelectedItem().toString();
        String keyBase64 = view.getTfKey().getText();
        String ivBase64 = view.getTfIV().getText();

        if (keyBase64.isEmpty()) {
            throw new Exception("Vui lòng khởi tạo hoặc nhập Khóa trước khi xử lý!");
        }

        if (!mode.equals("ECB") && !mode.equals("NONE")) {
            if (ivBase64.isEmpty()) {
                throw new Exception("Chế độ " + mode + " bắt buộc phải có IV. Vui lòng tạo IV!");
            }
        }

        SecretKey secretKey = parseKey(keyBase64, algorithm);
        IvParameterSpec ivSpec = parseIv(ivBase64);

        switch (algorithm) {
            case "AES":
                model = new AES(mode, padding);
                break;

            case "DES":
                model = new DES(mode, padding);
                break;

            case "DESede":
                model = new DESede(mode, padding);
                break;

            case "Blowfish":
                model = new Blowfish(mode, padding);
                break;

            case "RC2":
                model = new RC2(mode, padding);
                break;

            case "ARCFOUR":
                model = new ARCFOUR(mode, padding);
                break;

            case "ChaCha20":
                model = new ChaCha20(mode, padding);
                break;

            case "Twofish":
                model = new Twofish(mode, padding);
                break;

            case "Serpent":
                model = new Serpent(mode, padding);
                break;

            case "Camellia":
                model = new Camellia(mode, padding);
                break;

            default:
                throw new Exception("Thuật toán không hỗ trợ!");
        }

        model.loadKey(secretKey);
        if (ivSpec != null) model.loadIV(ivSpec);
    }

    private void processAsText(boolean isEncrypt, String input) throws Exception {
        String result = isEncrypt ? model.encryptBase64(input) : model.decrypt(Base64.getDecoder().decode(input));
        view.getTxtOutput().setText(result);
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
                    : model.decryptFile(srcFile.getAbsolutePath(), desFile.getAbsolutePath());

            if (success) {
                view.getTxtOutput().setText("ĐÃ XỬ LÝ FILE THÀNH CÔNG!\nĐường dẫn: " + desFile.getAbsolutePath());
                JOptionPane.showMessageDialog(view, "Xử lý file thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
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

        try {
            SecretKey secretKey = AbstractSymmetric.genKey(algorithm, keySize);
            String genKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            view.getTfKey().setText(genKey);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo khóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleGenIV() {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        int ivSize = 16;
        if (algorithm.equals("DES") || algorithm.equals("DESede") || algorithm.equals("Blowfish") || algorithm.equals("RC2")) {
            ivSize = 8;
        }

        try {
            IvParameterSpec ivSpec = AbstractSymmetric.genIV(ivSize);
            String genIV = Base64.getEncoder().encodeToString(ivSpec.getIV());
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
                String fileName = file.getName();

                if (fileName.endsWith(".txt") || fileName.endsWith(".key") || fileName.endsWith(".iv")) {
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

    private void updateUIBasedOnAlgorithm() {
        String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
        JComboBox<String> cbMode = view.getCbMode();
        JComboBox<String> cbPadding = view.getCbPadding();
        JComboBox<String> cbKeySize = view.getCbKeySize();

        view.getTfKey().setText("");
        view.getTfIV().setText("");
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
        if (view.getCbMode().getSelectedItem() == null) return;

        String mode = view.getCbMode().getSelectedItem().toString();
        JComboBox<String> cbPadding = view.getCbPadding();

        if (mode.equals("CTR") || mode.equals("CFB") || mode.equals("OFB") || mode.equals("GCM") || mode.equals("NONE")) {
            cbPadding.setSelectedItem("NoPadding");
            cbPadding.setEnabled(false);
        } else {
            cbPadding.setEnabled(true);
        }
    }
}

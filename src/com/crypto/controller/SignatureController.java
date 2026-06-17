package com.crypto.controller;

import com.crypto.model.signature.SignatureModel;
import com.crypto.view.SignatureView;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.util.Base64;

public class SignatureController {
    private SignatureView view;
    private SignatureModel model;

    public SignatureController(SignatureView view) {
        this.view = view;
        this.model = new SignatureModel();
        initController();
    }

    private void initController() {
        view.getBtnGenerateKeys().addActionListener(e -> displayGeneratedKeys());
        view.getBtnSaveKeys().addActionListener(e -> saveKeysToFile());
        view.getBtnBrowsePrivKey().addActionListener(e -> importFromFile());
        view.getBtnSignOrder().addActionListener(e -> genDigitalSignature());
        view.getBtnCopySignature().addActionListener(e -> executeCopyEvent());
    }

    private void displayGeneratedKeys() {
        try {
            KeyPair keyPair = model.generateRSAKeyPair();

            String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                    Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(keyPair.getPrivate().getEncoded()) +
                    "\n-----END PRIVATE KEY-----\n";

            String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                    Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(keyPair.getPublic().getEncoded()) +
                    "\n-----END PUBLIC KEY-----\n";

            view.getTxtPrivateKey().setText(privateKeyPEM);
            view.getTxtPublicKey().setText(publicKeyPEM);

            view.getTxtPrivateKey().setCaretPosition(0);
            view.getTxtPublicKey().setCaretPosition(0);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi tạo bộ khóa: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveKeysToFile() {
        String pubKeyContent = view.getTxtPublicKey().getText();
        String privKeyContent = view.getTxtPrivateKey().getText();

        if (pubKeyContent.isEmpty() || privKeyContent.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng bấm 'Tạo bộ khóa' trước khi lưu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn thư mục để lưu bộ khóa");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();

            File privFile = new File(dir, "private_key.pem");
            File pubFile = new File(dir, "public_key.pem");

            try (FileOutputStream privOut = new FileOutputStream(privFile);
                 FileOutputStream pubOut = new FileOutputStream(pubFile)) {

                privOut.write(privKeyContent.getBytes());
                pubOut.write(pubKeyContent.getBytes());

                JOptionPane.showMessageDialog(view,
                        "Đã lưu 2 file (public_key.pem và private_key.pem) thành công tại:\n" + dir.getAbsolutePath(),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi khi lưu file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn file Private Key");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Private Key Files (*.pem, *.txt)", "pem", "txt");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                view.getTxtPrivateKeyPath().setText(file.getAbsolutePath());

                JOptionPane.showMessageDialog(view, "Đọc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi đọc file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void genDigitalSignature() {
        String privateKeyPath = view.getTxtPrivateKeyPath().getText();
        String hashInput = view.getTxtHashInput().getText().trim();

        if (privateKeyPath.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn file Private Key!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (hashInput.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng dán mã băm của đơn hàng vào!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String signatureBase64 = model.signHash(hashInput, privateKeyPath);

            view.getTxtSigOutput().setText(signatureBase64);
            view.getBtnCopySignature().setEnabled(true);

            JOptionPane.showMessageDialog(view, "Ký đơn hàng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi ký đơn hàng. Vui lòng kiểm tra lại file Private Key!\nChi tiết: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            view.getTxtSigOutput().setText("");
            view.getBtnCopySignature().setEnabled(false);
        }
    }

    private void executeCopyEvent() {
        String sigText = view.getTxtSigOutput().getText();
        if (!sigText.isEmpty()) {
            StringSelection stringSelection = new StringSelection(sigText);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            JOptionPane.showMessageDialog(view, "Đã sao chép chữ ký! Hãy quay lại trình duyệt để dán.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

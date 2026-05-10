package com.crypto.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AsymmetricView extends JPanel {
    private JComboBox<String> cbAlgorithm, cbPadding, cbKeySize, cbSymAlgorithm, cbSymMode, cbSymPadding, cbSymKeySize;
    JTextArea txtPublicKey, txtPrivateKey;
    JButton GenKeyBtn, LoadPublicKeyBtn, SavePublicKeyBtn, LoadPrivateKeyBtn, SavePrivateKeyBtn;

    JTextArea txtInput, txtOutput;
    JButton encryptBtn, decryptBtn, importFileBtn, exportFileBtn;

    public AsymmetricView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Thuật toán Panel ===
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel asymmetricPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        asymmetricPanel.setBorder(new TitledBorder("Thuật toán bất đối xứng"));
        cbAlgorithm = new JComboBox<>(new String[]{"RSA"});
        cbPadding = new JComboBox<>(new String[]{"PKCS1Padding", "OAEPWithSHA-256AndMGF1Padding"});
        cbKeySize = new JComboBox<>(new String[]{"1024 bits", "2048 bits", "4096 bits"});
        cbKeySize.setSelectedIndex(1);

        asymmetricPanel.add(new JLabel("Thuật toán:"));
        asymmetricPanel.add(cbAlgorithm);
        asymmetricPanel.add(new JLabel("Padding"));
        asymmetricPanel.add(cbPadding);
        asymmetricPanel.add(new JLabel("Key Size:"));
        asymmetricPanel.add(cbKeySize);

        JPanel symmetricPanel = new JPanel(new GridLayout(2, 2, 10, 0));
        symmetricPanel.setBorder(new TitledBorder("Thuật toán đối xứng"));
        cbSymAlgorithm = new JComboBox<>(new String[]{"AES", "DES", "DESede", "Blowfish", "RC2", "ARCFOUR", "ChaCha20", "Twofish", "Serpent", "Camellia"});
        cbSymPadding = new JComboBox<>(new String[]{});
        cbSymMode = new JComboBox<>(new String[]{});
        cbSymKeySize = new JComboBox<>(new String[]{});

        symmetricPanel.add(new JLabel("Thuật toán (file):"));
        symmetricPanel.add(cbSymAlgorithm);
        symmetricPanel.add(new JLabel("Mode:"));
        symmetricPanel.add(cbSymMode);
        symmetricPanel.add(new JLabel("Padding:"));
        symmetricPanel.add(cbSymPadding);
        symmetricPanel.add(new JLabel("Key Size:"));
        symmetricPanel.add(cbSymKeySize);

        topPanel.add(asymmetricPanel);
        topPanel.add(symmetricPanel);

        // === Xử lý khóa Panel ===
        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        GenKeyBtn = new JButton("Tạo cặp khóa");
        LoadPublicKeyBtn = new JButton("Tải khóa Public");
        SavePublicKeyBtn = new JButton("Lưu khóa Public");
        LoadPrivateKeyBtn = new JButton("Tải khóa Private");
        SavePrivateKeyBtn = new JButton("Lưu khóa Private");
        btnPanel.add(LoadPublicKeyBtn);
        btnPanel.add(LoadPrivateKeyBtn);
        btnPanel.add(SavePublicKeyBtn);;
        btnPanel.add(SavePrivateKeyBtn);
        btnPanel.add(GenKeyBtn);

        // public/private khóa Panel
        JPanel keyPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        JPanel publicKeyPanel = new JPanel(new BorderLayout());
        publicKeyPanel.add(new JLabel("Public Key"), BorderLayout.NORTH);
        txtPublicKey = new JTextArea(5, 20);
        txtPublicKey.setLineWrap(true);
        txtPublicKey.setWrapStyleWord(true);
        publicKeyPanel.add(new JScrollPane(txtPublicKey), BorderLayout.CENTER);

        JPanel privateKeyPanel = new JPanel(new BorderLayout());
        privateKeyPanel.add(new JLabel("Private Key"), BorderLayout.NORTH);
        txtPrivateKey = new JTextArea(5, 20);
        txtPrivateKey.setLineWrap(true);
        txtPrivateKey.setWrapStyleWord(true);
        privateKeyPanel.add(new JScrollPane(txtPrivateKey), BorderLayout.CENTER);

        keyPanel.add(publicKeyPanel);
        keyPanel.add(privateKeyPanel);

        // === Panel Xử lý dữ liệu ===
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBorder(new TitledBorder("Xử lý dữ liệu"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Input
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(new TitledBorder("Input"));
        txtInput = new JTextArea();
        txtInput.setLineWrap(true);
        txtInput.setWrapStyleWord(true);
        inputPanel.add(new JScrollPane(txtInput), BorderLayout.CENTER);

        importFileBtn = new JButton("Chọn File");
        inputPanel.add(importFileBtn, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.weightx = 0.45;
        bottomPanel.add(inputPanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        encryptBtn = new JButton("Mã hóa");
        decryptBtn = new JButton("Giải mã");

        encryptBtn.setBackground(new Color(66, 133, 244));
        encryptBtn.setForeground(Color.white);
        decryptBtn.setBackground(new Color(244, 160, 0));
        decryptBtn.setForeground(Color.white);

        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);

        gbc.gridx = 1;
        gbc.weightx = 0.1;
        bottomPanel.add(buttonPanel,gbc);

        // Output
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(new TitledBorder("Output"));
        txtOutput = new JTextArea();
        txtOutput.setLineWrap(true);
        txtOutput.setWrapStyleWord(true);
        outputPanel.add(new JScrollPane(txtOutput), BorderLayout.CENTER);

        exportFileBtn = new JButton("Lưu File");
        outputPanel.add(exportFileBtn, BorderLayout.SOUTH);

        gbc.gridx = 2;
        gbc.weightx = 0.45;
        bottomPanel.add(outputPanel, gbc);

        // === Thêm hết vào ===
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

        container.add(topPanel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(btnPanel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(keyPanel);
        container.add(Box.createRigidArea(new Dimension(0, 10)));
        container.add(bottomPanel);

        add(container, BorderLayout.CENTER);
    }

    public JComboBox<String> getCbAlgorithm() {
        return cbAlgorithm;
    }

    public void setCbAlgorithm(JComboBox<String> cbAlgorithm) {
        this.cbAlgorithm = cbAlgorithm;
    }

    public JComboBox<String> getCbPadding() {
        return cbPadding;
    }

    public void setCbPadding(JComboBox<String> cbPadding) {
        this.cbPadding = cbPadding;
    }

    public JComboBox<String> getCbSymAlgorithm() {
        return cbSymAlgorithm;
    }

    public void setCbSymAlgorithm(JComboBox<String> cbSymAlgorithm) {
        this.cbSymAlgorithm = cbSymAlgorithm;
    }

    public JComboBox<String> getCbSymMode() {
        return cbSymMode;
    }

    public void setCbSymMode(JComboBox<String> cbSymMode) {
        this.cbSymMode = cbSymMode;
    }

    public JComboBox<String> getCbSymPadding() {
        return cbSymPadding;
    }

    public void setCbSymPadding(JComboBox<String> cbPadding) {
        this.cbSymPadding = cbPadding;
    }

    public JComboBox<String> getCbSymKeySize() {
        return cbSymKeySize;
    }

    public void setCbSymKeySize(JComboBox<String> cbSymKeySize) {
        this.cbSymKeySize = cbSymKeySize;
    }

    public JComboBox<String> getCbKeySize() {
        return cbKeySize;
    }

    public void setCbKeySize(JComboBox<String> cbKeySize) {
        this.cbKeySize = cbKeySize;
    }

    public JTextArea getTxtPublicKey() {
        return txtPublicKey;
    }

    public void setTxtPublicKey(JTextArea txtPublicKey) {
        this.txtPublicKey = txtPublicKey;
    }

    public JTextArea getTxtPrivateKey() {
        return txtPrivateKey;
    }

    public void setTxtPrivateKey(JTextArea txtPrivateKey) {
        this.txtPrivateKey = txtPrivateKey;
    }

    public JButton getGenKeyBtn() {
        return GenKeyBtn;
    }

    public void setGenKeyBtn(JButton genKeyBtn) {
        GenKeyBtn = genKeyBtn;
    }

    public JButton getLoadPublicKeyBtn() {
        return LoadPublicKeyBtn;
    }

    public void setLoadPublicKeyBtn(JButton loadKeyBtn) {
        LoadPublicKeyBtn = loadKeyBtn;
    }

    public JButton getSavePublicKeyBtn() {
        return SavePublicKeyBtn;
    }

    public void setSavePublicKeyBtn(JButton saveKeyBtn) {
        SavePublicKeyBtn = saveKeyBtn;
    }

    public JButton getLoadPrivateKeyBtn() { return LoadPrivateKeyBtn; }

    public void setLoadPrivateKeyBtn(JButton loadPrivateKeyBtn) { LoadPrivateKeyBtn = loadPrivateKeyBtn; }

    public JButton getSavePrivateKeyBtn() { return SavePrivateKeyBtn; }

    public void setSavePrivateKeyBtn(JButton savePrivateKeyBtn) { SavePrivateKeyBtn = savePrivateKeyBtn; }

    public JTextArea getTxtInput() {
        return txtInput;
    }

    public void setTxtInput(JTextArea txtInput) {
        this.txtInput = txtInput;
    }

    public JTextArea getTxtOutput() {
        return txtOutput;
    }

    public void setTxtOutput(JTextArea txtOutput) {
        this.txtOutput = txtOutput;
    }

    public JButton getEncryptBtn() {
        return encryptBtn;
    }

    public void setEncryptBtn(JButton encryptBtn) {
        this.encryptBtn = encryptBtn;
    }

    public JButton getDecryptBtn() {
        return decryptBtn;
    }

    public void setDecryptBtn(JButton decryptBtn) {
        this.decryptBtn = decryptBtn;
    }

    public JButton getImportFileBtn() {
        return importFileBtn;
    }

    public void setImportFileBtn(JButton importFileBtn) {
        this.importFileBtn = importFileBtn;
    }

    public JButton getExportFileBtn() {
        return exportFileBtn;
    }

    public void setExportFileBtn(JButton exportFileBtn) {
        this.exportFileBtn = exportFileBtn;
    }
}

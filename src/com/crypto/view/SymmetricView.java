package com.crypto.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SymmetricView extends JPanel {
    JComboBox<String> cbAlgorithm, cbMode, cbPadding, cbKeySize;
    JTextField tfKey, tfIV;
    JTextArea txtInput, txtOutput;
    JButton encryptBtn, decryptBtn, genKeyBtn, genIVBtn, importKeyBtn, exportKeyBtn, importFileBtn, exportFileBtn;

    public SymmetricView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel Quản lý khóa ===
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(new TitledBorder("Quản lý khóa"));

        cbAlgorithm = new JComboBox<>(new String[]{"AES", "DES", "TripleDES"});
        cbMode = new JComboBox<>(new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        cbPadding = new JComboBox<>(new String[]{"PKCS5Padding", "NoPadding"});
        cbKeySize = new JComboBox<>(new String[]{"128 bits", "192 bits", "256 bits"});
        tfKey = new JTextField(15);
        tfIV = new JTextField(15);
        genKeyBtn = new JButton("Tạo khóa");
        genIVBtn = new JButton("Tạo IV");
        importKeyBtn = new JButton("Nhập khóa");
        exportKeyBtn = new JButton("Xuất khóa");

        JPanel row1 = new JPanel();
        row1.add(new JLabel("Giải thuật:"));
        row1.add(cbAlgorithm);
        row1.add(new JLabel("Mode:"));
        row1.add(cbMode);
        row1.add(new JLabel("Padding:"));
        row1.add(cbPadding);
        row1.add(new JLabel("Độ dài khóa:"));
        row1.add(cbKeySize);

        JPanel row2 = new JPanel();
        row2.add(new JLabel("Khóa (Key):"));
        row2.add(tfKey);
        row2.add(genKeyBtn);

        row2.add(new JLabel("IV:"));
        row2.add(tfIV);
        row2.add(genIVBtn);

        row2.add(importKeyBtn);
        row2.add(exportKeyBtn);

        topPanel.add(row1);
        topPanel.add(row2);

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
        encryptBtn = new JButton("Mã hóa >>");
        decryptBtn = new JButton("<< Giải mã");

        encryptBtn.setBackground(new Color(66, 133, 244));
        encryptBtn.setForeground(Color.white);
        decryptBtn.setBackground(new Color(244, 160, 0));
        decryptBtn.setForeground(Color.white);

        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);

        gbc.gridx = 1;
        gbc.weightx = 0.1;
        bottomPanel.add(buttonPanel, gbc);

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
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
    }

    public JComboBox<String> getCbAlgorithm() {
        return cbAlgorithm;
    }

    public void setCbAlgorithm(JComboBox<String> cbAlgorithm) {
        this.cbAlgorithm = cbAlgorithm;
    }

    public JComboBox<String> getCbMode() {
        return cbMode;
    }

    public void setCbMode(JComboBox<String> cbMode) {
        this.cbMode = cbMode;
    }

    public JComboBox<String> getCbPadding() {
        return cbPadding;
    }

    public void setCbPadding(JComboBox<String> cbPadding) {
        this.cbPadding = cbPadding;
    }

    public JComboBox<String> getCbKeySize() {
        return cbKeySize;
    }

    public void setCbKeySize(JComboBox<String> cbKeySize) {
        this.cbKeySize = cbKeySize;
    }

    public JTextField getTfKey() {
        return tfKey;
    }

    public void setTfKey(JTextField tfKey) {
        this.tfKey = tfKey;
    }

    public JTextField getTfIV() {
        return tfIV;
    }

    public void setTfIV(JTextField tfIV) {
        this.tfIV = tfIV;
    }

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

    public JButton getGenKeyBtn() {
        return genKeyBtn;
    }

    public void setGenKeyBtn(JButton genKeyBtn) {
        this.genKeyBtn = genKeyBtn;
    }

    public JButton getGenIVBtn() {
        return genIVBtn;
    }

    public void setGenIVBtn(JButton genIVBtn) {
        this.genIVBtn = genIVBtn;
    }

    public JButton getImportKeyBtn() {
        return importKeyBtn;
    }

    public void setImportKeyBtn(JButton importKeyBtn) {
        this.importKeyBtn = importKeyBtn;
    }

    public JButton getExportKeyBtn() {
        return exportKeyBtn;
    }

    public void setExportKeyBtn(JButton exportKeyBtn) {
        this.exportKeyBtn = exportKeyBtn;
    }

    public JButton getExportFileBtn() {
        return exportFileBtn;
    }

    public void setExportFileBtn(JButton exportFileBtn) {
        this.exportFileBtn = exportFileBtn;
    }

    public JButton getImportFileBtn() {
        return importFileBtn;
    }

    public void setImportFileBtn(JButton importFileBtn) {
        this.importFileBtn = importFileBtn;
    }
}

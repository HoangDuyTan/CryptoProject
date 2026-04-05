package com.crypto.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SignatureTab extends JPanel {
    // Top Panel
    JTextField txtPrivateKey, txtPublicKey;
    JButton GenKeyBtn, LoadPrivKeyBtn, LoadPubKeyBtn;

    // Bottom Left Panel
    JButton selectDocSignBtn, signDocBtn, saveSigBtn;
    JTextArea txtDocSign, txtSigOutput;
    JComboBox<String> cbHashAlgorithm;

    // Bottom Right Panel
    JButton selectDocVerifyBtn, loadSigBtn, verifySigBtn;
    JTextArea txtDocVerify, txtSigInput;

    public SignatureTab() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel Quản lý khóa ===
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(new TitledBorder("Quản lý khóa"));
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        gbcTop.insets = new Insets(5, 5, 5, 5);

        // Label
        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        topPanel.add(new JLabel("Private Key:"), gbcTop);
        gbcTop.gridx = 1;
        topPanel.add(new JLabel("Public Key:"), gbcTop);

        // Text field
        txtPrivateKey = new JTextField();
        txtPublicKey = new JTextField();
        gbcTop.gridy = 1;
        gbcTop.gridx = 0;
        gbcTop.weightx = 0.5;
        topPanel.add(txtPrivateKey, gbcTop);
        gbcTop.gridx = 1;
        gbcTop.weightx = 0.5;
        topPanel.add(txtPublicKey, gbcTop);

        // Button
        GenKeyBtn = new JButton("Tạo khóa");
        LoadPrivKeyBtn = new JButton("Tải Private Key");
        LoadPubKeyBtn = new JButton("Tải Public Key");
        JPanel topBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topBtnPanel.add(GenKeyBtn);
        topBtnPanel.add(LoadPrivKeyBtn);
        topBtnPanel.add(LoadPubKeyBtn);
        gbcTop.gridx = 2;
        gbcTop.weightx = 0;
        topPanel.add(topBtnPanel, gbcTop);

        // === Panel Ký/ Xác minh tài liệu ===
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 15, 0));

        // Panel Ký tài liệu
        JPanel bottomLeftPanel = new JPanel(new GridBagLayout());
        bottomLeftPanel.setBorder(new TitledBorder("Ký tài liệu"));
        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.insets = new Insets(5, 5, 5, 5);
        gbcL.gridx = 0;
        gbcL.weightx = 1.0;
        gbcL.anchor = GridBagConstraints.WEST;

        selectDocSignBtn = new JButton("Chọn tài liệu");
        gbcL.gridy = 0;
        gbcL.weighty = 0;
        gbcL.fill = GridBagConstraints.NONE;
        bottomLeftPanel.add(selectDocSignBtn, gbcL);

        gbcL.gridy = 1;
        bottomLeftPanel.add(new JLabel("Nội dung tài liệu"), gbcL);

        txtDocSign = new JTextArea();
        txtDocSign.setLineWrap(true);
        txtDocSign.setWrapStyleWord(true);
        gbcL.gridy = 2;
        gbcL.weighty = 0.5;
        gbcL.fill = GridBagConstraints.BOTH;
        bottomLeftPanel.add(new JScrollPane(txtDocSign), gbcL);

        gbcL.gridy = 3;
        gbcL.weighty = 0;
        gbcL.fill = GridBagConstraints.NONE;
        bottomLeftPanel.add(new JLabel("Thuật toán Hash"), gbcL);

        JPanel signActionPanel = new JPanel(new BorderLayout());
        cbHashAlgorithm = new JComboBox<>(new String[]{"SHA-256", "SHA-512", "MD5"});
        signDocBtn = new JButton("Ký tài liệu");
        signDocBtn.setBackground(new Color(66, 133, 244));
        signDocBtn.setForeground(Color.white);
        signActionPanel.add(cbHashAlgorithm, BorderLayout.CENTER);
        signActionPanel.add(signDocBtn, BorderLayout.EAST);

        gbcL.gridy = 4;
        gbcL.fill = GridBagConstraints.HORIZONTAL;
        bottomLeftPanel.add(signActionPanel, gbcL);

        gbcL.gridy = 5;
        gbcL.fill = GridBagConstraints.NONE;
        bottomLeftPanel.add(new JLabel("Chữ ký (Base64)"), gbcL);

        txtSigOutput = new JTextArea();
        txtSigOutput.setLineWrap(true);
        txtSigOutput.setWrapStyleWord(true);
        txtSigOutput.setEditable(false);
        gbcL.gridy = 6;
        gbcL.weighty = 0.5;
        gbcL.fill = GridBagConstraints.BOTH;
        bottomLeftPanel.add(new JScrollPane(txtSigOutput), gbcL);

        saveSigBtn = new JButton("Lưu chữ ký");
        gbcL.gridy = 7;
        gbcL.weighty = 0;
        gbcL.fill = GridBagConstraints.NONE;
        bottomLeftPanel.add(saveSigBtn, gbcL);

        bottomPanel.add(bottomLeftPanel);

        // Panel Xác minh tài liệu
        JPanel bottomRightPanel = new JPanel(new GridBagLayout());
        bottomRightPanel.setBorder(new TitledBorder("Xác minh tài liệu"));
        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.insets = new Insets(5, 5, 5, 5);
        gbcR.gridx = 0;
        gbcR.weightx = 1.0;
        gbcR.anchor = GridBagConstraints.WEST;

        selectDocVerifyBtn = new JButton("Chọn tài liệu");
        gbcR.gridy = 0;
        gbcR.weighty = 0;
        gbcR.fill = GridBagConstraints.NONE;
        bottomRightPanel.add(selectDocVerifyBtn, gbcR);

        gbcR.gridy = 1;
        bottomRightPanel.add(new JLabel("Nội dung tài liệu gốc"), gbcR);

        txtDocVerify = new JTextArea();
        txtDocVerify.setLineWrap(true);
        txtDocVerify.setWrapStyleWord(true);
        gbcR.gridy = 2;
        gbcR.weighty = 0.5;
        gbcR.fill = GridBagConstraints.BOTH;
        bottomRightPanel.add(new JScrollPane(txtDocVerify), gbcR);

        gbcR.gridy = 3;
        gbcR.weighty = 0;
        gbcR.fill = GridBagConstraints.NONE;
        bottomRightPanel.add(new JLabel("Chữ ký cần xác minh"), gbcR);

        txtSigInput = new JTextArea();
        txtSigInput.setLineWrap(true);
        txtSigInput.setWrapStyleWord(true);
        gbcR.gridy = 4;
        gbcR.weighty = 0.5;
        gbcR.fill = GridBagConstraints.BOTH;
        bottomRightPanel.add(new JScrollPane(txtSigInput), gbcR);

        loadSigBtn = new JButton("Tải chữ ký");
        gbcR.gridy = 5;
        gbcR.weighty = 0;
        gbcR.fill = GridBagConstraints.NONE;
        bottomRightPanel.add(loadSigBtn, gbcR);

        verifySigBtn = new JButton("Xác minh chữ ký");
        verifySigBtn.setBackground(new Color(244, 160, 0));
        verifySigBtn.setForeground(Color.white);
        gbcR.gridy = 6;
        bottomRightPanel.add(verifySigBtn, gbcR);

        bottomPanel.add(bottomRightPanel);

        // === Thêm hết vào ===
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
    }
}

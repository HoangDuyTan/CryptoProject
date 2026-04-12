package com.crypto.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class AsymmetricView extends JPanel {
    JTextArea txtPublicKey, txtPrivateKey;
    JComboBox<String> cbKeySize;
    JButton GenRSAKeyBtn, LoadKeyBtn, SaveKeyBtn;

    JTextArea txtInput, txtOutput;
    JButton encryptBtn, decryptBtn, importFileBtn, exportFileBtn;

    public AsymmetricView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === RSA Panel ===
        JPanel topPanel = new JPanel(new GridLayout(2, 2, 10, 0));
        topPanel.setBorder(new TitledBorder("Cặp khóa RSA"));

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

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cbKeySize = new JComboBox<>(new String[]{"1024 bits", "2048 bits", "4096 bits"});
        GenRSAKeyBtn = new JButton("Tạo cặp khóa RSA");
        LoadKeyBtn = new JButton("Tải khóa");
        SaveKeyBtn = new JButton("Lưu khóa");
        btnPanel.add(cbKeySize);
        btnPanel.add(GenRSAKeyBtn);
        btnPanel.add(LoadKeyBtn);
        btnPanel.add(SaveKeyBtn);

        topPanel.add(publicKeyPanel);
        topPanel.add(privateKeyPanel);
        topPanel.add(btnPanel);

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
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
    }
}

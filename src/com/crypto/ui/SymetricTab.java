package com.crypto.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SymetricTab extends JPanel {
    JComboBox<String> cbAlgorithm, cbMode, cbPadding, cbKeyLength;
    JTextField tfKey, tfIV;
    JTextArea txtInput, txtOutput;
    JButton encryptBtn, decryptBtn, genKeyBtn, importKeyBtn, exportKeyBtn;

    public SymetricTab() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel Quản lý khóa ===
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        topPanel.setBorder(new TitledBorder("Quản lý khóa"));

        cbAlgorithm = new JComboBox<>(new String[]{"AES", "DES", "TripleDES"});
        cbMode = new JComboBox<>(new String[]{"ECB", "CBC", "CFB", "OFB", "CTR"});
        cbPadding = new JComboBox<>(new String[]{"PKCS5Padding", "NoPadding"});
        cbKeyLength = new JComboBox<>(new String[]{"128 bits", "192 bits", "256 bits"});
        tfKey = new JTextField(15);
        tfIV = new JTextField(15);
        genKeyBtn = new JButton("Tạo khóa");
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
        row1.add(cbKeyLength);

        JPanel row2 = new JPanel();
        row2.add(new JLabel("Khóa (Key):"));
        row2.add(tfKey);
        row2.add(new JLabel("IV:"));
        row2.add(tfIV);
        row2.add(genKeyBtn);
        row2.add(importKeyBtn);
        row2.add(exportKeyBtn);

        topPanel.add(row1);
        topPanel.add(row2);

        // === Panel Xử lý dữ liệu ===
        JPanel bottomPanel = new JPanel(new GridBagLayout());
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

        gbc.gridx = 2;
        gbc.weightx = 0.45;
        bottomPanel.add(outputPanel, gbc);

        // === Thêm hết vào ===
        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);
    }
}

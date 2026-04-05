package com.crypto.ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BasicAlgorithmTab extends JPanel {
    private JComboBox<String> cbAlgorithm, cbAlphabet;
    private JTextField tfKey;
    private JTextArea txtInput, txtOutput;
    private JButton encryptBtn, decryptBtn;

    public BasicAlgorithmTab() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel Cấu hình giải thuật ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBorder(new TitledBorder("Cấu hình Giải thuật"));

        cbAlgorithm = new JComboBox<>(new String[]{"Caesar", "Vigenere", "Affine", "Substitution", "Hill", "Playfair"});
        cbAlphabet = new JComboBox<>(new String[]{"Tiếng Việt", "Tiếng Anh"});
        tfKey = new JTextField(15);

        topPanel.add(new JLabel("Giải thuật:"));
        topPanel.add(cbAlgorithm);
        topPanel.add(new JLabel("Bảng chữ cái:"));
        topPanel.add(cbAlphabet);
        topPanel.add(new JLabel("Khóa (Key):"));
        topPanel.add(tfKey);

        // === Panel Mã hóa/ Giải mã ===
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
        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 0, 20));
        encryptBtn = new JButton("Mã hóa >>");
        decryptBtn = new JButton("<< Giải mã");

        encryptBtn.setBackground(new Color(66, 133, 244));
        encryptBtn.setForeground(Color.white);
        decryptBtn.setBackground(new Color(244, 160, 0));
        decryptBtn.setForeground(Color.white);

        btnPanel.add(encryptBtn);
        btnPanel.add(decryptBtn);

        gbc.gridx = 1;
        gbc.weightx = 0.1;
        bottomPanel.add(btnPanel, gbc);

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

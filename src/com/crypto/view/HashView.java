package com.crypto.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class HashView extends JPanel {
    JComboBox<String> cbAlgorithm, cbOutputFormat;
    JTextArea txtInput, txtOutput;
    JButton hashBtn, importFileBtn;

    public HashView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel Cấu hình Hash ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new TitledBorder("Cấu hình Hash"));

        cbAlgorithm = new JComboBox<>(new String[]{"MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512"});
        cbOutputFormat = new JComboBox<>(new String[]{"Hex", "Base64"});

        topPanel.add(new JLabel("Giải thuật:"));
        topPanel.add(cbAlgorithm);
        topPanel.add(new JLabel("Định dạng đầu ra:"));
        topPanel.add(cbOutputFormat);

        // === Panel Băm dữ liệu ===
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBorder(new TitledBorder("Băm dữ liệu"));
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
        inputPanel.add(txtInput, BorderLayout.CENTER);

        importFileBtn = new JButton("Chọn File");
        inputPanel.add(importFileBtn, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.weightx = 0.45;
        bottomPanel.add(new JScrollPane(inputPanel), gbc);

        // Button
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        hashBtn = new JButton("Băm dữ liệu >>");
        hashBtn.setBackground(new Color(66, 133, 244));
        hashBtn.setForeground(Color.white);
        hashBtn.setPreferredSize(new Dimension(130, 35));
        buttonPanel.add(hashBtn);

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

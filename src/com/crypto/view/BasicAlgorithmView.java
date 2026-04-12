package com.crypto.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BasicAlgorithmView extends JPanel {
    private JComboBox<String> cbAlgorithm, cbAlphabet;
    private JTextField tfKey;
    private JTextArea txtInput, txtOutput;
    private JButton encryptBtn, decryptBtn;

    public BasicAlgorithmView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // === Panel Cấu hình giải thuật ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        topPanel.setBorder(new TitledBorder("Cấu hình Giải thuật"));

        cbAlgorithm = new JComboBox<>(new String[]{"Caesar", "Vigenere", "Affine", "Substitution", "Hill", "Transposition"});
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

    public JButton getDecryptBtn() {
        return decryptBtn;
    }

    public void setDecryptBtn(JButton decryptBtn) {
        this.decryptBtn = decryptBtn;
    }

    public JButton getEncryptBtn() {
        return encryptBtn;
    }

    public void setEncryptBtn(JButton encryptBtn) {
        this.encryptBtn = encryptBtn;
    }

    public JTextArea getTxtOutput() {
        return txtOutput;
    }

    public void setTxtOutput(JTextArea txtOutput) {
        this.txtOutput = txtOutput;
    }

    public JTextArea getTxtInput() {
        return txtInput;
    }

    public void setTxtInput(JTextArea txtInput) {
        this.txtInput = txtInput;
    }

    public JTextField getTfKey() {
        return tfKey;
    }

    public void setTfKey(JTextField tfKey) {
        this.tfKey = tfKey;
    }

    public JComboBox<String> getCbAlphabet() {
        return cbAlphabet;
    }

    public void setCbAlphabet(JComboBox<String> cbAlphabet) {
        this.cbAlphabet = cbAlphabet;
    }

    public JComboBox<String> getCbAlgorithm() {
        return cbAlgorithm;
    }

    public void setCbAlgorithm(JComboBox<String> cbAlgorithm) {
        this.cbAlgorithm = cbAlgorithm;
    }
}

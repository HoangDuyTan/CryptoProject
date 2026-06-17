package com.crypto.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class SignatureView extends JPanel {
    public JTextField txtPrivateKeyPath;
    public JButton btnBrowsePrivKey;
    public JTextArea txtHashInput;
    public JTextArea txtSigOutput;
    public JButton btnSignOrder;
    public JButton btnCopySignature;
    public JButton btnGenerateKeys, btnSaveKeys;
    public JTextArea txtPublicKey, txtPrivateKey;

    public SignatureView() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // PANEL TẠO BỘ KHÓA MỚI
        JPanel keyGenPanel = new JPanel(new BorderLayout());
        keyGenPanel.setBorder(new TitledBorder("Phát sinh bộ khóa mới (Dành cho người dùng mới)"));

        btnGenerateKeys = new JButton("Tạo bộ khóa (Public / Private Key)");
        keyGenPanel.add(btnGenerateKeys,  BorderLayout.NORTH);

        btnSaveKeys = new JButton("Lưu bộ khóa (Public / Private Key)");
        keyGenPanel.add(btnSaveKeys, BorderLayout.SOUTH);

        JPanel keyPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel publicKeyPanel = new JPanel(new BorderLayout());
        publicKeyPanel.add(new JLabel("Public Key"), BorderLayout.NORTH);
        txtPublicKey = new JTextArea(5, 20);
        txtPublicKey.setLineWrap(true);
        txtPublicKey.setWrapStyleWord(true);
        txtPublicKey.setEditable(false);
        publicKeyPanel.add(new JScrollPane(txtPublicKey), BorderLayout.CENTER);

        JPanel privateKeyPanel = new JPanel(new BorderLayout());
        privateKeyPanel.add(new JLabel("Private Key"), BorderLayout.NORTH);
        txtPrivateKey = new JTextArea(5, 20);
        txtPrivateKey.setLineWrap(true);
        txtPrivateKey.setWrapStyleWord(true);
        txtPrivateKey.setEditable(false);
        privateKeyPanel.add(new JScrollPane(txtPrivateKey), BorderLayout.CENTER);

        keyPanel.add(publicKeyPanel);
        keyPanel.add(privateKeyPanel);
        keyGenPanel.add(keyPanel,  BorderLayout.CENTER);

        // PANEL CẤU HÌNH KHÓA BÍ MẬT
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(new TitledBorder("1. Cấu hình Khóa cá nhân để ký"));
        GridBagConstraints gbcTop = new GridBagConstraints();
        gbcTop.fill = GridBagConstraints.HORIZONTAL;
        gbcTop.insets = new Insets(8, 8, 8, 8);

        gbcTop.gridx = 0;
        gbcTop.gridy = 0;
        gbcTop.weightx = 0;
        topPanel.add(new JLabel("Chọn Private Key để ký (.pem/.txt): "), gbcTop);

        txtPrivateKeyPath = new JTextField();
        txtPrivateKeyPath.setEditable(false);
        gbcTop.gridx = 1;
        gbcTop.weightx = 1.0;
        topPanel.add(txtPrivateKeyPath, gbcTop);

        btnBrowsePrivKey = new JButton("Chọn Private Key");
        gbcTop.gridx = 2;
        gbcTop.weightx = 0;
        topPanel.add(btnBrowsePrivKey, gbcTop);


        // ====== PANEL KÝ ĐƠN HÀNG ======
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(new TitledBorder("2. Thực hiện Ký số đơn hàng"));
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.insets = new Insets(8, 8, 8, 8);
        gbcC.fill = GridBagConstraints.BOTH;
        gbcC.gridx = 0;
        gbcC.weightx = 1.0;

        gbcC.gridy = 0;
        gbcC.weighty = 0;
        centerPanel.add(new JLabel("Dán mã băm Đơn hàng (Hash Value lấy từ Website):"), gbcC);

        txtHashInput = new JTextArea(3, 20);
        txtHashInput.setLineWrap(true);
        txtHashInput.setWrapStyleWord(true);
        txtHashInput.setToolTipText("Ví dụ: 9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08");
        gbcC.gridy = 1;
        gbcC.weighty = 0.3;
        centerPanel.add(new JScrollPane(txtHashInput), gbcC);

        btnSignOrder = new JButton("TẠO CHỮ KÝ SỐ ĐƠN HÀNG");
        btnSignOrder.setBackground(new Color(66, 133, 244));
        btnSignOrder.setForeground(Color.white);
        btnSignOrder.setPreferredSize(new Dimension(0, 40));
        gbcC.gridy = 2;
        gbcC.weighty = 0;
        centerPanel.add(btnSignOrder, gbcC);

        // Chữ ký đầu ra
        gbcC.gridy = 3;
        centerPanel.add(new JLabel("Chữ ký điện tử kết quả (Base64):"), gbcC);

        txtSigOutput = new JTextArea(5, 20);
        txtSigOutput.setLineWrap(true);
        txtSigOutput.setWrapStyleWord(true);
        txtSigOutput.setEditable(false);
        txtSigOutput.setBackground(new Color(245, 245, 245));
        gbcC.gridy = 4;
        gbcC.weighty = 0.5;
        centerPanel.add(new JScrollPane(txtSigOutput), gbcC);

        btnCopySignature = new JButton("Sao chép chữ ký vào Bộ nhớ tạm (Clipboard)");
        btnCopySignature.setEnabled(false);
        gbcC.gridy = 5;
        gbcC.weighty = 0;
        centerPanel.add(btnCopySignature, gbcC);

        // ======= Thêm hết vào ======
        JPanel northWrapper = new JPanel(new BorderLayout());
        northWrapper.add(keyGenPanel, BorderLayout.NORTH);
        northWrapper.add(topPanel, BorderLayout.CENTER);

        add(northWrapper, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public JTextField getTxtPrivateKeyPath() {
        return txtPrivateKeyPath;
    }

    public void setTxtPrivateKeyPath(JTextField txtPrivateKeyPath) {
        this.txtPrivateKeyPath = txtPrivateKeyPath;
    }

    public JButton getBtnBrowsePrivKey() {
        return btnBrowsePrivKey;
    }

    public void setBtnBrowsePrivKey(JButton btnBrowsePrivKey) {
        this.btnBrowsePrivKey = btnBrowsePrivKey;
    }

    public JTextArea getTxtHashInput() {
        return txtHashInput;
    }

    public void setTxtHashInput(JTextArea txtHashInput) {
        this.txtHashInput = txtHashInput;
    }

    public JTextArea getTxtSigOutput() {
        return txtSigOutput;
    }

    public void setTxtSigOutput(JTextArea txtSigOutput) {
        this.txtSigOutput = txtSigOutput;
    }

    public JButton getBtnSignOrder() {
        return btnSignOrder;
    }

    public void setBtnSignOrder(JButton btnSignOrder) {
        this.btnSignOrder = btnSignOrder;
    }

    public JButton getBtnCopySignature() {
        return btnCopySignature;
    }

    public void setBtnCopySignature(JButton btnCopySignature) {
        this.btnCopySignature = btnCopySignature;
    }

    public JButton getBtnGenerateKeys() { return btnGenerateKeys; }

    public void setBtnGenerateKeys(JButton btnGenerateKeys) { this.btnGenerateKeys = btnGenerateKeys; }

    public JButton getBtnSaveKeys() { return btnSaveKeys; }

    public void setBtnSaveKeys(JButton btnSaveKeys) { this.btnSaveKeys = btnSaveKeys; }

    public JTextArea getTxtPublicKey() { return txtPublicKey; }

    public void setTxtPublicKey(JTextArea txtPublicKey) { this.txtPublicKey = txtPublicKey; }

    public JTextArea getTxtPrivateKey() { return txtPrivateKey; }

    public void setTxtPrivateKey(JTextArea txtPrivateKey) { this.txtPrivateKey = txtPrivateKey; }
}
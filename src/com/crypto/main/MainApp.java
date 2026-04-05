package com.crypto.main;

import com.crypto.ui.BasicAlgorithmTab;
import com.crypto.ui.SymetricTab;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {
    public static JLabel statusBar;

    public MainApp() {
        setTitle("Ứng dụng Encryption");
        setSize(1050, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Thanh Navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));

        tabbedPane.addTab("Giải thuật cơ bản", new BasicAlgorithmTab());
        tabbedPane.addTab("Mã hóa đối xứng", new SymetricTab());
        tabbedPane.addTab("Mã hoá bất đối xứng", null);
        tabbedPane.addTab("Hash", null);
        tabbedPane.addTab("Chữ ký điện tử", null);

        // Thanh Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        statusBar = new JLabel("Trạng thái: Sẵn sàng...");
        statusBar.setFont(new Font("Arial", Font.ITALIC, 16));
        statusBar.setBackground(Color.DARK_GRAY);
        statusPanel.add(statusBar);

        // Thêm hết vào
        add(tabbedPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void updateStatus(String status) {
        if (status != null) {
            statusBar.setText("Trạng thái: " + status);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
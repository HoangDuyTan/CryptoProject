package com.crypto.main;

import com.crypto.controller.ClassicController;
import com.crypto.controller.SymmetricController;
import com.crypto.view.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.swing.*;
import java.awt.*;
import java.security.Security;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("Ứng dụng Encryption");
        setSize(1050, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Thanh Navigation
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 20));

        BasicAlgorithmView basicAlgorithmView = new BasicAlgorithmView();
        new ClassicController(basicAlgorithmView);

        SymmetricView symmetricView = new SymmetricView();
        new SymmetricController(symmetricView);

        tabbedPane.addTab("Giải thuật cơ bản", basicAlgorithmView);
        tabbedPane.addTab("Mã hóa đối xứng", symmetricView);
        tabbedPane.addTab("Mã hoá bất đối xứng", new AsymmetricView());
        tabbedPane.addTab("Hash", new HashView());
        tabbedPane.addTab("Chữ ký điện tử", new SignatureView());

        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        Security.addProvider(new BouncyCastleProvider());
        SwingUtilities.invokeLater(MainApp::new);
    }
}
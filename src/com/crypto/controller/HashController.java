package com.crypto.controller;

import com.crypto.model.hash.HashModel;
import com.crypto.view.HashView;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class HashController {
    HashView view;
    HashModel model;

    public HashController(HashView view) {
        this.view = view;
        this.model = new HashModel();
        initController();
    }

    private void initController() {
        view.getHashBtn().addActionListener(e -> process());

        view.getImportFileBtn().addActionListener(e -> importFromFile(view.getTxtInput()));
        view.getExportFileBtn().addActionListener(e -> exportToFile(view.getTxtOutput(), ".txt"));
    }

    private void process() {
        String input = view.getTxtInput().getText();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Vui lòng nhập văn bản hoặc cho file đầu vào!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String algorithm = view.getCbAlgorithm().getSelectedItem().toString();
            String format = view.getCbOutputFormat().getSelectedItem().toString();
            String result;

            File file = new File(input);
            if (file.isFile()) {
                result = model.hashFile(input, algorithm, format);
            } else {
                result = model.hashText(input, algorithm, format);
            }

            view.getTxtOutput().setText(result);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importFromFile(JTextComponent des) {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                String fileName = file.getName();

                if (fileName.endsWith(".txt")) {
                    String content = new String(Files.readAllBytes(file.toPath()));
                    des.setText(content);
                } else {
                    des.setText(file.getAbsolutePath());
                }

                JOptionPane.showMessageDialog(view, "Đọc thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi đọc file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exportToFile(JTextComponent src, String ext) {
        if (src.getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Không có dữ liệu để lưu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                file = new File(file.getAbsolutePath() + ext);

                if (file.exists()) {
                    JOptionPane.showMessageDialog(view, "File này đã tồn tại! Vui lòng chọn một tên khác để tạo file mới", "Từ chối ghi đè", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Files.write(file.toPath(), src.getText().getBytes(), StandardOpenOption.CREATE_NEW);
                JOptionPane.showMessageDialog(view, "Lưu file thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Lỗi lưu file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

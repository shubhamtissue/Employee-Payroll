// frames/ExitFrame.java
package payroll.frames;

import payroll.NavigatorManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitFrame extends JFrame {
    private NavigatorManager navigationManager;

    public ExitFrame(NavigatorManager manager) {
        this.navigationManager = manager;
        initializeUI();
        setupWindowListener();
    }

    private void initializeUI() {
        setTitle("Exit - Payroll System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Exit Application", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(205, 92, 92));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel messageLabel = new JLabel(
                "<html><center>Are you sure you want to exit the Payroll Management System?<br><br>" +
                        "All unsaved data will be lost.</center></html>",
                JLabel.CENTER
        );
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel iconLabel = new JLabel(new ImageIcon("warning_icon.png")); // You can add an icon
        iconLabel.setHorizontalAlignment(JLabel.CENTER);

        messagePanel.add(iconLabel, BorderLayout.NORTH);
        messagePanel.add(messageLabel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton yesButton = new JButton("Yes, Exit");
        JButton noButton = new JButton("No, Go Back");

        yesButton.setBackground(new Color(205, 92, 92));
        yesButton.setForeground(Color.WHITE);
        yesButton.setFont(new Font("Arial", Font.BOLD, 14));

        noButton.setBackground(new Color(70, 130, 180));
        noButton.setForeground(Color.WHITE);
        noButton.setFont(new Font("Arial", Font.BOLD, 14));

        yesButton.addActionListener(e -> navigationManager.exitApplication());
        noButton.addActionListener(e -> navigationManager.showHomeFrame());

        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        // Add components to frame
        add(headerLabel, BorderLayout.NORTH);
        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                navigationManager.showHomeFrame();
            }
        });
    }
}
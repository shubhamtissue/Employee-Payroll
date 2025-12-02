// frames/HomeFrame.java
package payroll.frames;

import payroll.NavigatorManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HomeFrame extends JFrame {
    private NavigatorManager navigationManager;

    public HomeFrame(NavigatorManager manager) {
        this.navigationManager = manager;
        initializeUI();
        setupWindowListener();
    }

    private void initializeUI() {
        setTitle("Payroll Management System - Home");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Payroll Management System", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 28));
        headerLabel.setForeground(new Color(0, 70, 140));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Main content panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(240, 245, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.weightx = 1.0;

        // Create navigation buttons
        JButton addEmployeeBtn = createStyledButton("Add New Employee", new Color(70, 130, 180));
        JButton viewEmployeesBtn = createStyledButton("View Employees", new Color(60, 179, 113));
        JButton generatePayslipBtn = createStyledButton("Generate Payslip", new Color(218, 165, 32));
        JButton exitBtn = createStyledButton("Exit Application", new Color(205, 92, 92));

        // Add action listeners
        addEmployeeBtn.addActionListener(e -> navigationManager.showAddEmployeeFrame());
        viewEmployeesBtn.addActionListener(e -> navigationManager.showViewEmployeesFrame());
        generatePayslipBtn.addActionListener(e -> navigationManager.showGeneratePayslipFrame());
        exitBtn.addActionListener(e -> navigationManager.showExitFrame());

        // Add buttons to panel
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(addEmployeeBtn, gbc);

        gbc.gridy = 1;
        mainPanel.add(viewEmployeesBtn, gbc);

        gbc.gridy = 2;
        mainPanel.add(generatePayslipBtn, gbc);

        gbc.gridy = 3;
        mainPanel.add(exitBtn, gbc);

        // Footer
        JLabel footerLabel = new JLabel("Software House Payroll System v1.0", JLabel.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(Color.GRAY);
        footerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to frame
        add(headerLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerLabel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(backgroundColor.darker(), 2),
                BorderFactory.createEmptyBorder(12, 25, 12, 25)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                navigationManager.exitApplication();
            }
        });
    }
}
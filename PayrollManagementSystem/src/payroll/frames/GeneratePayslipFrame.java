// frames/GeneratePayslipFrame.java
package payroll.frames;

import payroll.NavigatorManager;
import payroll.model.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

public class GeneratePayslipFrame extends JFrame {
    private NavigatorManager navigationManager;
    private PayrollManager payrollManager;

    private JComboBox<Integer> employeeIdCombo;
    private JComboBox<String> monthCombo;
    private JTextField yearField;
    private JTextField overtimeHoursField;
    private JTextField bonusField;
    private JTextField extraDeductionsField;
    private JTextField hoursWorkedField;
    private JPanel contractPanel;

    public GeneratePayslipFrame(NavigatorManager manager, PayrollManager payrollManager) {
        this.navigationManager = manager;
        this.payrollManager = payrollManager;
        initializeUI();
        setupWindowListener();
        loadEmployeeIds();
    }

    private void initializeUI() {
        setTitle("Generate Payslip - Payroll System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Generate Employee Payslip", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20),
                "Payslip Information",
                TitledBorder.CENTER,
                TitledBorder.TOP
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Employee ID
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Employee ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        employeeIdCombo = new JComboBox<>();
        employeeIdCombo.addActionListener(new EmployeeSelectionListener());
        formPanel.add(employeeIdCombo, gbc);

        // Month
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Month:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        String[] months = {"January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"};
        monthCombo = new JComboBox<>(months);
        // Set current month as default
        Calendar cal = Calendar.getInstance();
        monthCombo.setSelectedIndex(cal.get(Calendar.MONTH));
        formPanel.add(monthCombo, gbc);

        // Year
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Year:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        yearField = new JTextField(String.valueOf(cal.get(Calendar.YEAR)));
        formPanel.add(yearField, gbc);

        // Overtime hours
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Overtime Hours:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        overtimeHoursField = new JTextField("0");
        formPanel.add(overtimeHoursField, gbc);

        // Bonus
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Bonus:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        bonusField = new JTextField("0");
        formPanel.add(bonusField, gbc);

        // Extra deductions
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Extra Deductions:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5;
        extraDeductionsField = new JTextField("0");
        formPanel.add(extraDeductionsField, gbc);

        // Contract-specific panel
        contractPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contractPanel.setVisible(false);
        contractPanel.add(new JLabel("Hours Worked:"));
        hoursWorkedField = new JTextField("0", 10);
        contractPanel.add(hoursWorkedField);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        formPanel.add(contractPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generateButton = new JButton("Generate Payslip");
        JButton clearButton = new JButton("Clear");
        JButton backButton = new JButton("Back to Home");

        generateButton.addActionListener(new GeneratePayslipListener());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> navigationManager.showHomeFrame());

        buttonPanel.add(generateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        // Add components to frame
        add(headerLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEmployeeIds() {
        employeeIdCombo.removeAllItems();
        for (Employee employee : payrollManager.getAllEmployees()) {
            employeeIdCombo.addItem(employee.getId());
        }

        if (employeeIdCombo.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No employees found. Please add employees first.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class EmployeeSelectionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (employeeIdCombo.getSelectedItem() != null) {
                int employeeId = (Integer) employeeIdCombo.getSelectedItem();
                Employee employee = payrollManager.getEmployee(employeeId);

                // Show/hide contract-specific fields
                boolean isPartTime = employee instanceof PartTimeEmployee;
                contractPanel.setVisible(isPartTime);

                revalidate();
                repaint();
            }
        }
    }

    private class GeneratePayslipListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (employeeIdCombo.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(GeneratePayslipFrame.this,
                            "Please select an employee!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int employeeId = (Integer) employeeIdCombo.getSelectedItem();
                Employee employee = payrollManager.getEmployee(employeeId);

                if (employee == null) {
                    JOptionPane.showMessageDialog(GeneratePayslipFrame.this,
                            "Employee not found!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int month = monthCombo.getSelectedIndex() + 1;
                int year = Integer.parseInt(yearField.getText());
                double overtimeHours = Double.parseDouble(overtimeHoursField.getText());
                double bonus = Double.parseDouble(bonusField.getText());
                double extraDeductions = Double.parseDouble(extraDeductionsField.getText());

                // Set hours worked for part-time employees
                if (employee instanceof PartTimeEmployee) {
                    double hoursWorked = Double.parseDouble(hoursWorkedField.getText());
                    ((PartTimeEmployee) employee).setHoursWorked(hoursWorked);
                }

                // Generate payslip
                PayslipGenerator.PayslipData payslip = PayslipGenerator.generatePayslip(
                        employee, month, year, overtimeHours, bonus, extraDeductions
                );

                // Display payslip
                showPayslipDialog(payslip);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(GeneratePayslipFrame.this,
                        "Please enter valid numbers in all fields!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(GeneratePayslipFrame.this,
                        "Error generating payslip: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showPayslipDialog(PayslipGenerator.PayslipData payslip) {
        JDialog payslipDialog = new JDialog(this, "Employee Payslip", true);
        payslipDialog.setSize(500, 600);
        payslipDialog.setLocationRelativeTo(this);
        payslipDialog.setLayout(new BorderLayout(10, 10));

        JTextArea payslipText = new JTextArea(PayslipGenerator.formatPayslip(payslip));
        payslipText.setFont(new Font("Monospaced", Font.PLAIN, 12));
        payslipText.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(payslipText);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("Print");
        JButton closeButton = new JButton("Close");

        printButton.addActionListener(e -> {
            try {
                payslipText.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(payslipDialog,
                        "Error printing payslip: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        closeButton.addActionListener(e -> payslipDialog.dispose());

        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);

        payslipDialog.add(new JLabel("Employee Payslip", JLabel.CENTER), BorderLayout.NORTH);
        payslipDialog.add(scrollPane, BorderLayout.CENTER);
        payslipDialog.add(buttonPanel, BorderLayout.SOUTH);
        payslipDialog.setVisible(true);
    }

    private void clearForm() {
        overtimeHoursField.setText("0");
        bonusField.setText("0");
        extraDeductionsField.setText("0");
        hoursWorkedField.setText("0");
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
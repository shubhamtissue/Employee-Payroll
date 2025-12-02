// frames/AddEmployeeFrame.java
package payroll.frames;

import payroll.NavigatorManager;
import payroll.model.PayrollManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddEmployeeFrame extends JFrame {
    private NavigatorManager navigationManager;
    private PayrollManager payrollManager;

    private JComboBox<String> employeeTypeCombo;
    private JTextField nameField;
    private JTextField designationField;
    private JTextField salaryField;
    private JCheckBox hourlyCheckbox;
    private JPanel contractPanel;

    public AddEmployeeFrame(NavigatorManager manager, PayrollManager payrollManager) {
        this.navigationManager = manager;
        this.payrollManager = payrollManager;
        initializeUI();
        setupWindowListener();
    }

    private void initializeUI() {
        setTitle("Add New Employee - Payroll System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Add New Employee", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        // Employee Type
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Employee Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        String[] types = {"Full-Time Employee", "Part-Time Employee"};
        employeeTypeCombo = new JComboBox<>(types);
        employeeTypeCombo.addActionListener(new EmployeeTypeListener());
        formPanel.add(employeeTypeCombo, gbc);

        // Name
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        nameField = new JTextField(20);
        formPanel.add(nameField, gbc);

        // Designation
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Designation:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        designationField = new JTextField(20);
        formPanel.add(designationField, gbc);

        // Salary/Rate
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel salaryLabel = new JLabel("Basic Salary:");
        formPanel.add(salaryLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        salaryField = new JTextField(20);
        formPanel.add(salaryField, gbc);

        // Part-time specific panel
        contractPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contractPanel.setVisible(false);
        hourlyCheckbox = new JCheckBox("Hourly Rate (if unchecked, monthly rate)");
        contractPanel.add(hourlyCheckbox);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(contractPanel, gbc);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Add Employee");
        JButton clearButton = new JButton("Clear");
        JButton backButton = new JButton("Back to Home");

        addButton.addActionListener(new AddEmployeeListener());
        clearButton.addActionListener(e -> clearForm());
        backButton.addActionListener(e -> navigationManager.showHomeFrame());

        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        // Add components to frame
        add(headerLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class EmployeeTypeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedType = (String) employeeTypeCombo.getSelectedItem();
            boolean isPartTime = selectedType.equals("Part-Time Employee");

            // Update salary label
            JLabel salaryLabel = (JLabel) ((JPanel) getContentPane().getComponent(1)).getComponent(4);
            if (isPartTime) {
                salaryLabel.setText("Rate (Salary/Rate):");
            } else {
                salaryLabel.setText("Basic Salary:");
            }

            // Show/hide contract panel
            contractPanel.setVisible(isPartTime);

            revalidate();
            repaint();
        }
    }

    private class AddEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String name = nameField.getText().trim();
                String designation = designationField.getText().trim();
                String salaryText = salaryField.getText().trim();

                if (name.isEmpty() || designation.isEmpty() || salaryText.isEmpty()) {
                    JOptionPane.showMessageDialog(AddEmployeeFrame.this,
                            "Please fill in all required fields!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double salary = Double.parseDouble(salaryText);
                String selectedType = (String) employeeTypeCombo.getSelectedItem();
                int employeeId = -1;

                switch (selectedType) {
                    case "Full-Time Employee":
                        employeeId = payrollManager.addFullTimeEmployee(name, designation, salary);
                        break;
                    case "Part-Time Employee":
                        boolean hourly = hourlyCheckbox.isSelected();
                        employeeId = payrollManager.addPartTimeEmployee(name, designation, salary, hourly);
                        break;
                }

                if (employeeId != -1) {
                    JOptionPane.showMessageDialog(AddEmployeeFrame.this,
                            "Employee added successfully!\nEmployee ID: " + employeeId,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(AddEmployeeFrame.this,
                        "Please enter a valid salary amount!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(AddEmployeeFrame.this,
                        "Error adding employee: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        designationField.setText("");
        salaryField.setText("");
        hourlyCheckbox.setSelected(false);
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
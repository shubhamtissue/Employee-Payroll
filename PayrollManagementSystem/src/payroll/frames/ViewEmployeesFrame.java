// frames/ViewEmployeesFrame.java
package payroll.frames;

import payroll.NavigatorManager;
import payroll.model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewEmployeesFrame extends JFrame {
    private NavigatorManager navigationManager;
    private PayrollManager payrollManager;
    private JTable employeesTable;
    private DefaultTableModel tableModel;

    public ViewEmployeesFrame(NavigatorManager manager, PayrollManager payrollManager) {
        this.navigationManager = manager;
        this.payrollManager = payrollManager;
        initializeUI();
        loadEmployeesData();
        setupWindowListener();
    }

    private void initializeUI() {
        setTitle("View Employees - Payroll System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel headerLabel = new JLabel("Employee List", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Table setup
        String[] columns = {"ID", "Name", "Designation", "Type", "Basic Salary", "Gross Salary", "Net Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        employeesTable = new JTable(tableModel);
        employeesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employeesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        employeesTable.setRowHeight(25);
        employeesTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(employeesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton refreshButton = new JButton("Refresh");
        JButton editButton = new JButton("Edit Employee");
        JButton deleteButton = new JButton("Delete Employee");
        JButton backButton = new JButton("Back to Home");

        refreshButton.addActionListener(e -> loadEmployeesData());
        editButton.addActionListener(new EditEmployeeListener());
        deleteButton.addActionListener(new DeleteEmployeeListener());
        backButton.addActionListener(e -> navigationManager.showHomeFrame());

        buttonPanel.add(refreshButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        // Add components to frame
        add(headerLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEmployeesData() {
        tableModel.setRowCount(0);

        for (Employee employee : payrollManager.getAllEmployees()) {
            String type = getEmployeeType(employee);
            Object[] rowData = {
                    employee.getId(),
                    employee.getName(),
                    employee.getDesignation(),
                    type,
                    String.format("₹%.2f", employee.getBasicSalary()),
                    String.format("₹%.2f", employee.calculateGrossSalary()),
                    String.format("₹%.2f", employee.calculateNetSalary())
            };
            tableModel.addRow(rowData);
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "No employees found. Please add employees first.",
                    "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private String getEmployeeType(Employee employee) {
        if (employee instanceof FullTimeEmployee) return "Full-Time";
        if (employee instanceof PartTimeEmployee) return "Part-Time";
        return "Unknown";
    }

    private class EditEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = employeesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ViewEmployeesFrame.this,
                        "Please select an employee to edit!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int modelRow = employeesTable.convertRowIndexToModel(selectedRow);
            int employeeId = (Integer) tableModel.getValueAt(modelRow, 0);

            Employee employee = payrollManager.getEmployee(employeeId);
            if (employee != null) {
                showEditDialog(employee);
            }
        }
    }

    private class DeleteEmployeeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = employeesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(ViewEmployeesFrame.this,
                        "Please select an employee to delete!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int modelRow = employeesTable.convertRowIndexToModel(selectedRow);
            int employeeId = (Integer) tableModel.getValueAt(modelRow, 0);
            String employeeName = (String) tableModel.getValueAt(modelRow, 1);

            int confirm = JOptionPane.showConfirmDialog(
                    ViewEmployeesFrame.this,
                    "Are you sure you want to delete employee: " + employeeName + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                boolean deleted = payrollManager.removeEmployee(employeeId);
                if (deleted) {
                    JOptionPane.showMessageDialog(ViewEmployeesFrame.this,
                            "Employee deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployeesData();
                }
            }
        }
    }

    private void showEditDialog(Employee employee) {
        JDialog editDialog = new JDialog(this, "Edit Employee: " + employee.getName(), true);
        editDialog.setSize(500, 600);
        editDialog.setLocationRelativeTo(this);
        editDialog.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Basic info
        JTextField nameField = new JTextField(employee.getName());
        JTextField designationField = new JTextField(employee.getDesignation());
        JTextField basicSalaryField = new JTextField(String.valueOf(employee.getBasicSalary()));

        // Salary components
        SalaryComponents comp = employee.getSalaryComponents();
        JTextField hraField = new JTextField(String.valueOf(comp.getHra()));
        JTextField medicalField = new JTextField(String.valueOf(comp.getMedicalAllowance()));
        JTextField conveyanceField = new JTextField(String.valueOf(comp.getConveyance()));
        JTextField otherAllowancesField = new JTextField(String.valueOf(comp.getOtherAllowances()));
        JTextField overtimeRateField = new JTextField(String.valueOf(comp.getOvertimeRate()));
        JTextField pfPercentField = new JTextField(String.valueOf(comp.getPfPercent()));
        JTextField taxPercentField = new JTextField(String.valueOf(comp.getTaxPercent()));
        JTextField deductionsField = new JTextField(String.valueOf(comp.getDeductions()));

        // Full-time specific fields
        JTextField fixedBenefitsField = new JTextField("0");
        if (employee instanceof FullTimeEmployee) {
            fixedBenefitsField.setText(String.valueOf(((FullTimeEmployee) employee).getFixedBenefits()));
        }

        // Part-time specific fields
        JTextField hoursWorkedField = new JTextField("0");
        if (employee instanceof PartTimeEmployee) {
            hoursWorkedField.setText(String.valueOf(((PartTimeEmployee) employee).getHoursWorked()));
        }

        // Add fields to form
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Designation:"));
        formPanel.add(designationField);
        formPanel.add(new JLabel("Basic Salary:"));
        formPanel.add(basicSalaryField);

        formPanel.add(new JLabel("HRA:"));
        formPanel.add(hraField);
        formPanel.add(new JLabel("Medical Allowance:"));
        formPanel.add(medicalField);
        formPanel.add(new JLabel("Conveyance:"));
        formPanel.add(conveyanceField);
        formPanel.add(new JLabel("Other Allowances:"));
        formPanel.add(otherAllowancesField);
        formPanel.add(new JLabel("Overtime Rate:"));
        formPanel.add(overtimeRateField);
        formPanel.add(new JLabel("PF %:"));
        formPanel.add(pfPercentField);
        formPanel.add(new JLabel("Tax %:"));
        formPanel.add(taxPercentField);
        formPanel.add(new JLabel("Deductions:"));
        formPanel.add(deductionsField);

        // Add employee type specific fields
        if (employee instanceof FullTimeEmployee) {
            formPanel.add(new JLabel("Fixed Benefits:"));
            formPanel.add(fixedBenefitsField);
        }

        if (employee instanceof PartTimeEmployee) {
            formPanel.add(new JLabel("Hours Worked:"));
            formPanel.add(hoursWorkedField);
        }

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Update basic info
                employee.setName(nameField.getText());
                employee.setDesignation(designationField.getText());
                employee.setBasicSalary(Double.parseDouble(basicSalaryField.getText()));

                // Update salary components
                SalaryComponents newComponents = new SalaryComponents();
                newComponents.setHra(Double.parseDouble(hraField.getText()));
                newComponents.setMedicalAllowance(Double.parseDouble(medicalField.getText()));
                newComponents.setConveyance(Double.parseDouble(conveyanceField.getText()));
                newComponents.setOtherAllowances(Double.parseDouble(otherAllowancesField.getText()));
                newComponents.setOvertimeRate(Double.parseDouble(overtimeRateField.getText()));
                newComponents.setPfPercent(Double.parseDouble(pfPercentField.getText()));
                newComponents.setTaxPercent(Double.parseDouble(taxPercentField.getText()));
                newComponents.setDeductions(Double.parseDouble(deductionsField.getText()));

                payrollManager.updateEmployeeSalaryComponents(employee.getId(), newComponents);

                // Update type-specific fields
                if (employee instanceof FullTimeEmployee) {
                    ((FullTimeEmployee) employee).setFixedBenefits(Double.parseDouble(fixedBenefitsField.getText()));
                }

                if (employee instanceof PartTimeEmployee) {
                    ((PartTimeEmployee) employee).setHoursWorked(Double.parseDouble(hoursWorkedField.getText()));
                }

                JOptionPane.showMessageDialog(editDialog,
                        "Employee details updated successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                editDialog.dispose();
                loadEmployeesData(); // Refresh table

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editDialog,
                        "Please enter valid numbers in all fields!", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> editDialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        editDialog.add(new JLabel("Edit Employee Details", JLabel.CENTER), BorderLayout.NORTH);
        editDialog.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
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
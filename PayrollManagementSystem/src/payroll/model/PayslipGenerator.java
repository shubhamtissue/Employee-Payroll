// model/PayslipGenerator.java
package payroll.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

public class PayslipGenerator {

    public static class PayslipData {
        public final int employeeId;
        public final String employeeName;
        public final String designation;
        public final int month;
        public final int year;
        public final Map<String, Double> earnings;
        public final Map<String, Double> deductions;
        public final double grossSalary;
        public final double totalDeductions;
        public final double netSalary;

        public PayslipData(int employeeId, String employeeName, String designation, int month, int year,
                           Map<String, Double> earnings, Map<String, Double> deductions,
                           double grossSalary, double totalDeductions, double netSalary) {
            this.employeeId = employeeId;
            this.employeeName = employeeName;
            this.designation = designation;
            this.month = month;
            this.year = year;
            this.earnings = new LinkedHashMap<>(earnings);
            this.deductions = new LinkedHashMap<>(deductions);
            this.grossSalary = grossSalary;
            this.totalDeductions = totalDeductions;
            this.netSalary = netSalary;
        }
    }

    public static PayslipData generatePayslip(Employee employee, int month, int year,
                                              double overtimeHours, double bonus, double extraDeductions) {
        Map<String, Double> earnings = new LinkedHashMap<>();
        Map<String, Double> deductions = new LinkedHashMap<>();

        SalaryComponents comp = employee.getSalaryComponents();

        // Calculate earnings
        double basic = employee.getBasicSalary();
        earnings.put("Basic Salary", basic);

        if (employee instanceof FullTimeEmployee) {
            FullTimeEmployee ft = (FullTimeEmployee) employee;
            earnings.put("HRA", comp.getHra());
            earnings.put("Medical Allowance", comp.getMedicalAllowance());
            earnings.put("Conveyance", comp.getConveyance());
            earnings.put("Other Allowances", comp.getOtherAllowances());
            earnings.put("Fixed Benefits", ft.getFixedBenefits());
        }

        double overtimePay = overtimeHours * comp.getOvertimeRate();
        earnings.put("Overtime Pay", overtimePay);
        earnings.put("Bonus", bonus);

        // Calculate gross salary
        double grossSalary = 0.0;
        for (double amount : earnings.values()) {
            grossSalary += amount;
        }

        // Calculate deductions
        double pf = grossSalary * comp.getPfPercent() / 100.0;
        double taxable = grossSalary - pf;
        double tax = taxable * comp.getTaxPercent() / 100.0;

        deductions.put("Provident Fund (" + comp.getPfPercent() + "%)", pf);
        deductions.put("Income Tax (" + comp.getTaxPercent() + "%)", tax);
        deductions.put("Other Deductions", comp.getDeductions());
        deductions.put("Extra Deductions", extraDeductions);

        double totalDeductions = 0.0;
        for (double amount : deductions.values()) {
            totalDeductions += amount;
        }

        double netSalary = grossSalary - totalDeductions;

        return new PayslipData(
                employee.getId(), employee.getName(), employee.getDesignation(),
                month, year, earnings, deductions, grossSalary, totalDeductions, netSalary
        );
    }

    public static String formatPayslip(PayslipData payslip) {
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, payslip.month - 1);

        sb.append("========================================\n");
        sb.append("              PAYSLIP\n");
        sb.append("========================================\n");
        sb.append(String.format("Employee: %s (%s)\n", payslip.employeeName, payslip.designation));
        sb.append(String.format("Employee ID: %d\n", payslip.employeeId));
        sb.append(String.format("Period: %s %d\n", monthFormat.format(cal.getTime()), payslip.year));
        sb.append("----------------------------------------\n");
        sb.append("EARNINGS:\n");

        for (Map.Entry<String, Double> entry : payslip.earnings.entrySet()) {
            sb.append(String.format("  %-20s : %10.2f\n", entry.getKey(), entry.getValue()));
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("Gross Salary           : %10.2f\n", payslip.grossSalary));
        sb.append("----------------------------------------\n");
        sb.append("DEDUCTIONS:\n");

        for (Map.Entry<String, Double> entry : payslip.deductions.entrySet()) {
            sb.append(String.format("  %-20s : %10.2f\n", entry.getKey(), entry.getValue()));
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("Total Deductions       : %10.2f\n", payslip.totalDeductions));
        sb.append("----------------------------------------\n");
        sb.append(String.format("NET SALARY            : %10.2f\n", payslip.netSalary));
        sb.append("========================================\n");

        return sb.toString();
    }
}
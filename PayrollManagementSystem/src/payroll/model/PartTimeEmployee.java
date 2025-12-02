// model/PartTimeEmployee.java
package payroll.model;

public class PartTimeEmployee extends Employee {
    private double hoursWorked;
    private boolean hourlyBased;

    public PartTimeEmployee(int id, String name, String designation, double rate, boolean hourlyBased) {
        super(id, name, designation, rate);
        this.hourlyBased = hourlyBased;
        this.hoursWorked = 0.0;
        // Part-time employees typically have different benefits
        salaryComponents.setPfPercent(0.0);
        salaryComponents.setTaxPercent(5.0);
    }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }
    public boolean isHourlyBased() { return hourlyBased; }

    @Override
    public double calculateGrossSalary() {
        if (hourlyBased) {
            return basicSalary * hoursWorked;
        } else {
            return basicSalary; // Fixed monthly rate for part-time
        }
    }

    @Override
    public double calculateNetSalary() {
        double gross = calculateGrossSalary();
        double pf = gross * salaryComponents.getPfPercent() / 100.0;
        double taxable = gross - pf;
        double tax = taxable * salaryComponents.getTaxPercent() / 100.0;
        double totalDeductions = pf + tax + salaryComponents.getDeductions();

        return gross - totalDeductions;
    }

    public double calculateMonthlySalary(double overtimeHours, double bonus, double extraDeductions) {
        double gross = calculateGrossSalary();
        double overtimePay = overtimeHours * salaryComponents.getOvertimeRate();
        gross += overtimePay + bonus;

        double pf = gross * salaryComponents.getPfPercent() / 100.0;
        double taxable = gross - pf;
        double tax = taxable * salaryComponents.getTaxPercent() / 100.0;
        double totalDeductions = pf + tax + salaryComponents.getDeductions() + extraDeductions;

        return gross - totalDeductions;
    }
}
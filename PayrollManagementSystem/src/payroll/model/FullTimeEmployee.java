// model/FullTimeEmployee.java
package payroll.model;

public class FullTimeEmployee extends Employee {
    private double fixedBenefits;

    public FullTimeEmployee(int id, String name, String designation, double basicSalary) {
        super(id, name, designation, basicSalary);
        this.fixedBenefits = 0.0;
        // Set default salary components for full-time employees
        salaryComponents.setHra(basicSalary * 0.20);
        salaryComponents.setMedicalAllowance(2000);
        salaryComponents.setConveyance(1000);
        salaryComponents.setPfPercent(5.0);
        salaryComponents.setTaxPercent(10.0);
    }

    public double getFixedBenefits() { return fixedBenefits; }
    public void setFixedBenefits(double fixedBenefits) { this.fixedBenefits = fixedBenefits; }

    @Override
    public double calculateGrossSalary() {
        return basicSalary +
                salaryComponents.getHra() +
                salaryComponents.getMedicalAllowance() +
                salaryComponents.getConveyance() +
                salaryComponents.getOtherAllowances() +
                fixedBenefits;
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
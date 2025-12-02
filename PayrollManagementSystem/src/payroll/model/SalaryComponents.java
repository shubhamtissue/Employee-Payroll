// model/SalaryComponents.java
package payroll.model;

public class SalaryComponents {
    private double hra;
    private double medicalAllowance;
    private double conveyance;
    private double otherAllowances;
    private double overtimeRate;
    private double pfPercent;
    private double taxPercent;
    private double deductions;

    public SalaryComponents() {
        this.hra = 0.0;
        this.medicalAllowance = 0.0;
        this.conveyance = 0.0;
        this.otherAllowances = 0.0;
        this.overtimeRate = 0.0;
        this.pfPercent = 0.0;
        this.taxPercent = 0.0;
        this.deductions = 0.0;
    }

    // Getters
    public double getHra() { return hra; }
    public double getMedicalAllowance() { return medicalAllowance; }
    public double getConveyance() { return conveyance; }
    public double getOtherAllowances() { return otherAllowances; }
    public double getOvertimeRate() { return overtimeRate; }
    public double getPfPercent() { return pfPercent; }
    public double getTaxPercent() { return taxPercent; }
    public double getDeductions() { return deductions; }

    // Setters
    public void setHra(double hra) { this.hra = hra; }
    public void setMedicalAllowance(double medicalAllowance) { this.medicalAllowance = medicalAllowance; }
    public void setConveyance(double conveyance) { this.conveyance = conveyance; }
    public void setOtherAllowances(double otherAllowances) { this.otherAllowances = otherAllowances; }
    public void setOvertimeRate(double overtimeRate) { this.overtimeRate = overtimeRate; }
    public void setPfPercent(double pfPercent) { this.pfPercent = pfPercent; }
    public void setTaxPercent(double taxPercent) { this.taxPercent = taxPercent; }
    public void setDeductions(double deductions) { this.deductions = deductions; }

    public double getTotalAllowances() {
        return hra + medicalAllowance + conveyance + otherAllowances;
    }
}
// model/Employee.java
package payroll.model;

public abstract class Employee {
    protected int id;
    protected String name;
    protected String designation;
    protected double basicSalary;
    protected SalaryComponents salaryComponents;

    public Employee(int id, String name, String designation, double basicSalary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        this.salaryComponents = new SalaryComponents();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDesignation() { return designation; }
    public double getBasicSalary() { return basicSalary; }
    public SalaryComponents getSalaryComponents() { return salaryComponents; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public abstract double calculateGrossSalary();
    public abstract double calculateNetSalary();

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (Basic: %.2f)", id, name, designation, basicSalary);
    }
}
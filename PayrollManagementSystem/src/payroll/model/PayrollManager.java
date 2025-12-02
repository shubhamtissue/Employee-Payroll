// model/PayrollManager.java
package payroll.model;

import java.util.*;

public class PayrollManager {
    private Map<Integer, Employee> employees;
    private int nextId;

    public PayrollManager() {
        this.employees = new LinkedHashMap<>();
        this.nextId = 1;
        seedSampleData();
    }

    public int addFullTimeEmployee(String name, String designation, double basicSalary) {
        int id = nextId++;
        FullTimeEmployee employee = new FullTimeEmployee(id, name, designation, basicSalary);
        employees.put(id, employee);
        return id;
    }

    public int addPartTimeEmployee(String name, String designation, double rate, boolean hourlyBased) {
        int id = nextId++;
        PartTimeEmployee employee = new PartTimeEmployee(id, name, designation, rate, hourlyBased);
        employees.put(id, employee);
        return id;
    }

    public Employee getEmployee(int id) {
        return employees.get(id);
    }

    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }

    public boolean removeEmployee(int id) {
        return employees.remove(id) != null;
    }

    public void updateEmployeeSalaryComponents(int id, SalaryComponents newComponents) {
        Employee employee = employees.get(id);
        if (employee != null) {
            employee.getSalaryComponents().setHra(newComponents.getHra());
            employee.getSalaryComponents().setMedicalAllowance(newComponents.getMedicalAllowance());
            employee.getSalaryComponents().setConveyance(newComponents.getConveyance());
            employee.getSalaryComponents().setOtherAllowances(newComponents.getOtherAllowances());
            employee.getSalaryComponents().setOvertimeRate(newComponents.getOvertimeRate());
            employee.getSalaryComponents().setPfPercent(newComponents.getPfPercent());
            employee.getSalaryComponents().setTaxPercent(newComponents.getTaxPercent());
            employee.getSalaryComponents().setDeductions(newComponents.getDeductions());
        }
    }

    private void seedSampleData() {
        addFullTimeEmployee("Ali Khan", "Software Engineer", 120000);
        addFullTimeEmployee("Sara Ahmed", "Senior Developer", 150000);
        addPartTimeEmployee("Zubair", "QA Tester", 800, true);
        addPartTimeEmployee("Hamza", "Intern", 30000, false);
    }
}
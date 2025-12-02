// NavigatorManager.java
package payroll;

import javax.swing.*;
import payroll.frames.*;
import payroll.model.PayrollManager;
import java.util.HashMap;
import java.util.Map;

public class NavigatorManager {
    private Map<String, JFrame> frames;
    private PayrollManager payrollManager;

    public NavigatorManager() {
        this.frames = new HashMap<>();
        this.payrollManager = new PayrollManager();
        initializeFrames();
    }

    private void initializeFrames() {
        frames.put("HOME", new HomeFrame(this));
        frames.put("ADD_EMPLOYEE", new AddEmployeeFrame(this, payrollManager));
        frames.put("VIEW_EMPLOYEES", new ViewEmployeesFrame(this, payrollManager));
        frames.put("GENERATE_PAYSLIP", new GeneratePayslipFrame(this, payrollManager));
        frames.put("EXIT", new ExitFrame(this));
    }

    public void showFrame(String frameName) {
        closeAllFrames();
        JFrame frame = frames.get(frameName);
        if (frame != null) {
            frame.setVisible(true);
        }
    }

    public void showHomeFrame() {
        showFrame("HOME");
    }

    public void showAddEmployeeFrame() {
        showFrame("ADD_EMPLOYEE");
    }

    public void showViewEmployeesFrame() {
        frames.put("VIEW_EMPLOYEES", new ViewEmployeesFrame(this, payrollManager));
        showFrame("VIEW_EMPLOYEES");
    }

    public void showGeneratePayslipFrame() {
        showFrame("GENERATE_PAYSLIP");
    }

    public void showExitFrame() {
        showFrame("EXIT");
    }

    private void closeAllFrames() {
        for (JFrame frame : frames.values()) {
            if (frame != null && frame.isVisible()) {
                frame.setVisible(false);
            }
        }
    }

    public void exitApplication() {
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to exit?",
                "Exit Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            closeAllFrames();
            System.exit(0);
        }
    }

    public PayrollManager getPayrollManager() {
        return payrollManager;
    }
}
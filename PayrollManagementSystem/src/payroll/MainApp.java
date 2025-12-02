// MainApp.java
package payroll;

import javax.swing.*;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Remove the look and feel setting or use default
            NavigatorManager manager = new NavigatorManager();
            manager.showHomeFrame();
        });
    }
}
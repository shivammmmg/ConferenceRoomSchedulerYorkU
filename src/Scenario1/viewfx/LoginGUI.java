package Scenario1.viewfx;

import javax.swing.*;
import Scenario1.controller.UserManager;
import shared.model.User;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: LoginGUI
 * Description:
 *  - Provides the login interface for existing users.
 *  - Connects to UserManager (Singleton) for authentication.
 *
 * Author: Shivam Gupta
 * Date: November 2025
 */
public class LoginGUI extends JFrame {

    public LoginGUI() {
        // Window setup
        setTitle("Login");
        setSize(400, 350);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Labels
        JLabel titleLbl = new JLabel("User Login");
        titleLbl.setBounds(150, 30, 200, 25);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setBounds(50, 90, 100, 25);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(50, 130, 100, 25);

        // Fields
        JTextField emailTxt = new JTextField();
        emailTxt.setBounds(150, 90, 180, 25);

        JPasswordField passTxt = new JPasswordField();
        passTxt.setBounds(150, 130, 180, 25);

        // Buttons
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 180, 100, 30);

        JButton registerBtn = new JButton("Register (New User)");
        registerBtn.setBounds(120, 230, 160, 30);

        // Add components
        add(titleLbl);
        add(emailLbl);
        add(passLbl);
        add(emailTxt);
        add(passTxt);
        add(loginBtn);
        add(registerBtn);

        // Login button logic
        loginBtn.addActionListener(e -> {
            try {
                String email = emailTxt.getText();
                String password = new String(passTxt.getPassword());

                User user = UserManager.getInstance().login(email, password);

                if (user != null) {
                    JOptionPane.showMessageDialog(this,
                            "Welcome, " + user.getName() + " (" + user.getUserType() + ")");
                    dispose();
                    // Next screen after login (optional)
                    // new ProfileGUI(user).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid credentials. Please try again.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register button logic
        registerBtn.addActionListener(e -> {
            dispose(); // Close login screen
            new RegisterGUI().setVisible(true); // Open register screen
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}

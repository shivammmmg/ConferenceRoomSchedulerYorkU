package Scenario1.viewfx;

import javax.swing.*;
import java.awt.Color;
import Scenario1.controller.UserManager;

/**
 * EECS 3311 - YorkU Conference Room Scheduler
 * ------------------------------------------------------------
 * Class: RegisterGUI
 * Description:
 *  - Provides a Swing-based registration form.
 *  - Connects to UserManager (Singleton) for account creation.
 *
 * Author: Shivam Gupta
 * Date: November 2025
 */
public class RegisterGUI extends JFrame {

    public RegisterGUI() {

        // Window setup
        setTitle("Create New Account");
        setSize(400, 400);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Labels
        JLabel titleLbl = new JLabel("Register New User");
        titleLbl.setBounds(130, 20, 200, 25);

        JLabel nameLbl = new JLabel("Full Name:");
        nameLbl.setBounds(50, 70, 100, 25);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setBounds(50, 110, 100, 25);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setBounds(50, 150, 100, 25);

        JLabel typeLbl = new JLabel("User Type:");
        typeLbl.setBounds(50, 210, 100, 25);

        // Fields
        JTextField nameTxt = new JTextField();
        nameTxt.setBounds(150, 70, 180, 25);

        JTextField emailTxt = new JTextField();
        emailTxt.setBounds(150, 110, 180, 25);

        JPasswordField passTxt = new JPasswordField();
        passTxt.setBounds(150, 150, 180, 25);

        JLabel hintLbl = new JLabel("<html><small>Password must have uppercase, lowercase, number, special char, and 6+ chars.</small></html>");
        hintLbl.setBounds(50, 175, 300, 25);
        hintLbl.setForeground(Color.GRAY);

        String[] types = {"Student", "Faculty", "Staff", "Partner"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        typeBox.setBounds(150, 210, 180, 25);

        // Buttons
        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(150, 260, 100, 30);

        JButton backBtn = new JButton("Back to Login");
        backBtn.setBounds(130, 310, 150, 30);

        // Add components
        add(titleLbl);
        add(nameLbl);
        add(emailLbl);
        add(passLbl);
        add(typeLbl);
        add(nameTxt);
        add(emailTxt);
        add(passTxt);
        add(typeBox);
        add(registerBtn);
        add(backBtn);
        add(hintLbl);


        // Register button logic
        registerBtn.addActionListener(e -> {
            try {
                boolean success = UserManager.getInstance().register(
                        nameTxt.getText(),
                        emailTxt.getText(),
                        new String(passTxt.getPassword()),
                        (String) typeBox.getSelectedItem()
                );
                if (success) {
                    JOptionPane.showMessageDialog(this, "Registration successful!");
                    // Optional: clear fields after success
                    nameTxt.setText("");
                    emailTxt.setText("");
                    passTxt.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Email already exists or password is weak.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Back button logic
        backBtn.addActionListener(e -> {
            dispose(); // Close this window
            new LoginGUI().setVisible(true); // Open login screen (we'll build next)
        });
    }

    // To run this file directly
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterGUI().setVisible(true));
    }
}

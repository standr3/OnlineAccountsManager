package ledger;

import ledger.dbconnection.ConnectionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Arrays;

public class SignupFrame extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signupButton;
    private JButton backButton;

    public SignupFrame() {
        setTitle("Signup");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new GridBagLayout());
        add(contentPane);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add username label
        JLabel usernameLabel = new JLabel("Username:");
        // Add username field
        usernameField = new JTextField(15);

        // Add password label
        JLabel passwordLabel = new JLabel("Password:");
        // Add password field
        passwordField = new JPasswordField(15);

        // Add confirm password label
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        // Add confirm password field
        confirmPasswordField = new JPasswordField(15);

        // Add signup button
        signupButton = new JButton("Signup");

        // Add back button
        backButton = new JButton("Back");

        // Add components to content pane
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPane.add(usernameLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPane.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPane.add(passwordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(confirmPasswordField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        contentPane.add(signupButton, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        contentPane.add(backButton, gbc);

        // Add action listeners
        signupButton.addActionListener(this);
        backButton.addActionListener(this);

        // Set frame size and location
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signupButton) {
            if (usernameField.getText().equals("") || passwordField.getPassword().length == 0 || confirmPasswordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Please fill all the fields");
            } else if (!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
                JOptionPane.showMessageDialog(this, "Passwords do not match");
            } else {
                try {
                    ConnectionManager connectionManager = new ConnectionManager();
                    try (ResultSet resultSet = connectionManager.getStatement()
                            .executeQuery("SELECT * FROM users WHERE user_name = '" + usernameField.getText() + "'")) {
                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(this, "Username already exists");
                        } else {
                            connectionManager.getStatement().executeUpdate("INSERT INTO users (user_name, pass_word) VALUES ('" + usernameField.getText() + "', '" + String.valueOf(passwordField.getPassword()) + "')");
                            JOptionPane.showMessageDialog(this, "Signup successful");
                            new LoginFrame().setVisible(true);
                            dispose();
                        }
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } else if (e.getSource() == backButton) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JButton exitButton;

    public LoginFrame() {
        setUndecorated(true);
//        setTitle("Login");
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new GridBagLayout());
        contentPane.setBackground(Color.WHITE);
        setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        ImageIcon userIcon = new ImageIcon(ClassLoader.getSystemResource("icons/user.png"));
        userIcon.setImage(userIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel userIconLabel = new JLabel(userIcon);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth =1;
        contentPane.add(userIconLabel, gbc);


        // Add username label
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPane.add(usernameLabel, gbc);

        // Add username field
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth =2;
        contentPane.add(usernameField, gbc);

        // Add password label
        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        contentPane.add(passwordLabel, gbc);

        // Add password field
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        contentPane.add(passwordField, gbc);

        // Add login button
        loginButton = new JButton("SIGN IN");
        loginButton.setBackground(Color.BLACK);
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(loginButton, gbc);

        // Add login button
        signupButton = new JButton("SIGN UP");
        signupButton.setBackground(Color.BLACK);
        signupButton.setForeground(Color.WHITE);
        signupButton.addActionListener(this);
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(signupButton, gbc);

        exitButton = new JButton("EXIT");
        exitButton.setBackground(Color.BLACK);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(exitButton, gbc);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            if(e.getSource()==loginButton){
                ConnectionManager conn = new ConnectionManager();
                String username  = usernameField.getText();
                String password  = passwordField.getText();
                String q  = "select * from users   where user_name = '"+username+"' and pass_word = '"+password+"'";
                String userID;
                ResultSet rs = conn.statement.executeQuery(q);
                if(rs.next()){
                    setVisible(false);
                    userID = rs.getString("id");
                    System.out.println(userID);
                    new AccountLedger(userID).setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Incorrect Card Number or PIN");
                }
            }else if(e.getSource()==signupButton){
//                setVisible(false);
//                new Signup().setVisible(true);
            }else if(e.getSource()==exitButton){
                System.exit(0);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("salut");
        new LoginFrame();
    }
}

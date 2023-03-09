import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class AccountLedger extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public AccountLedger(String id) {
        super("Account Ledger for ID " + id);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);

        String[] columns = {/*"entry_id","user_id",*/ "Site", "Username", "Password"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        scrollPane = new JScrollPane(table);
        add(scrollPane);

        /*String url = "jdbc:mysql://localhost:3306/mydatabase";
        String user = "myusername";
        String password = "mypassword";*/
        String query = "SELECT * FROM accounts WHERE user_id = '"+id+"'";

        try {
            ConnectionManager conn = new ConnectionManager();


            ResultSet rs = conn.statement.executeQuery(query);
            while (rs.next()) {
//                int accountId = rs.getInt("user_id");
                String site = rs.getString("website");
                String username = rs.getString("user_name");
                String password = rs.getString("pass_word");
                Object[] row = {/*accountId, */site, username, password};
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

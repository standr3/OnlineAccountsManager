package ledger;

import ledger.dbconnection.ConnectionManager;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.*;

public class AccountLedger extends JFrame {
    private final JTable table;
    private int selectedRow = -1;

    public AccountLedger(int id) {
        super("Account Ledger for ID " + id);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);

        String[] columns = {"ID","Site", "Username", "Password"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        //hide the ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAccount(id);
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> {
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AccountLedger.this, "Please select a row to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //extract all the current values from the selected row
            String site = (String) table.getValueAt(selectedRow, 1);
            String username = (String) table.getValueAt(selectedRow, 2);
            String password = (String) table.getValueAt(selectedRow, 3);
            updateAccount(selectedRow, site, username, password);
        });
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(AccountLedger.this, "Please select a row to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int accountId = (int) table.getValueAt(selectedRow, 0);
            deleteAccount(accountId);
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> selectedRow = table.getSelectedRow());
        getDataFromDB(model, id);
    }

    private void getDataFromDB(DefaultTableModel model, int id) {
        try {
            ConnectionManager conn = new ConnectionManager();
            String query = String.format("SELECT * FROM accounts WHERE user_id = '%d'", id);
            ResultSet rs = conn.getStatement().executeQuery(query);

            while (rs.next()) {
                int accountId = rs.getInt("entry_id");
                String site = rs.getString("website");
                String username = rs.getString("user_name");
                String password = rs.getString("pass_word");
                Object[] row = {accountId, site, username, password};
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void addAccount(int userId) {
        String site = JOptionPane.showInputDialog(this, "Enter site name:");
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");

        try {
            ConnectionManager conn = new ConnectionManager();
            String query =
                    String.format("INSERT INTO accounts (user_id, website, user_name, pass_word) VALUES ('%d', '%s', '%s', '%s')",
                    userId, site, username, password);
            int rowsInserted = conn.getStatement().executeUpdate(query);
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Account added successfully.");
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                Object[] row = {getLastInsertedId(conn.getConnection()), site, username, password};
                model.addRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to add account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private int getLastInsertedId(Connection conn) throws SQLException {
        String query = "SELECT LAST_INSERT_ID()";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }
    private void updateAccount(int row, String site, String username, String password) {
        int accountId = (int) table.getValueAt(row, 0);
        try {
            ConnectionManager conn = new ConnectionManager();
            String query =
                    String.format("UPDATE accounts SET website='%s', user_name='%s', pass_word='%s' WHERE entry_id='%d'"
                    ,site
                    ,username
                    ,password
                    ,accountId);
            int rowsUpdated = conn.getStatement().executeUpdate(query);
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Account updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Unable to update account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteAccount(int id) {
        try {
            ConnectionManager conn = new ConnectionManager();
            String query = String.format("DELETE FROM accounts WHERE entry_id='%d'", id);
            int rowsDeleted = conn.getStatement().executeUpdate(query);
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Account deleted successfully.");
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(this, "Unable to delete account.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package ledger.dbconnection;

import java.sql.*;

public class ConnectionManager {
    private Connection connection;
    private Statement statement;
    public ConnectionManager(){
        try{
//            Class.forName("com.mysql.jdbc.Driver");
            setConnection(DriverManager.getConnection(
                    "jdbc:mysql:///onlineaccountsmanagementsystem",
                    "root",
                    "m3nhwu69n49dhzxu"));
            setStatement(getConnection().createStatement());

        }catch(Exception e){
            System.out.println(e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Statement getStatement() {
        return statement;
    }

    public void setStatement(Statement statement) {
        this.statement = statement;
    }

}

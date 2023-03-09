
import java.sql.*;

public class ConnectionManager {
    Connection connection;
    Statement statement;
    public ConnectionManager(){
        try{
//            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:mysql:///onlineaccountsmanagementsystem",
                    "root",
                    "m3nhwu69n49dhzxu");
            statement = connection.createStatement();

        }catch(Exception e){
            System.out.println(e);
        }
    }
}

package DatabaseInterface;

import static DatabaseInterface.ConnectionForPgsql.con;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionForMysql
{
  
    static Connection con = null;
  
    static
    {
        String url = "jdbc:mysql://localhost:3306/zoho";
        String user = "root";
        String pass = "Tharani.sri1";
        try 
        {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static Connection getConnection()
    {
        return con;
    }
}

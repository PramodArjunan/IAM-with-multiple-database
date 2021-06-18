/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DatabaseInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionForPgsql 
{
    static Connection con = null;
  
    static
    {
        String url = "jdbc:postgresql://localhost:5432/pramod";
        String user = "postgres";
        String pass = "1234";
        try 
        {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(url, user, pass);
            con.setAutoCommit(false);
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

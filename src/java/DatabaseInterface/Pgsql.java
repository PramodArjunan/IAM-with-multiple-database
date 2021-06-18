package DatabaseInterface;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class Pgsql implements Database
{
   public void insertIntoUserInformation(String email,String name,long number,String psw1,String hid,int rc,int zid,String access) throws Exception
   {   
        Connection con=ConnectionForPgsql.getConnection();
        Statement stmt=con.createStatement();
        String sql="insert into userinfo(name,email,phone,hid,rc,psw,access) values ('"+name+"','"+email+"','"+number+"','"+hid+"','"+rc+"','"+psw1+"','"+access+"');";
        stmt.executeUpdate(sql);
        con.commit();       
   }
    public ResultSet selectDetailsBasedOnEmail(String email) throws Exception
   {
        Connection con=ConnectionForPgsql.getConnection();
        if(con!=null)
        {  Statement stmt=con.createStatement();
            String sql="select * from userinfo where email='"+email+"';";  
            ResultSet rs = stmt.executeQuery(sql);con.commit();         
            return rs;
        }
        return null;
   }
   public void updateRcValue(int rc,String email) throws Exception
   {
        Connection con=ConnectionForPgsql.getConnection();
        Statement stmt=con.createStatement();
        String sql1="update userinfo set rc='"+rc+"' where email='"+email+"';";  
        stmt.executeUpdate(sql1);
        con.commit();       
   }
   
   public void updateAccess(String email) throws Exception
   {
        Connection con=ConnectionForPgsql.getConnection();
        Statement stmt=con.createStatement();
        String access="yes";        
        String sql1="update userinfo set access='"+access+"' where email='"+email+"';";  
        stmt.executeUpdate(sql1);
        con.commit();
   }
   
   public void updatePassword(String newpsw,String email) throws Exception
   { 
        Connection con=ConnectionForPgsql.getConnection();
        Statement stmt=con.createStatement();
        String sql1="update userinfo set psw='"+newpsw+"' where email='"+email+"';";  
        stmt.executeUpdate(sql1);
        con.commit();       
   }
   
   public void insertIntoTransaction(int zid,String trans,int withdraw,int rc) throws Exception
   {
        Connection con=ConnectionForPgsql.getConnection();
        Statement stmt=con.createStatement();
        String sql2="insert into trans(zid,trans,amount,balance) values ('"+zid+"','"+trans+"','"+withdraw+"','"+rc+"');";
        stmt.executeUpdate(sql2);
        con.commit();        
   }
   
   public ResultSet selectUserBasedOnZid(String db,int zid) throws Exception
   {
        Connection con=ConnectionForPgsql.getConnection();
        Statement stmt=con.createStatement();
        String sql="select * from "+db+" where zid='"+zid+"';";         
        return stmt.executeQuery(sql);
   }
   
   public  ResultSet selectAllUsers() throws Exception
   {
        Connection con=ConnectionForPgsql.getConnection();
        Statement stmt=con.createStatement();
        String sql="select * from userinfo;";  
        con.commit();
        return stmt.executeQuery(sql);  
   }
}


package DatabaseInterface;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Mysql implements Database
{
    public void insertIntoUserInformation(String email,String name,long number,String psw1,String hid,int rc,int zid,String access) throws Exception
   {
        Connection con=ConnectionForMysql.getConnection();
        PreparedStatement ps = con.prepareStatement("insert into userinfo(name,phone,email,hid,rc,zid,psw,access) values(?,?,?,?,?,?,?,?)");
        ps.setString(1, name);
        ps.setLong(2, number);
        ps.setString(3,email);
        ps.setString(4,hid);
        ps.setInt(5,rc);
        ps.setInt(6,zid);
        ps.setString(7,psw1);
        ps.setString(8,access);
        ps.executeUpdate();
   }
    
   public void updateRcValue(int rc,String email) throws Exception
   {
        Connection con=ConnectionForMysql.getConnection();
        PreparedStatement ps1=con.prepareStatement("update userinfo set rc=? where email=?");  
        ps1.setInt(1,rc);  
        ps1.setString(2,email);
        ps1.executeUpdate();
   }
   
   public void updateAccess(String email) throws Exception
   {
        Connection con=ConnectionForMysql.getConnection();
        PreparedStatement ps1=con.prepareStatement("update userinfo set access=? where email=?");  
        ps1.setString(1,"yes");  
        ps1.setString(2,email);
        ps1.executeUpdate();
   }
   
   public void updatePassword(String newpsw,String email) throws Exception
   {
        Connection con=ConnectionForMysql.getConnection();
        PreparedStatement ps1=con.prepareStatement("update userinfo set psw=? where email=?");
        ps1.setString(1,newpsw);  
        ps1.setString(2,email);
        ps1.executeUpdate();
   }
   
   public void insertIntoTransaction(int zid,String trans,int withdraw,int rc) throws Exception
   {
        Connection con=ConnectionForMysql.getConnection();
        PreparedStatement ps2=con.prepareStatement("insert into trans(zid,trans,amount,balance) values(?,?,?,?)");
        ps2.setInt(1,zid);  
        ps2.setString(2,trans);
        ps2.setInt(3,withdraw);
        ps2.setInt(4,rc);
        ps2.executeUpdate();
        
   }
   
   
   public ResultSet selectDetailsBasedOnEmail(String email) throws Exception
   {
        Connection con=ConnectionForMysql.getConnection();
        if(con!=null)
        {
            PreparedStatement ps=con.prepareStatement("select * from userinfo where email=?");  
            ps.setString(1,email);  
            ResultSet rs=ps.executeQuery();
            return rs;   
        }
        return null;
   }
  
   public ResultSet selectUserBasedOnZid(String db,int zid) throws Exception
   {
        Connection con=ConnectionForMysql.getConnection();
        PreparedStatement ps2=con.prepareStatement("select * from "+db+" where zid=?");
        ps2.setInt(1,zid);
        return ps2.executeQuery();
   }

    public ResultSet selectAllUsers(String db) throws Exception
    {
        Connection con=ConnectionForMysql.getConnection();
        PreparedStatement ps=con.prepareStatement("select * from "+db+"");
        return ps.executeQuery();
   }
}

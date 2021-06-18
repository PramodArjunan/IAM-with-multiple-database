package DatabaseInterface;
import org.elasticsearch.search.SearchHit;
import java.io.IOException;


public interface Database
{
    public void insertIntoUserInformation(String user_email,String user_name,long user_number,String user_password,String user_hid,int user_recharge,int user_zid,String user_access) throws Exception;
    
    public void insertIntoTransaction(int user_email,String trans,int deposit_amount,int rc) throws Exception;

    public void updateRcValue(int rc,String user_email) throws Exception;
    
    public void updatePassword(String user_newpassword,String user_email) throws Exception;
    
    public void updateAccess(String email) throws Exception;

}

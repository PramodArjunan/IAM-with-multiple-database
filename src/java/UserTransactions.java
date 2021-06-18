import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elasticsearch.search.SearchHit;
import TemporaryStorageInJvm.CurrentUser;
import TemporaryStorageInJvm.TemporaryUserStorageInJvm;
import TemporaryStorageInJvm.UserCreation;
import DatabaseInterface.ElasticSearch;
import DatabaseInterface.Mysql;
import DatabaseInterface.Pgsql;

public class UserTransactions extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        
        String modalForm = request.getParameter("modalForm");
    
        UserCreation detailsOfUSer=null; 
        
        Mysql mysql=new Mysql();
        
        Pgsql pgsql=new Pgsql();
        
        ElasticSearch elasticSearch=new ElasticSearch();
       
        
        try 
        {
            // deposit by user
            
            if("deposit".equals(modalForm))
            {
                String user_email =CurrentUser.getemail();
                detailsOfUSer=TemporaryUserStorageInJvm.get(user_email);
                int rc=detailsOfUSer.getrc();
                int zid=detailsOfUSer.getzid();
                int deposit_amount = Integer.valueOf(request.getParameter("uname"));
                rc=rc+deposit_amount;
                detailsOfUSer.setrc(rc);
                elasticSearch.updateRcValue(rc, user_email);
                elasticSearch.insertIntoTransaction(zid,"deposit",deposit_amount,rc);
                mysql.updateRcValue(rc, user_email);
                mysql.insertIntoTransaction(zid,"deposit",deposit_amount,rc);
                pgsql.updateRcValue(rc, user_email);
                pgsql.insertIntoTransaction(zid,"deposit",deposit_amount,rc);
                out.println("<h1>deposit successfull</h1>");
            }
            
            
            
        //for user to send amount to another using zid
            
            if("sendrc".equals(modalForm))
            { 
                String user_email =CurrentUser.getemail();
                detailsOfUSer=TemporaryUserStorageInJvm.get(user_email);
                int rc=detailsOfUSer.getrc();
                int zid=detailsOfUSer.getzid();
                int send_zid=Integer.valueOf(request.getParameter("uname"));
                int send_amount=Integer.valueOf(request.getParameter("rc"));
                rc=rc-send_amount;
                detailsOfUSer.setrc(rc);
                elasticSearch.updateRcValue(rc,user_email);
                elasticSearch.insertIntoTransaction(zid,"send",send_amount,rc);
                mysql.updateRcValue(rc,user_email);
                mysql.insertIntoTransaction(zid,"send",send_amount,rc);
                pgsql.updateRcValue(rc,user_email);
                pgsql.insertIntoTransaction(zid,"send",send_amount,rc);
                Map<String, Object> map=null;
                SearchHit[] searchHit =elasticSearch.selectUserBasedOnZid("userinfo",send_zid);
            	String send_email="";
                rc=0;
                for (SearchHit hit : searchHit) 
            	{
            	    map = hit.getSourceAsMap();
                    send_email=(String)map.get("email");
                    rc=(int)map.get("rc");
                }
                rc=rc+send_amount;
                elasticSearch.updateRcValue(rc,send_email);
                elasticSearch.insertIntoTransaction(send_zid,"receive",send_amount,rc);
                mysql.updateRcValue(rc,send_email);
                mysql.insertIntoTransaction(send_zid,"receive",send_amount,rc);
                pgsql.updateRcValue(rc,send_email);
                pgsql.insertIntoTransaction(send_zid,"receive",send_amount,rc);
                out.println("successfully transfered");
            }
            
            //withdraw by user
            
            if("withdraw".equals(modalForm))
            {
                String user_email =CurrentUser.getemail();
                detailsOfUSer=TemporaryUserStorageInJvm.get(user_email);
                int rc=detailsOfUSer.getrc();
                int zid=detailsOfUSer.getzid();
                int withdraw_amount = Integer.valueOf(request.getParameter("uname"));
                rc=rc-withdraw_amount;
                detailsOfUSer.setrc(rc);
                elasticSearch.updateRcValue(rc, user_email);
                elasticSearch.insertIntoTransaction(zid,"withdraw",withdraw_amount,rc);
                mysql.updateRcValue(rc, user_email);
                mysql.insertIntoTransaction(zid,"withdraw",withdraw_amount,rc);
                pgsql.updateRcValue(rc, user_email);
                pgsql.insertIntoTransaction(zid,"withdraw",withdraw_amount,rc);
                out.println("<h1>withdraw successfull</h1>");
            }
            
            //user can view the withdraw transaction
            
            if("viewwithdraw".equals(modalForm))
            {
            	String email =CurrentUser.getemail();
                detailsOfUSer=TemporaryUserStorageInJvm.get(email);
                int zid=detailsOfUSer.getzid();
        	Map<String, Object> map=null;
        	out.println("<h1>WITHDRAW</h1>");  
                out.print("<table border='1' width='100%'");   
                out.print("<tr><th>Transaction</th><th>Amount</th><th>Balance</th></tr>");
                SearchHit[] searchHit =elasticSearch.selectUserBasedOnZid("trans",zid);
	        for (SearchHit hit : searchHit) 
	        {
	            map = hit.getSourceAsMap();
        	    if("withdraw".equals((String)map.get("transaction")))
        	    {
        	      	out.print("<tr><td>"+map.get("transaction")+"</td><td>"+map.get("amount")+"</td><td>"+map.get("balance")+"</td></tr>");                          
        	    }
        	}
        	out.print("</table>"); 
            }
            
            //for user to view all transactions
            
            if("viewtrans".equals(modalForm))
            {
                String email =CurrentUser.getemail();
                detailsOfUSer=TemporaryUserStorageInJvm.get(email);
                int zid=detailsOfUSer.getzid();
        	Map<String, Object> map=null;
        	out.println("<h1>TRANSACTIONS</h1>");  
                out.print("<table border='1' width='100%'");   
                out.print("<tr><th>Transaction</th><th>Amount</th><th>Balance</th></tr>");
        	SearchHit[] searchHit =elasticSearch.selectUserBasedOnZid("trans",zid);
        	for (SearchHit hit : searchHit) 
        	{
        	    map = hit.getSourceAsMap();
        	    out.print("<tr><td>"+map.get("transaction")+"</td><td>"+map.get("amount")+"</td><td>"+map.get("balance")+"</td></tr>");                          
        	}
                out.print("</table>"); 
            }
            
    
           
            //for user to see the deposit transaction
            
            if("viewdeposit".equals(modalForm))
            {
            	String email =CurrentUser.getemail();
                detailsOfUSer=TemporaryUserStorageInJvm.get(email);
                int zid=detailsOfUSer.getzid();
        	Map<String, Object> map=null;
        	out.println("<h1>DEPOSIT</h1>");  
                out.print("<table border='1' width='100%'");   
                out.print("<tr><th>Transaction</th><th>Amount</th><th>Balance</th></tr>");
                SearchHit[] searchHit =elasticSearch.selectUserBasedOnZid("trans",zid);
	        for (SearchHit hit : searchHit) 
	        {
	            map = hit.getSourceAsMap();
        	    if("deposit".equals((String)map.get("transaction")))
        	    {
        	       	out.print("<tr><td>"+map.get("transaction")+"</td><td>"+map.get("amount")+"</td><td>"+map.get("balance")+"</td></tr>");                          
        	    }
        	}
                out.print("</table>"); 
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
        finally 
        {
            out.close();
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

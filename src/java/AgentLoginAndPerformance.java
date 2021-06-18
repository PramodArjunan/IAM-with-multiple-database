import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elasticsearch.search.SearchHit;
import DatabaseInterface.ElasticSearch;
import DatabaseInterface.Mysql;
import DatabaseInterface.Pgsql;

public class AgentLoginAndPerformance extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        
        String modalForm = request.getParameter("modalForm");
        
        ElasticSearch elasticSearch=new ElasticSearch();
        
        Mysql mysql=new Mysql();
        
        Pgsql pgsql=new Pgsql();
        
        try 
        {     
            // agent login
            
            if("agentlogin".equals(modalForm))
            {
                String agent_email = request.getParameter("uname");
                String agent_password = request.getParameter("psw");
                String email="p";
                String password="p";
                if(agent_password.equals(password)&&agent_email.equals(email))
                {
                    out.println("<h1>WELCOME ADMIN</h1>");  
                    out.print("<table border='1' width='100%'");   
                    out.print("<tr><th>Nameee</th><th>Email</th><th>Phone</th><th>H_ID</th><th>ZID</th><th>RC</th><th>Access</th></tr>");  
                    out.println("<h3>ACCESS NEED TO BE GIVEN FOR THE FOLLOWING USERS</h3>");  
            	    Map<String, Object> map=null;
            	    SearchHit[] searchHit =elasticSearch.selectAllUsers();
            	    for (SearchHit hit : searchHit) 
            	    {
            	        map = hit.getSourceAsMap();
            	        if("no".equals((String)map.get("access")))
            	        {
                            out.print("<tr><td>"+map.get("name")+"</td><td>"+map.get("email")+"</td><td>"+map.get("phone")+"</td><td>"+map.get("hid")+"</td><td>"+map.get("zid")+"</td><td>"+map.get("rc")+"</td><td>"+map.get("access")+"</td> <td><a href='agent_giving_access_to_user?id="+(String)map.get("email")+"'>Give access</a></td>  </tr>");                          
            	        }
            	    }
            	    out.print("</table>");
                    out.println("<br><br><br>");
                    out.println("<a href=AgentPasswordChange.jsp>Change password</a>");
                    out.println("<br><br><br>");
                    out.println("<form action='agent_login_and_performance' method='get'>");  
                    out.println("<input type=hidden name=modalForm value=viewtransbyadmin>");
                    out.println("Enter the ZID you need to see transaction :<input type=text name=uname>");                
                    out.println(" <input type=submit value=ok>");
                    out.println("</form>");
                    out.println("<form action='agent_login_and_performance' method='get'>");  
                    out.println("<input type=hidden name=modalForm value=allusers>");
                    out.println("Do you want to see all users that you have given access:");                
                    out.println(" <input type=submit value=ok>");
                    out.println("</form>");
                    out.println("<a href=AgentLogin.jsp><button type=\"button\">LOGOUT</button></a>");
                }
                else
                {
                    RequestDispatcher rd=request.getRequestDispatcher("/IncorrectAgentDetails.jsp");
                    rd.forward(request, response);
                }
            }  
            
            //agent password change for users
            
            if("adminchangepsw".equals(modalForm))
            {
                String user_email = request.getParameter("uname");
                String user_password = request.getParameter("psw");
                elasticSearch.updatePassword(user_password,user_email);
                mysql.updatePassword(user_password,user_email);
                pgsql.updatePassword(user_password,user_email);
                out.println("<h1>User password changed successfully</h1>");
            }
            
            //for viewing all the users by agent
            
            if("allusers".equals(modalForm))
            {
                Map<String, Object> map=null;
                out.println("<h1>ALL USERS</h1>");  
                out.print("<table border='1' width='100%'");   
                out.print("<tr><th>Nameee</th><th>Email</th><th>Phone</th><th>H_ID</th><th>ZID</th><th>RC</th><th>Access</th></tr>");  
        	SearchHit[] searchHit = elasticSearch.selectAllUsers();;
        	for (SearchHit hit : searchHit) 
        	{
        	    map = hit.getSourceAsMap();
        	    if("yes".equals((String)map.get("access")))
        	    {
        	        out.print("<tr><td>"+map.get("name")+"</td><td>"+map.get("email")+"</td><td>"+map.get("phone")+"</td><td>"+map.get("hid")+"</td><td>"+map.get("zid")+"</td><td>"+map.get("rc")+"</td><td>"+map.get("access")+"</td>   </tr>");                          
        	    }
                }        
        	out.print("</table>");
            }
            
            //for agent to see transactions of a particular user
            
            if("viewtransbyadmin".equals(modalForm))
            {
                int zid=Integer.valueOf(request.getParameter("uname"));
        	Map<String, Object> map=null;
                out.println("<h1>TRANSACTION OF ZID:"+zid+"</h1>");  
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

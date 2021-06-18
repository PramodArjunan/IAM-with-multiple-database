import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.elasticsearch.search.SearchHit;
import TemporaryStorageInJvm.TemporaryUserStorageInJvm;
import TemporaryStorageInJvm.UserCreation;
import TemporaryStorageInJvm.CurrentUser;
import DatabaseInterface.ElasticSearch;
import TemporaryStorageInJvm.UniqueidGeneration;
import DatabaseInterface.Mysql;
import DatabaseInterface.Pgsql;
import java.util.LinkedHashMap;
import java.sql.ResultSet;




public class UserCreationAndLogin extends HttpServlet
{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
        
        UserCreation newUser=new UserCreation();
        
        CurrentUser currentUser=new CurrentUser();
        
        ElasticSearch elasticSearch=new ElasticSearch();
        
        Mysql mysql=new Mysql();
        
        Pgsql pgsql=new Pgsql();
      
        String modalForm = request.getParameter("modalForm");
        
        try
        {    
            // userlogin
            
            if("userlogin".equals(modalForm))
            {
                
                String user_email = request.getParameter("uname");
                String user_password = request.getParameter("psw");
                UserCreation details_of_user=null;
                int row=0;
                ResultSet rs=null;
                details_of_user=null;//TemporaryUserStorageInJvm.get(user_email);
                Map<String,UserCreation> getting_all_users = new LinkedHashMap<>();
                getting_all_users=TemporaryUserStorageInJvm.getall();
                int size=getting_all_users.size();
                if(details_of_user==null)
                {
                    rs=mysql.selectDetailsBasedOnEmail(user_email);
                   
                    if(rs==null)
                    {
                        rs=pgsql.selectDetailsBasedOnEmail(user_email);   
                    }
                    if(rs!=null)
                    {
                        if(rs.next())
                        {
                            newUser.setemail(rs.getString("email"));
                            newUser.setname(rs.getString("name"));
                            newUser.setaccess(rs.getString("access"));
                            newUser.setphone(rs.getLong("phone"));
                            newUser.sethid(rs.getString("hid"));
                            newUser.setrc(rs.getInt("rc"));
                            newUser.setpsw(rs.getString("psw"));
                            newUser.setzid(rs.getInt("zid"));
                        }   
                    }
                    else
                    {
                        Map<String, Object> map=null;
                        SearchHit[] searchHit =elasticSearch.selectDetailsBasedOnEmail(user_email);
                        for (SearchHit hit : searchHit)
                        {
                            map = hit.getSourceAsMap();
                        }
                        newUser.setemail((String)map.get("email"));
                        newUser.setname((String)map.get("name"));
                        newUser.setaccess((String)map.get("access"));
                        newUser.setphone((int)map.get("phone"));
                        newUser.sethid((String)map.get("hid"));
                        newUser.setrc((int)map.get("rc"));
                        newUser.setpsw((String)map.get("psw"));
                        newUser.setzid((int)map.get("zid"));
                    }
                    if(size<=2)
                    {
                        if(user_password.equals(newUser.getpsw()))
                        { 
                           TemporaryUserStorageInJvm.add(user_email,newUser);
                        }
                    }  
                    else
                    {
                        if(user_password.equals(newUser.getpsw()))
                        { 
                            getting_all_users=TemporaryUserStorageInJvm.getall();
                            Map.Entry<String,UserCreation> entry = getting_all_users.entrySet().iterator().next();
                            String key=entry.getKey();
                            TemporaryUserStorageInJvm.remove(key);
                            TemporaryUserStorageInJvm.add(user_email,newUser);
                        }
                    }
                }
                details_of_user=TemporaryUserStorageInJvm.get(user_email);
                if(details_of_user!=null)
                {
                    if("yes".equals(details_of_user.getaccess()))
                    {
                           if(user_password.equals(details_of_user.getpsw()))
                           {
                               currentUser.setemail(user_email);
                               out.println("<h1>Login successfull</h1>");
                               out.println("<h3>Welome :"+details_of_user.getname()+"</h3>");
                               out.println("<h3>Email id :" +details_of_user.getemail()+"</h3>");
                               out.println("<h3>Phone number :" +details_of_user.getphone()+"</h3>");
                               out.println("<h3>H_id :" +details_of_user.gethid()+"</h3>");
                               out.println("<h3>RC :" +details_of_user.getrc()+"</h3>");
                               out.println("<h3>ZID :" +details_of_user.getzid()+"</h3>");
                               out.println("<form action='user_transactions' method='get'>");  
                               out.println("<input type=hidden name=modalForm value=deposit>");
                               out.println("Deposit :<input type=text name=uname>");
                               out.println("<input type=submit value=ok name=modalForm>");
                               out.println("</form>");
                               out.println("<br><br>");
                               out.println("<form action='user_transactions' method='get'>");  
                               out.println("<input type=hidden name=modalForm value=withdraw>");
                               out.println("withdraw :<input type=text name=uname>");
                               out.println(" <input type=submit value=ok>");
                               out.println("</form>");
                               out.println("<br><br>");
                               out.println("<form action='user_transactions' method='get'>");  
                               out.println("<input type=hidden name=modalForm value=viewtrans>");
                               out.println("To view your transaction");
                               out.println(" <input type=submit value=Transaction>");
                               out.println("</form>");
                               out.println("<br><br>");
                               out.println("<form action='user_transactions' method='get'>");  
                               out.println("<input type=hidden name=modalForm value=viewdeposit>");
                               out.println("To view your deposit history");
                               out.println(" <input type=submit value=Transaction>");
                               out.println("</form>");
                               out.println("<br><br>");
                               out.println("<form action='user_transactions' method='get'>");  
                               out.println("<input type=hidden name=modalForm value=viewwithdraw>");
                               out.println("To view your withdraw history");
                               out.println(" <input type=submit value=Transaction>");
                               out.println("</form>");
                               out.println("<br><br>");
                               out.println("<a href=UserPasswordChange.jsp>Change password</a>");
                               out.println("<br><br>");
                               out.println("<form action='user_transactions' method='get'>");  
                               out.println("<input type=hidden name=modalForm value=sendrc>");
                               out.println("Enter the ZID you need to send rc :<input type=text name=uname>");
                               out.println("<br><br>");
                               out.println("Enter rc :<input type=text name=rc>");
                               out.println("<br><br>");
                               out.println("<input type=submit value=ok>");
                               out.println("</form><br><br>");
                                out.println("<a href=UserLogin.jsp><button type=\"button\">LOGOUT</button></a>");
                           }
                           else
                           {
                               RequestDispatcher rd=request.getRequestDispatcher("/UserLoginFailed.jsp");
                               rd.forward(request, response);
                           }
                   }
                    else
                    {
                        RequestDispatcher rd=request.getRequestDispatcher("/NoAccessForUserToLogin.jsp");
                        rd.forward(request, response);
                    }
                }
                else
                {
                    RequestDispatcher rd=request.getRequestDispatcher("/NoUserPresent.jsp");
                    rd.forward(request, response);
                }
            }
            
            
            //user creation
            
            if("usercreation".equals(modalForm))
            {
                UniqueidGeneration id=new UniqueidGeneration();
                String user_name = request.getParameter("name");
                long user_number = Long.parseLong(request.getParameter("number"));
                String user_email = request.getParameter("email");
                String user_password = request.getParameter("psw");
                String user_hid = request.getParameter("h_id"); 
                int user_recharge = Integer.valueOf(request.getParameter("rc"));
                String user_repeat_password = request.getParameter("psw-repeat");
                String user_access="no";
                int zid=id.getunique();
                zid=zid+1;
                id.setunique(zid);
                String regex = "^(?=.*[0-9])"+ "(?=.*[a-z])(?=.*[A-Z])"+ "(?=.*[@#$%^&+=])"+ "(?=\\S+$).{8,20}$";
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(user_password);
                boolean val=m.matches();
           //   if(val==true)
              //{
                    if(user_repeat_password.equals(user_password))
                    {     
                    	elasticSearch.insertIntoUserInformation(user_name,user_email,user_number,user_password,user_hid,user_recharge,zid,user_access);
                        mysql.insertIntoUserInformation(user_name,user_email,user_number,user_password,user_hid,user_recharge,zid,user_access);
                        pgsql.insertIntoUserInformation(user_name,user_email,user_number,user_password,user_hid,user_recharge,zid,user_access);
                        RequestDispatcher rd=request.getRequestDispatcher("/SignupSuccess.jsp");  
                        rd.forward(request, response);
                    }
                    else
                    {
                        RequestDispatcher rd=request.getRequestDispatcher("/IncorrectPassword.jsp");  
                        rd.forward(request, response);
                    }
                /*}
                    else
                    {
                        RequestDispatcher rd=request.getRequestDispatcher("/WrongPasswordcretiria.jsp");  
                        rd.forward(request, response);
                    }*/
            }
            
            // change password by user
            
            if("changepsw".equals(modalForm))
            {
                String user_email = request.getParameter("uname");
                String user_password = request.getParameter("psw");
                String user_newpassword = request.getParameter("psw1");
                UserCreation details_of_user=null;
                details_of_user=TemporaryUserStorageInJvm.get(user_email);
                String oldpassword=details_of_user.getpsw();
                if(user_password.equals(oldpassword))
                {    
                    elasticSearch.updatePassword(user_newpassword,user_email);
                    RequestDispatcher rd=request.getRequestDispatcher("/UserPasswordChangeSuccess.jsp");
                    rd.forward(request, response);   
                }
                else
                {
                    RequestDispatcher rd=request.getRequestDispatcher("/WrongOldPassword.jsp");
                    rd.forward(request, response);
                }
                
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}

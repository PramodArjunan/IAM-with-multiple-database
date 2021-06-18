import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DatabaseInterface.Mysql;
import DatabaseInterface.Pgsql;
import DatabaseInterface.ElasticSearch;
public class AgentGivingAccessToUser extends HttpServlet
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
       
        PrintWriter out = response.getWriter();
        
        ElasticSearch elasticSearch=new ElasticSearch();
        
        Mysql mysql=new Mysql();
        
        Pgsql pgsql=new Pgsql();
        
        //agent giving access to user login
        
        try
        {
            String email=request.getParameter("id"); 
            elasticSearch.updateAccess(email);
            mysql.updateAccess(email);
            pgsql.updateAccess(email);
            out.println("<h2>Access for the user is given successfully</h2>");
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        } 
        finally {
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

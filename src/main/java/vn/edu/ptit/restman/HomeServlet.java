package vn.edu.ptit.restman;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
@WebServlet("")
public class HomeServlet extends HttpServlet{
 protected void doGet(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  if(r.getSession(false)==null||r.getSession(false).getAttribute("role")==null){p.sendRedirect(r.getContextPath()+"/login");return;}
  Web.message(r,p,"home",null);
 }
}

package vn.edu.ptit.restman;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
 protected void doPost(HttpServletRequest r,HttpServletResponse p)throws IOException{
  if(r.getSession(false)!=null)r.getSession(false).invalidate();
  p.sendRedirect(r.getContextPath()+"/login");
 }
}

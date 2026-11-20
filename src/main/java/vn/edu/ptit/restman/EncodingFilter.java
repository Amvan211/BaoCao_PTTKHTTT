package vn.edu.ptit.restman;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebFilter("/*")
public class EncodingFilter implements Filter{
 public void doFilter(ServletRequest req,ServletResponse res,FilterChain chain)throws IOException,ServletException{
  req.setCharacterEncoding("UTF-8");res.setCharacterEncoding("UTF-8");
  if(req instanceof HttpServletRequest r && !r.getMethod().equals("GET") && !r.getMethod().equals("HEAD")){
   HttpSession s=r.getSession(false);
   String path=r.getRequestURI().substring(r.getContextPath().length());
   if(!path.equals("/login")&&(s==null||!String.valueOf(s.getAttribute("csrf")).equals(r.getParameter("csrf")))){((HttpServletResponse)res).sendError(403,"CSRF token không hợp lệ");return;}
  }
  if(req instanceof HttpServletRequest r){HttpSession s=r.getSession(true);if(s.getAttribute("csrf")==null)s.setAttribute("csrf",java.util.UUID.randomUUID().toString());}
  chain.doFilter(req,res);
 }
}

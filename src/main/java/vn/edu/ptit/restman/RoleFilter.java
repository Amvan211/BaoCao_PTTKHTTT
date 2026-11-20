package vn.edu.ptit.restman;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
@WebFilter(urlPatterns={"/manager/*","/stock/*"})
public class RoleFilter implements Filter{
 public void doFilter(ServletRequest request,ServletResponse response,FilterChain chain)throws IOException,ServletException{
  HttpServletRequest r=(HttpServletRequest)request;HttpServletResponse p=(HttpServletResponse)response;
  String role=r.getRequestURI().substring(r.getContextPath().length()).startsWith("/manager/")?"QUAN_LI":"KHO";
  if(!Web.role(r,role)){p.sendError(403,"Yêu cầu đăng nhập với đúng vai trò");return;}
  chain.doFilter(request,response);
 }
}

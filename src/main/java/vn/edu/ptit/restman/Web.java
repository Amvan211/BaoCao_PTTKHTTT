package vn.edu.ptit.restman;
import jakarta.servlet.http.*;
import java.io.IOException;
public final class Web {
 private Web(){}
 public static void message(HttpServletRequest req,HttpServletResponse resp,String page,String msg) throws IOException, jakarta.servlet.ServletException {
  req.setAttribute("message",msg);req.getRequestDispatcher("/WEB-INF/views/"+page+".jsp").forward(req,resp);
 }
 public static String safe(String s){return s==null?"":s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("'","&#39;");}
 public static boolean role(HttpServletRequest r,String role){return r.getSession(false)!=null&&role.equals(r.getSession(false).getAttribute("role"));}
 public static String param(HttpServletRequest r,String key){String v=r.getParameter(key);return v==null?"":v.trim();}
}

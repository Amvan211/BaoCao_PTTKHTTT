package vn.edu.ptit.restman;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
@WebServlet("/login")
public class AuthServlet extends HttpServlet {
 protected void doGet(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{Web.message(r,p,"login",null);}
 protected void doPost(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  String u=Web.param(r,"username"),pass=Web.param(r,"password");
  try(Connection c=DB.open();PreparedStatement s=c.prepareStatement("SELECT ma_nhan_vien,ho_ten,vai_tro,mat_khau_bam FROM nhan_vien WHERE ten_dang_nhap=? AND trang_thai='HOAT_DONG'")){
   s.setString(1,u);try(ResultSet rs=s.executeQuery()){
    if(rs.next()&&BCrypt.checkpw(pass,rs.getString("mat_khau_bam"))){
     r.getSession().invalidate();HttpSession session=r.getSession(true);r.changeSessionId();
     session.setAttribute("staffId",rs.getString("ma_nhan_vien"));session.setAttribute("name",rs.getString("ho_ten"));session.setAttribute("role",rs.getString("vai_tro"));
     p.sendRedirect(r.getContextPath()+"/");return;
    }
   }
   Web.message(r,p,"login","Sai tài khoản hoặc mật khẩu");
  }catch(SQLException e){throw new ServletException("Lỗi truy cập cơ sở dữ liệu",e);}
 }
}

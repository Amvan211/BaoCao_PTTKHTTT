package vn.edu.ptit.restman;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
@WebServlet("/manager/*")
public class ManagerServlet extends HttpServlet {
 protected void doGet(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  String path=r.getPathInfo()==null?"/":r.getPathInfo();
  try(Connection c=DB.open()){
   if(path.equals("/statistics")){
    String from=Web.param(r,"from"),to=Web.param(r,"to");
    if(from.isEmpty())from=LocalDate.now().withDayOfMonth(1).toString();
    if(to.isEmpty())to=LocalDate.now().toString();
    LocalDate f=LocalDate.parse(from),t=LocalDate.parse(to);
    if(f.isAfter(t)){Web.message(r,p,"statistics","Ngày bắt đầu không được lớn hơn ngày kết thúc");return;}
    List<Map<String,Object>> rows=new ArrayList<>();
    String sql="SELECT m.ma_mon,m.ten_mon,COALESCE(SUM(CASE WHEN h.ma_hoa_don IS NOT NULL THEN ct.so_luong ELSE 0 END),0) so_luong,COALESCE(SUM(CASE WHEN h.ma_hoa_don IS NOT NULL THEN ct.so_luong*ct.don_gia ELSE 0 END),0) doanh_thu FROM mon_an m LEFT JOIN chi_tiet_don_goi_mon ct ON ct.ma_mon=m.ma_mon LEFT JOIN don_goi_mon d ON d.ma_don=ct.ma_don AND d.thoi_gian_tao >= ? AND d.thoi_gian_tao < ? LEFT JOIN hoa_don h ON h.ma_don=d.ma_don GROUP BY m.ma_mon,m.ten_mon ORDER BY doanh_thu DESC";
    // Use conditional aggregation with date check to avoid counting rows outside the filter.
    sql="SELECT m.ma_mon,m.ten_mon,COALESCE(SUM(CASE WHEN h.ma_hoa_don IS NOT NULL AND d.thoi_gian_tao>=? AND d.thoi_gian_tao<? THEN ct.so_luong ELSE 0 END),0) so_luong,COALESCE(SUM(CASE WHEN h.ma_hoa_don IS NOT NULL AND d.thoi_gian_tao>=? AND d.thoi_gian_tao<? THEN ct.so_luong*ct.don_gia ELSE 0 END),0) doanh_thu FROM mon_an m LEFT JOIN chi_tiet_don_goi_mon ct ON ct.ma_mon=m.ma_mon LEFT JOIN don_goi_mon d ON d.ma_don=ct.ma_don LEFT JOIN hoa_don h ON h.ma_don=d.ma_don GROUP BY m.ma_mon,m.ten_mon ORDER BY doanh_thu DESC";
    try(PreparedStatement s=c.prepareStatement(sql)){Timestamp a=Timestamp.valueOf(f.atStartOfDay()),b=Timestamp.valueOf(t.plusDays(1).atStartOfDay());s.setTimestamp(1,a);s.setTimestamp(2,b);s.setTimestamp(3,a);s.setTimestamp(4,b);try(ResultSet x=s.executeQuery()){while(x.next()){Map<String,Object> row=new LinkedHashMap<>();row.put("code",x.getString(1));row.put("name",x.getString(2));row.put("quantity",x.getInt(3));row.put("revenue",x.getBigDecimal(4));rows.add(row);}}}
    r.setAttribute("rows",rows);r.setAttribute("from",from);r.setAttribute("to",to);Web.message(r,p,"statistics",null);
   }else if(path.equals("/dishes")){
    List<Map<String,Object>> rows=new ArrayList<>();
    try(PreparedStatement s=c.prepareStatement("SELECT ma_mon,ten_mon,don_gia,trang_thai FROM mon_an ORDER BY ten_mon");ResultSet x=s.executeQuery()){
     while(x.next()){Map<String,Object> row=new LinkedHashMap<>();row.put("code",x.getString(1));row.put("name",x.getString(2));row.put("price",x.getBigDecimal(3));row.put("status",x.getString(4));rows.add(row);}
    }r.setAttribute("rows",rows);Web.message(r,p,"dishes",null);
   }else if(path.equals("/combos")){
    List<Map<String,Object>> rows=new ArrayList<>();
    try(PreparedStatement s=c.prepareStatement("SELECT ma_combo,ten_combo,gia_ban,trang_thai FROM combo ORDER BY ten_combo");ResultSet x=s.executeQuery()){
     while(x.next()){Map<String,Object> row=new LinkedHashMap<>();row.put("code",x.getString(1));row.put("name",x.getString(2));row.put("price",x.getBigDecimal(3));row.put("status",x.getString(4));rows.add(row);}
    }r.setAttribute("rows",rows);Web.message(r,p,"combos",null);
   }else p.sendError(404);
  }catch(SQLException|IllegalArgumentException e){throw new ServletException("Không thể tải dữ liệu",e);}
 }
 protected void doPost(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  String path=r.getPathInfo();
  try(Connection c=DB.open()){
   if("/dishes".equals(path)){
    String code=Web.param(r,"code"),name=Web.param(r,"name"),action=Web.param(r,"action");
    if("stop".equals(action)){try(PreparedStatement s=c.prepareStatement("UPDATE mon_an SET trang_thai='NGUNG_BAN' WHERE ma_mon=?")){s.setString(1,code);s.executeUpdate();}}
    else{
     BigDecimal price=new BigDecimal(Web.param(r,"price"));if(price.signum()<0||code.isBlank()||name.isBlank())throw new IllegalArgumentException();
     String sql="INSERT INTO mon_an(ma_mon,ten_mon,don_gia,mo_ta,trang_thai) VALUES(?,?,?,?,'DANG_BAN') ON DUPLICATE KEY UPDATE ten_mon=VALUES(ten_mon),don_gia=VALUES(don_gia),mo_ta=VALUES(mo_ta)";
     try(PreparedStatement s=c.prepareStatement(sql)){s.setString(1,code);s.setString(2,name);s.setBigDecimal(3,price);s.setString(4,Web.param(r,"description"));s.executeUpdate();}
    }p.sendRedirect(r.getContextPath()+"/manager/dishes");
   }else if("/combos".equals(path)){
    String code=Web.param(r,"code"),action=Web.param(r,"action");
    if("stop".equals(action)){try(PreparedStatement s=c.prepareStatement("UPDATE combo SET trang_thai='NGUNG' WHERE ma_combo=?")){s.setString(1,code);s.executeUpdate();}}
    else{
     BigDecimal price=new BigDecimal(Web.param(r,"price"));if(price.signum()<0||code.isBlank()||Web.param(r,"name").isBlank())throw new IllegalArgumentException();
     try(PreparedStatement s=c.prepareStatement("INSERT INTO combo(ma_combo,ten_combo,gia_ban,trang_thai) VALUES(?,?,?,'HOAT_DONG') ON DUPLICATE KEY UPDATE ten_combo=VALUES(ten_combo),gia_ban=VALUES(gia_ban)")){s.setString(1,code);s.setString(2,Web.param(r,"name"));s.setBigDecimal(3,price);s.executeUpdate();}
    }p.sendRedirect(r.getContextPath()+"/manager/combos");
   }else p.sendError(404);
  }catch(SQLException|IllegalArgumentException e){r.setAttribute("message","Dữ liệu không hợp lệ hoặc trùng mã: "+Web.safe(e.getMessage()));doGet(r,p);}
 }
}

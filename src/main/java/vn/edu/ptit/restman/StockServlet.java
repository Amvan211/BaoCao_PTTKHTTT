package vn.edu.ptit.restman;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
@WebServlet("/stock/*")
public class StockServlet extends HttpServlet {
 protected void doGet(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  String path=r.getPathInfo();
  try(Connection c=DB.open()){
   if("/suppliers".equals(path)){
    List<Map<String,String>> rows=new ArrayList<>();
    try(PreparedStatement s=c.prepareStatement("SELECT ma_nha_cung_cap,ten_nha_cung_cap,so_dien_thoai,trang_thai FROM nha_cung_cap ORDER BY ten_nha_cung_cap");ResultSet x=s.executeQuery()){
     while(x.next()){Map<String,String> a=new LinkedHashMap<>();a.put("code",x.getString(1));a.put("name",x.getString(2));a.put("phone",x.getString(3));a.put("status",x.getString(4));rows.add(a);}
    }r.setAttribute("rows",rows);Web.message(r,p,"suppliers",null);
   }else if("/import".equals(path)){
    List<Map<String,String>> suppliers=new ArrayList<>(),ingredients=new ArrayList<>();
    try(PreparedStatement s=c.prepareStatement("SELECT ma_nha_cung_cap,ten_nha_cung_cap FROM nha_cung_cap WHERE trang_thai='HOAT_DONG'");ResultSet x=s.executeQuery()){while(x.next())suppliers.add(Map.of("id",x.getString(1),"name",x.getString(2)));}
    try(PreparedStatement s=c.prepareStatement("SELECT ma_nguyen_lieu,ten_nguyen_lieu FROM nguyen_lieu");ResultSet x=s.executeQuery()){while(x.next())ingredients.add(Map.of("id",x.getString(1),"name",x.getString(2)));}
    r.setAttribute("suppliers",suppliers);r.setAttribute("ingredients",ingredients);Web.message(r,p,"import",null);
   }else p.sendError(404);
  }catch(SQLException e){throw new ServletException(e);}
 }
 protected void doPost(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  String path=r.getPathInfo();
  if("/suppliers".equals(path)){supplier(r,p);return;}
  if("/import".equals(path)){importStock(r,p);return;}
  p.sendError(404);
 }
 private void supplier(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  String code=Web.param(r,"code"),action=Web.param(r,"action");
  try(Connection c=DB.open()){
   if("stop".equals(action)){try(PreparedStatement s=c.prepareStatement("UPDATE nha_cung_cap SET trang_thai='NGUNG' WHERE ma_nha_cung_cap=?")){s.setString(1,code);s.executeUpdate();}}
   else{
    String name=Web.param(r,"name");if(code.isBlank()||name.isBlank())throw new IllegalArgumentException();
    try(PreparedStatement s=c.prepareStatement("INSERT INTO nha_cung_cap(ma_nha_cung_cap,ten_nha_cung_cap,so_dien_thoai,dia_chi,trang_thai) VALUES(?,?,?,?,'HOAT_DONG') ON DUPLICATE KEY UPDATE ten_nha_cung_cap=VALUES(ten_nha_cung_cap),so_dien_thoai=VALUES(so_dien_thoai),dia_chi=VALUES(dia_chi)")){
     s.setString(1,code);s.setString(2,name);s.setString(3,Web.param(r,"phone"));s.setString(4,Web.param(r,"address"));s.executeUpdate();
    }
   }p.sendRedirect(r.getContextPath()+"/stock/suppliers");
  }catch(SQLException|IllegalArgumentException e){r.setAttribute("error","Không lưu được nhà cung cấp");doGet(r,p);}
 }
 private void importStock(HttpServletRequest r,HttpServletResponse p)throws IOException,ServletException{
  String supplier=Web.param(r,"supplier"),ingredient=Web.param(r,"ingredient");
  BigDecimal qty,price;
  try{qty=new BigDecimal(Web.param(r,"quantity"));price=new BigDecimal(Web.param(r,"price"));if(qty.signum()<=0||price.signum()<0)throw new IllegalArgumentException();}catch(Exception e){r.setAttribute("error","Số lượng phải > 0 và đơn giá >= 0");doGet(r,p);return;}
  String staff=(String)r.getSession().getAttribute("staffId");
  try(Connection c=DB.open()){
   c.setAutoCommit(false);
   try{
    try(PreparedStatement s=c.prepareStatement("SELECT ma_nha_cung_cap FROM nha_cung_cap WHERE ma_nha_cung_cap=? AND trang_thai='HOAT_DONG'")){s.setString(1,supplier);try(ResultSet x=s.executeQuery()){if(!x.next())throw new SQLException("Nhà cung cấp không hoạt động");}}
    try(PreparedStatement s=c.prepareStatement("SELECT ma_nguyen_lieu FROM nguyen_lieu WHERE ma_nguyen_lieu=? FOR UPDATE")){s.setString(1,ingredient);try(ResultSet x=s.executeQuery()){if(!x.next())throw new SQLException("Nguyên liệu không tồn tại");}}
    String id="PN-"+UUID.randomUUID().toString().substring(0,12);
    try(PreparedStatement s=c.prepareStatement("INSERT INTO phieu_nhap(ma_phieu_nhap,ma_nha_cung_cap,ma_nhan_vien,ngay_nhap,tong_tien,trang_thai) VALUES(?,?,?,NOW(),?,'DA_GHI_NHAN')")){
     s.setString(1,id);s.setString(2,supplier);s.setString(3,staff);s.setBigDecimal(4,qty.multiply(price));s.executeUpdate();
    }
    try(PreparedStatement s=c.prepareStatement("INSERT INTO chi_tiet_phieu_nhap(ma_phieu_nhap,ma_nguyen_lieu,so_luong,don_gia_nhap) VALUES(?,?,?,?)")){
     s.setString(1,id);s.setString(2,ingredient);s.setBigDecimal(3,qty);s.setBigDecimal(4,price);s.executeUpdate();
    }
    try(PreparedStatement s=c.prepareStatement("UPDATE nguyen_lieu SET so_luong_ton=so_luong_ton+? WHERE ma_nguyen_lieu=?")){
     s.setBigDecimal(1,qty);s.setString(2,ingredient);if(s.executeUpdate()!=1)throw new SQLException("Không cập nhật được tồn kho");
    }
    c.commit();r.setAttribute("success","Nhập kho thành công: "+id);
   }catch(Exception e){c.rollback();r.setAttribute("error","Không thể ghi nhận phiếu nhập: "+e.getMessage());}
  }catch(SQLException e){throw new ServletException(e);}
  doGet(r,p);
 }
}

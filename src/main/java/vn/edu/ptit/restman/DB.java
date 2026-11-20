package vn.edu.ptit.restman;
import java.sql.*;
public final class DB {
 private DB(){}
 public static Connection open() throws SQLException {
  try { Class.forName("com.mysql.cj.jdbc.Driver"); }
  catch (ClassNotFoundException e) { throw new SQLException("Không tìm thấy MySQL JDBC driver",e); }
  String url=System.getenv().getOrDefault("RESTMAN_DB_URL","jdbc:mysql://localhost:3306/restman?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh");
  return DriverManager.getConnection(url,System.getenv().getOrDefault("RESTMAN_DB_USER","root"),System.getenv().getOrDefault("RESTMAN_DB_PASSWORD",""));
 }
}

# RestMan — Java EE (Jakarta Servlet/JSP/JDBC)
Bài tập PTTK — Đề 20 — Bùi Tiến Đạt (B23DCCN124).

## Công nghệ
Java 17, Jakarta Servlet 6, JSP, JDBC, MySQL 8, Maven WAR, Apache Tomcat 10.1.
Đây là **Java/Jakarta EE web application**, không phải Spring Boot.

## Khởi chạy
1. Cài JDK 17+, Maven, MySQL 8 và Tomcat 10.1.
2. Chạy `database/schema.sql` trên MySQL (lưu ý tạo mới DB; tránh chạy lại khi đã có dữ liệu). File này đã kèm dữ liệu mẫu cho toàn bộ giao diện.
3. Đăng nhập bằng một trong hai tài khoản demo (cùng mật khẩu `123456`):
   - Quản lý: `quanly` — xem thống kê, món ăn và combo.
   - Thủ kho: `thukho` — nhập kho và quản lý nhà cung cấp.
   Khi dùng ngoài môi trường demo, hãy tạo BCrypt mới bằng `scripts/create_password_hash.py` và đổi mật khẩu mặc định.
4. Đặt biến môi trường `RESTMAN_DB_URL`, `RESTMAN_DB_USER`, `RESTMAN_DB_PASSWORD`.
5. Chạy `mvn clean package`; copy `target/restman.war` vào `tomcat/webapps/`; mở `http://localhost:8080/restman/`.
6. Đăng nhập để sử dụng chức năng theo vai trò.

## Phạm vi đã lập trình
- Đăng nhập / đăng xuất, kiểm soát vai trò và CSRF.
- Module 1: thống kê doanh thu và số lượng bán theo món, quản lý thông tin món ăn, tạo/sửa/ngừng combo cơ bản.
- Module 2: thêm/sửa/ngừng nhà cung cấp; nhập **một dòng nguyên liệu/phiếu**, lưu phiếu và tăng tồn kho trong cùng transaction.
- Giao diện JSP + CSS responsive và dữ liệu mẫu.
## Chưa triển khai
- Các loại thống kê nguyên liệu, khách hàng, nhà cung cấp; cấu hình các dòng món trong combo; nhập nhiều dòng nguyên liệu.
- Các nghiệp vụ bán hàng/khách hàng và đặt trực tuyến, dù đã có bảng dữ liệu để mở rộng.
- Phân trang, xuất báo cáo, kiểm thử tích hợp với MySQL và Tomcat thực tế.
## Lưu ý thiết kế
Sơ đồ PlantUML do người học cung cấp là mô hình mục tiêu, không đồng nghĩa toàn bộ use case đã được lập trình. Repository trong code được thực hiện trực tiếp bằng JDBC tại Servlet nhằm giữ bản demo gọn; sơ đồ thiết kế tách Service/Repository chi tiết hơn và là hướng refactor tiếp theo.
Không lưu mật khẩu trong source. Đổi mật khẩu demo, không công khai DB ra internet.

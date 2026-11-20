CREATE DATABASE IF NOT EXISTS restman CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restman;
CREATE TABLE nhan_vien(ma_nhan_vien VARCHAR(20) PRIMARY KEY,ho_ten VARCHAR(100) NOT NULL,vai_tro ENUM('QUAN_LI','KHO','BAN_HANG') NOT NULL,ten_dang_nhap VARCHAR(50) UNIQUE NOT NULL,mat_khau_bam VARCHAR(255) NOT NULL,trang_thai VARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG');
CREATE TABLE khach_hang(ma_khach_hang VARCHAR(20) PRIMARY KEY,ho_ten VARCHAR(100),so_dien_thoai VARCHAR(15) UNIQUE,email VARCHAR(120));
CREATE TABLE the_thanh_vien(ma_the VARCHAR(20) PRIMARY KEY,ma_khach_hang VARCHAR(20) NOT NULL UNIQUE,ngay_cap DATE,diem_tich_luy INT DEFAULT 0,hang_the VARCHAR(20),FOREIGN KEY(ma_khach_hang) REFERENCES khach_hang(ma_khach_hang));
CREATE TABLE mon_an(ma_mon VARCHAR(20) PRIMARY KEY,ten_mon VARCHAR(120) NOT NULL,mo_ta VARCHAR(500),don_gia DECIMAL(18,2) NOT NULL CHECK(don_gia>=0),trang_thai VARCHAR(20) NOT NULL DEFAULT 'DANG_BAN');
CREATE TABLE combo(ma_combo VARCHAR(20) PRIMARY KEY,ten_combo VARCHAR(120) NOT NULL,gia_ban DECIMAL(18,2) NOT NULL CHECK(gia_ban>=0),ngay_bat_dau DATE,ngay_ket_thuc DATE,trang_thai VARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG');
CREATE TABLE chi_tiet_combo(ma_combo VARCHAR(20),ma_mon VARCHAR(20),so_luong INT NOT NULL CHECK(so_luong>0),PRIMARY KEY(ma_combo,ma_mon),FOREIGN KEY(ma_combo) REFERENCES combo(ma_combo),FOREIGN KEY(ma_mon) REFERENCES mon_an(ma_mon));
CREATE TABLE nguyen_lieu(ma_nguyen_lieu VARCHAR(20) PRIMARY KEY,ten_nguyen_lieu VARCHAR(120) NOT NULL,don_vi_tinh VARCHAR(30),so_luong_ton DECIMAL(18,3) NOT NULL DEFAULT 0 CHECK(so_luong_ton>=0),muc_ton_toi_thieu DECIMAL(18,3) DEFAULT 0);
CREATE TABLE nha_cung_cap(ma_nha_cung_cap VARCHAR(20) PRIMARY KEY,ten_nha_cung_cap VARCHAR(150) NOT NULL,so_dien_thoai VARCHAR(15),dia_chi VARCHAR(255),trang_thai VARCHAR(20) NOT NULL DEFAULT 'HOAT_DONG');
CREATE TABLE phieu_nhap(ma_phieu_nhap VARCHAR(30) PRIMARY KEY,ma_nha_cung_cap VARCHAR(20) NOT NULL,ma_nhan_vien VARCHAR(20) NOT NULL,ngay_nhap DATETIME NOT NULL,tong_tien DECIMAL(18,2) NOT NULL,trang_thai VARCHAR(20) NOT NULL,FOREIGN KEY(ma_nha_cung_cap) REFERENCES nha_cung_cap(ma_nha_cung_cap),FOREIGN KEY(ma_nhan_vien) REFERENCES nhan_vien(ma_nhan_vien));
CREATE TABLE chi_tiet_phieu_nhap(ma_phieu_nhap VARCHAR(30),ma_nguyen_lieu VARCHAR(20),so_luong DECIMAL(18,3) NOT NULL CHECK(so_luong>0),don_gia_nhap DECIMAL(18,2) NOT NULL CHECK(don_gia_nhap>=0),PRIMARY KEY(ma_phieu_nhap,ma_nguyen_lieu),FOREIGN KEY(ma_phieu_nhap) REFERENCES phieu_nhap(ma_phieu_nhap),FOREIGN KEY(ma_nguyen_lieu) REFERENCES nguyen_lieu(ma_nguyen_lieu));
CREATE TABLE ban_an(ma_ban VARCHAR(20) PRIMARY KEY,ten_ban VARCHAR(50) UNIQUE,suc_chua INT,trang_thai VARCHAR(20));
CREATE TABLE dat_ban_truc_tuyen(ma_dat_ban VARCHAR(20) PRIMARY KEY,ma_khach_hang VARCHAR(20) NOT NULL,ma_ban VARCHAR(20) NOT NULL,thoi_gian_dat DATETIME,so_khach INT,trang_thai VARCHAR(20),FOREIGN KEY(ma_khach_hang) REFERENCES khach_hang(ma_khach_hang),FOREIGN KEY(ma_ban) REFERENCES ban_an(ma_ban));
CREATE TABLE don_goi_mon(ma_don VARCHAR(20) PRIMARY KEY,ma_khach_hang VARCHAR(20),ma_ban VARCHAR(20),ma_nhan_vien VARCHAR(20) NOT NULL,thoi_gian_tao DATETIME,trang_thai VARCHAR(20),kenh_dat VARCHAR(20),FOREIGN KEY(ma_khach_hang) REFERENCES khach_hang(ma_khach_hang),FOREIGN KEY(ma_ban) REFERENCES ban_an(ma_ban),FOREIGN KEY(ma_nhan_vien) REFERENCES nhan_vien(ma_nhan_vien));
CREATE TABLE chi_tiet_don_goi_mon(ma_don VARCHAR(20),ma_mon VARCHAR(20),so_luong INT NOT NULL CHECK(so_luong>0),don_gia DECIMAL(18,2) NOT NULL,ghi_chu VARCHAR(255),PRIMARY KEY(ma_don,ma_mon),FOREIGN KEY(ma_don) REFERENCES don_goi_mon(ma_don),FOREIGN KEY(ma_mon) REFERENCES mon_an(ma_mon));
CREATE TABLE hoa_don(ma_hoa_don VARCHAR(20) PRIMARY KEY,ma_don VARCHAR(20) UNIQUE NOT NULL,ma_nhan_vien VARCHAR(20) NOT NULL,thoi_gian_thanh_toan DATETIME,tong_tien DECIMAL(18,2),phuong_thuc VARCHAR(30),FOREIGN KEY(ma_don) REFERENCES don_goi_mon(ma_don),FOREIGN KEY(ma_nhan_vien) REFERENCES nhan_vien(ma_nhan_vien));
-- Dữ liệu mẫu phục vụ chạy thử giao diện.
-- Hai tài khoản bên dưới cùng dùng mật khẩu: 123456
INSERT INTO nhan_vien VALUES
('NVQL01','Nguyễn Minh Quản','QUAN_LI','quanly','$2a$10$JhNTYQPddS4rHIypjmm9TuNC8nifV6VAMO/JUUeM.9V/tg7rf6rae','HOAT_DONG'),
('NVK01','Trần Thu Kho','KHO','thukho','$2a$10$JhNTYQPddS4rHIypjmm9TuNC8nifV6VAMO/JUUeM.9V/tg7rf6rae','HOAT_DONG'),
('NVBH01','Lê Anh Bán','BAN_HANG','banhang','$2a$10$JhNTYQPddS4rHIypjmm9TuNC8nifV6VAMO/JUUeM.9V/tg7rf6rae','HOAT_DONG');

INSERT INTO khach_hang VALUES
('KH01','Phạm Hoàng Anh','0912345678','hoanganh@example.com'),
('KH02','Đỗ Mai Linh','0987654321','mailinh@example.com'),
('KH03','Khách vãng lai',NULL,NULL);
INSERT INTO the_thanh_vien VALUES
('TV01','KH01',DATE_SUB(CURDATE(),INTERVAL 180 DAY),1250,'VANG'),
('TV02','KH02',DATE_SUB(CURDATE(),INTERVAL 60 DAY),420,'BAC');

INSERT INTO mon_an VALUES
('M01','Phở bò','Phở bò truyền thống, nước dùng hầm xương',55000,'DANG_BAN'),
('M02','Cơm rang thập cẩm','Cơm rang với trứng, thịt và rau củ',45000,'DANG_BAN'),
('M03','Bún chả Hà Nội','Bún, chả nướng và nước chấm đặc biệt',60000,'DANG_BAN'),
('M04','Gà nướng mật ong','Đùi gà nướng mật ong kèm salad',85000,'DANG_BAN'),
('M05','Salad rau mùa','Rau xanh theo mùa và sốt mè rang',40000,'DANG_BAN'),
('M06','Trà đào cam sả','Trà đào thanh mát',30000,'DANG_BAN'),
('M07','Bánh flan','Bánh flan caramel',25000,'NGUNG_BAN');

INSERT INTO combo VALUES
('CB01','Combo văn phòng',89000,DATE_SUB(CURDATE(),INTERVAL 30 DAY),DATE_ADD(CURDATE(),INTERVAL 90 DAY),'HOAT_DONG'),
('CB02','Combo gia đình',279000,DATE_SUB(CURDATE(),INTERVAL 15 DAY),DATE_ADD(CURDATE(),INTERVAL 60 DAY),'HOAT_DONG'),
('CB03','Combo mùa hè',99000,DATE_SUB(CURDATE(),INTERVAL 180 DAY),DATE_SUB(CURDATE(),INTERVAL 30 DAY),'NGUNG');
INSERT INTO chi_tiet_combo VALUES
('CB01','M02',1),('CB01','M06',1),
('CB02','M03',2),('CB02','M04',1),('CB02','M05',1),('CB02','M06',2),
('CB03','M01',1),('CB03','M06',1);

INSERT INTO nguyen_lieu VALUES
('NL01','Gạo','kg',100,20),
('NL02','Thịt bò','kg',20,8),
('NL03','Thịt lợn','kg',35,10),
('NL04','Thịt gà','kg',28,8),
('NL05','Rau xanh','kg',18,6),
('NL06','Bún tươi','kg',12,5),
('NL07','Trứng gà','quả',180,50),
('NL08','Đào ngâm','hộp',15,5);

INSERT INTO nha_cung_cap VALUES
('NCC01','Thực phẩm Minh An','0900000000','Cầu Giấy, Hà Nội','HOAT_DONG'),
('NCC02','Nông sản Xanh Việt','0911222333','Gia Lâm, Hà Nội','HOAT_DONG'),
('NCC03','Thịt sạch An Tâm','0922333444','Thanh Trì, Hà Nội','HOAT_DONG'),
('NCC04','Đồ uống Hương Việt','0933444555','Nam Từ Liêm, Hà Nội','HOAT_DONG'),
('NCC05','Nhà cung cấp cũ','0944555666','Hà Đông, Hà Nội','NGUNG');

INSERT INTO ban_an VALUES
('B01','Bàn 01',4,'TRONG'),('B02','Bàn 02',4,'TRONG'),
('B03','Bàn 03',6,'DANG_PHUC_VU'),('B04','Bàn 04',2,'TRONG');

-- Các đơn trong tháng hiện tại để màn hình Thống kê có số liệu ngay khi mở.
INSERT INTO don_goi_mon VALUES
('D01','KH01','B01','NVBH01',DATE_SUB(NOW(),INTERVAL 2 DAY),'DA_THANH_TOAN','TAI_CHO'),
('D02','KH02','B02','NVBH01',DATE_SUB(NOW(),INTERVAL 5 DAY),'DA_THANH_TOAN','TAI_CHO'),
('D03','KH03','B03','NVBH01',DATE_SUB(NOW(),INTERVAL 8 DAY),'DA_THANH_TOAN','TAI_CHO'),
('D04','KH01',NULL,'NVBH01',DATE_SUB(NOW(),INTERVAL 12 DAY),'DA_THANH_TOAN','TRUC_TUYEN'),
('D05','KH02','B04','NVBH01',DATE_SUB(NOW(),INTERVAL 1 DAY),'DANG_PHUC_VU','TAI_CHO');
INSERT INTO chi_tiet_don_goi_mon VALUES
('D01','M01',2,55000,NULL),('D01','M06',2,30000,'Ít đá'),
('D02','M02',1,45000,NULL),('D02','M04',1,85000,NULL),
('D03','M03',3,60000,NULL),('D03','M05',1,40000,NULL),('D03','M06',3,30000,NULL),
('D04','M01',1,55000,'Không hành'),('D04','M03',1,60000,NULL),
('D05','M04',2,85000,NULL);
INSERT INTO hoa_don VALUES
('HD01','D01','NVBH01',DATE_SUB(NOW(),INTERVAL 2 DAY),170000,'CHUYEN_KHOAN'),
('HD02','D02','NVBH01',DATE_SUB(NOW(),INTERVAL 5 DAY),130000,'TIEN_MAT'),
('HD03','D03','NVBH01',DATE_SUB(NOW(),INTERVAL 8 DAY),310000,'THE'),
('HD04','D04','NVBH01',DATE_SUB(NOW(),INTERVAL 12 DAY),115000,'CHUYEN_KHOAN');

INSERT INTO phieu_nhap VALUES
('PN-DEMO-001','NCC01','NVK01',DATE_SUB(NOW(),INTERVAL 10 DAY),2500000,'DA_GHI_NHAN'),
('PN-DEMO-002','NCC03','NVK01',DATE_SUB(NOW(),INTERVAL 4 DAY),3600000,'DA_GHI_NHAN');
INSERT INTO chi_tiet_phieu_nhap VALUES
('PN-DEMO-001','NL01',100,25000),
('PN-DEMO-002','NL02',20,180000);

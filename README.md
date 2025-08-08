# 📚 Library Management System

Một ứng dụng quản lý thư viện được phát triển bằng **Java**, sử dụng **Hibernate** làm ORM và **MySQL** làm hệ quản trị cơ sở dữ liệu. Dự án hỗ trợ các chức năng cơ bản như quản lý sách, tác giả, thành viên và mượn/trả sách.

## ⚙️ Công nghệ sử dụng

- **Java 11+** – Ngôn ngữ lập trình chính  
- **Hibernate 5.6.15** – ORM Framework  
- **MySQL 8.0** – Cơ sở dữ liệu  
- **Maven** – Công cụ build  
- **SLF4J + Logback** – Logging  
- **EhCache** – Second-level cache

## 📁 Cấu trúc chính

- `entity/` – Các lớp ánh xạ CSDL  
- `repository/` – Tầng truy xuất dữ liệu  
- `service/` – Tầng xử lý nghiệp vụ  
- `util/` – Cấu hình Hibernate  
- `resources/` – File cấu hình: `hibernate.cfg.xml`, `logback.xml`, `ehcache.xml`

## 🚀 Khởi chạy nhanh

```bash
git clone https://github.com/Chaunekk/library-management-system.git
cd library-management-system
mvn clean compile
mvn exec:java -Dexec.mainClass="com.library.Main"

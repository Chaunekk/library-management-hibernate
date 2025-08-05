                                                            Library Management System
                                        Hệ thống quản lý thư viện được xây dựng bằng Java, Hibernate và MySQL.

📋 Mô tả dự án
Đây là một ứng dụng quản lý thư viện hoàn chỉnh với các chức năng:

Quản lý sách (thêm, sửa, xóa, tìm kiếm)
Quản lý tác giả
Quản lý thành viên thư viện
Quản lý việc mượn/trả sách
Báo cáo và thống kê
Tìm kiếm sách quá hạn
🛠️ Công nghệ sử dụng
Java 11+
Hibernate 5.6.15 - ORM Framework
MySQL 8.0 - Database
Maven - Build tool
SLF4J + Logback - Logging
EhCache - Second-level cache
📁 Cấu trúc dự án
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── library/
│   │           ├── entity/          # Entity classes
│   │           │   ├── Book.java
│   │           │   ├── Author.java
│   │           │   ├── Member.java
│   │           │   └── Borrowing.java
│   │           ├── repository/      # Data Access Layer
│   │           ├── service/         # Business Logic Layer
│   │           └── util/
│   │               └── HibernateUtil.java
│   └── resources/
│       ├── hibernate.cfg.xml       # Hibernate configuration
│       ├── logback.xml            # Logging configuration
│       └── ehcache.xml            # Cache configuration
└── test/
    └── java/                      # Unit tests
🚀 Cài đặt và chạy
Yêu cầu hệ thống
Java 11 hoặc cao hơn
Maven 3.6+
MySQL 8.0+
IDE (IntelliJ IDEA, Eclipse, VS Code)
Bước 1: Clone repository
git clone <repository-url>
cd library-management-system
Bước 2: Cài đặt MySQL
Tải và cài đặt MySQL Server
Tạo database:
CREATE DATABASE library_db;
CREATE USER 'library_user'@'localhost' IDENTIFIED BY 'library_password';
GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'localhost';
FLUSH PRIVILEGES;
Bước 3: Cấu hình database
Cập nhật thông tin database trong src/main/resources/hibernate.cfg.xml:

<property name="connection.url">jdbc:mysql://localhost:3306/library_db</property>
<property name="connection.username">library_user</property>
<property name="connection.password">library_password</property>
Bước 4: Build project
mvn clean compile
Bước 5: Chạy ứng dụng
mvn exec:java -Dexec.mainClass="com.library.Main"
📊 Database Schema
Bảng Books
book_id (PK) - ID sách
title - Tiêu đề sách
isbn - Mã ISBN
category - Thể loại
available - Trạng thái có sẵn
created_date - Ngày thêm
borrow_count - Số lần được mượn
Bảng Authors
author_id (PK) - ID tác giả
name - Tên tác giả
biography - Tiểu sử
nationality - Quốc tịch
Bảng Members
member_id (PK) - ID thành viên
name - Tên thành viên
email - Email (unique)
phone - Số điện thoại
join_date - Ngày gia nhập
active_borrowings - Số sách đang mượn
total_borrowed - Tổng số sách đã mượn
status - Trạng thái (ACTIVE/SUSPENDED/INACTIVE)
Bảng Borrowings
borrowing_id (PK) - ID phiếu mượn
member_id (FK) - ID thành viên
book_id (FK) - ID sách
borrow_date - Ngày mượn
due_date - Ngày hạn trả
return_date - Ngày trả thực tế
status - Trạng thái (BORROWED/RETURNED/OVERDUE)
Bảng Book_Authors (Many-to-Many)
book_id (FK)
author_id (FK)
🔧 Cấu hình
Hibernate Configuration
File hibernate.cfg.xml chứa:

Thông tin kết nối database
Dialect MySQL
Cấu hình cache
Entity mappings
Logging Configuration
File logback.xml cấu hình:

Log level
Log format
Output destinations
📝 Sử dụng
Quản lý sách
// Thêm sách mới
Book book = new Book("Java Programming", "978-0123456789", "Programming");
bookService.save(book);

// Tìm sách theo tiêu đề
List<Book> books = bookService.findByTitle("Java");

// Cập nhật thông tin sách
book.setCategory("Computer Science");
bookService.update(book);
Quản lý thành viên
// Đăng ký thành viên mới
Member member = new Member("John Doe", "john@email.com", "0123456789");
memberService.save(member);

// Kiểm tra điều kiện mượn sách
if (member.isEligibleToBorrow()) {
    // Cho phép mượn sách
}
Mượn/trả sách
// Mượn sách
LocalDate dueDate = LocalDate.now().plusWeeks(2);
Borrowing borrowing = borrowingService.borrowBook(member, book, dueDate);

// Trả sách
borrowingService.returnBook(borrowing);
🧪 Testing
Chạy unit tests:

mvn test
Chạy integration tests:

mvn verify
📈 Tính năng nâng cao
Stored Procedures: Tìm sách quá hạn
Caching: EhCache cho performance
Logging: Chi tiết các hoạt động
Validation: Kiểm tra dữ liệu đầu vào
Transaction Management: Đảm bảo tính nhất quán
🤝 Đóng góp
Fork repository
Tạo feature branch (git checkout -b feature/AmazingFeature)
Commit changes (git commit -m 'Add some AmazingFeature')
Push to branch (git push origin feature/AmazingFeature)
Tạo Pull Request
📄 License
Distributed under the MIT License. See LICENSE for more information.

📞 Liên hệ
Author: [Lan Châu]
Email: [chaunekk1904@gmail.com [blocked]]
Project Link: [https://github.com/Chaunekk/library-management-system]
🙏 Acknowledgments
Hibernate Documentation
MySQL Documentation
Maven Documentation
Stack Overflow Community

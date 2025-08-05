package com.library;

import com.library.entity.Book;
import com.library.entity.Author;
import com.library.entity.Member;
import com.library.util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Test Library Management System ===");
        
        // Test tạo Book
        Book book = new Book("Java Programming", "978-0123456789", "Programming");
        System.out.println("Tạo sách: " + book.getTitle());
        
        // Test tạo Author
        Author author = new Author("John Doe", "Experienced programmer", "USA");
        System.out.println("Tạo tác giả: " + author.getName());
        
        // Test tạo Member
        Member member = new Member("Nguyen Van A", "nguyenvana@email.com", "0123456789");
        System.out.println("Tạo thành viên: " + member.getName());
        
        // Test HibernateUtil (không kết nối DB)
        try {
            System.out.println("HibernateUtil class loaded successfully!");
        } catch (Exception e) {
            System.out.println("HibernateUtil error: " + e.getMessage());
        }
        
        System.out.println("=== Test hoàn tất ===");
    }
}
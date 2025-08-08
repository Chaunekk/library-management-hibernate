package com.library;

import com.library.entity.Book;
import com.library.entity.Member;
import com.library.repository.BookRepository;
import com.library.repository.MemberRepository;
import com.library.service.BookService;
import com.library.service.MemberService;
import com.library.util.HibernateUtil;
import org.hibernate.SessionFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // 1. Khởi tạo Hibernate SessionFactory
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // 2. Khởi tạo Repository và Validator
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        BookRepository bookRepository = new BookRepository(sessionFactory);
        MemberRepository memberRepository = new MemberRepository(sessionFactory);

        // 3. Khởi tạo Service
        BookService bookService = new BookService(bookRepository, validator);
        MemberService memberService = new MemberService(memberRepository, validator);

        try {
            // 4. Demo thêm Member
            Member member = new Member();
            member.setName("Nguyễn Văn A");
            member.setEmail("vana@example.com");
            member.setPhone("0123456789");
            memberService.registerMember(member);

            // 5. Demo thêm Book
            Book book = new Book();
            book.setTitle("Java Programming");
            book.setAuthor("James Gosling");
            book.setIsbn("978-1234567890");
            book.setPublishedDate(LocalDate.of(2020, 5, 15));
            bookService.addBook(book);

            // 6. Lấy ra và in thông tin book
            Book fetchedBook = bookService.getBookById(book.getBookId());
            System.out.println("Fetched book: " + fetchedBook.getTitle());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
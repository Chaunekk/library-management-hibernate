package com.library.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
/**
 * Entity class đại diện cho bảng Book trong database
 * Chứa thông tin về sách, bao gồm tiêu đề, ISBN, thể loại, tác giả và các thông tin liên quan khác
 */
@Entity
@Table(name = "books")
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;
    
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    
    @Column(name = "isbn", unique = true, length = 20)
    private String isbn;
    
    @Column(name = "category", length = 100)
    private String category;
    
    @Column(name = "available", nullable = false)
    private Boolean available = true;
    
    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "borrow_count")
    private Integer borrowCount = 0;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Borrowing> borrowings = new HashSet<>();
    
    // Constructors
    public Book() {}
    
    public Book(String title, String isbn, String category) {
        this.title = title;
        this.isbn = isbn;
        this.category = category;
        this.createdDate = LocalDate.now();
    }
    
    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Boolean getAvailable() {
        return available;
    }
    
    public void setAvailable(Boolean available) {
        this.available = available;
    }
    
    public LocalDate getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
    
    public Integer getBorrowCount() {
        return borrowCount;
    }
    
    public void setBorrowCount(Integer borrowCount) {
        this.borrowCount = borrowCount;
    }
    
    public Set<Author> getAuthors() {
        return authors;
    }
    
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
    
    public Set<Borrowing> getBorrowings() {
        return borrowings;
    }
    
    public void setBorrowings(Set<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }
    
    // Utility methods
    public void addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
    }
    
    public void removeAuthor(Author author) {
        this.authors.remove(author);
        author.getBooks().remove(this);
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", category='" + category + '\'' +
                ", available=" + available +
                '}';
    }
}
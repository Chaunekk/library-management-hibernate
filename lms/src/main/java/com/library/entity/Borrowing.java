package main.java.com.library.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Entity class đại diện cho bảng Borrowing trong database
 * Chứa thông tin về việc mượn sách của thành viên
 */
@Entity
@Table(name = "borrowings")
public class Borrowing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrowing_id")
    private Long borrowingId;
    
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "return_date")
    private LocalDate returnDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BorrowingStatus status = BorrowingStatus.BORROWED;
    
    @Column(name = "fine_amount")
    private Double fineAmount = 0.0;
    
    @Column(name = "notes", length = 500)
    private String notes;
    
    // Many-to-One relationship với Member
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    // Many-to-One relationship với Book
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    
    // Enum cho trạng thái mượn sách
    public enum BorrowingStatus {
        BORROWED, RETURNED, OVERDUE, LOST
    }
    
    // Constructors
    public Borrowing() {}
    
    public Borrowing(Member member, Book book, LocalDate dueDate) {
        this.member = member;
        this.book = book;
        this.borrowDate = LocalDate.now();
        this.dueDate = dueDate;
    }
    
    public Borrowing(Member member, Book book, LocalDate borrowDate, LocalDate dueDate) {
        this.member = member;
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }
    
    // Getters and Setters
    public Long getBorrowingId() {
        return borrowingId;
    }
    
    public void setBorrowingId(Long borrowingId) {
        this.borrowingId = borrowingId;
    }
    
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
    
    public BorrowingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BorrowingStatus status) {
        this.status = status;
    }
    
    public Double getFineAmount() {
        return fineAmount;
    }
    
    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setMember(Member member) {
        this.member = member;
    }
    
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    
    // Utility methods
    public boolean isOverdue() {
        return status != BorrowingStatus.RETURNED && 
               status != BorrowingStatus.LOST && 
               LocalDate.now().isAfter(dueDate);
    }
    
    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }
    
    public double calculateFine() {
        if (!isOverdue()) return 0.0;
        long daysOverdue = getDaysOverdue();
        // Phạt 5000 VND mỗi ngày quá hạn
        return daysOverdue * 5000.0;
    }
    
    public void returnBook() {
        this.returnDate = LocalDate.now();
        this.status = BorrowingStatus.RETURNED;
        if (isOverdue()) {
            this.fineAmount = calculateFine();
        }
    }
    
    public void extendDueDate(int days) {
        if (status == BorrowingStatus.BORROWED) {
            this.dueDate = this.dueDate.plusDays(days);
        }
    }
    
    @Override
    public String toString() {
        return "Borrowing{" +
                "borrowingId=" + borrowingId +
                ", borrowDate=" + borrowDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                ", fineAmount=" + fineAmount +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Borrowing)) return false;
        Borrowing borrowing = (Borrowing) o;
        return borrowingId != null && borrowingId.equals(borrowing.borrowingId);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
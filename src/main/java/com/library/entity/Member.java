package com.library.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity class đại diện cho bảng Member trong database
 * Chứa thông tin về thành viên thư viện
 */
@Entity
@Table(name = "members")
public class Member {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String email;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "address", length = 500)
    private String address;
    
    @Column(name = "join_date")
    private LocalDate joinDate;
    
    @Column(name = "active_borrowings")
    private Integer activeBorrowings = 0;
    
    @Column(name = "total_borrowed")
    private Integer totalBorrowed = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MemberStatus status = MemberStatus.ACTIVE;
    
    // One-to-Many relationship với Borrowing
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Borrowing> borrowings = new HashSet<>();
    
    // Enum cho trạng thái thành viên
    public enum MemberStatus {
        ACTIVE, SUSPENDED, INACTIVE
    }
    
    // Constructors
    public Member() {}
    
    public Member(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.joinDate = LocalDate.now();
    }
    
    public Member(String name, String email, String phone, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.joinDate = LocalDate.now();
    }
    
    // Getters and Setters
    public Long getMemberId() {
        return memberId;
    }
    
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public LocalDate getJoinDate() {
        return joinDate;
    }
    
    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
    
    public Integer getActiveBorrowings() {
        return activeBorrowings;
    }
    
    public void setActiveBorrowings(Integer activeBorrowings) {
        this.activeBorrowings = activeBorrowings;
    }
    
    public Integer getTotalBorrowed() {
        return totalBorrowed;
    }
    
    public void setTotalBorrowed(Integer totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }
    
    public MemberStatus getStatus() {
        return status;
    }
    
    public void setStatus(MemberStatus status) {
        this.status = status;
    }
    
    public Set<Borrowing> getBorrowings() {
        return borrowings;
    }
    
    public void setBorrowings(Set<Borrowing> borrowings) {
        this.borrowings = borrowings;
    }
    
    // Utility methods
    public boolean isEligibleToBorrow() {
        return status == MemberStatus.ACTIVE && activeBorrowings < 5;
    }
    
    public void incrementActiveBorrowings() {
        this.activeBorrowings++;
        this.totalBorrowed++;
    }
    
    public void decrementActiveBorrowings() {
        if (this.activeBorrowings > 0) {
            this.activeBorrowings--;
        }
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                ", activeBorrowings=" + activeBorrowings +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return memberId != null && memberId.equals(member.memberId);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
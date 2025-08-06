package com.library.repository;

import com.library.entity.Borrowing;
import com.library.entity.Borrowing.BorrowingStatus;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface cho Borrowing entity
 * Chứa các phương thức đặc biệt cho Borrowing
 */
public interface BorrowingRepository extends BaseRepository<Borrowing, Long> {
    
    /**
     * Tìm phiếu mượn theo thành viên
     * @param memberId ID thành viên
     * @return List các phiếu mượn của thành viên đó
     */
    List<Borrowing> findByMemberId(Long memberId);
    
    /**
     * Tìm phiếu mượn theo sách
     * @param bookId ID sách
     * @return List các phiếu mượn của sách đó
     */
    List<Borrowing> findByBookId(Long bookId);
    
    /**
     * Tìm phiếu mượn theo trạng thái
     * @param status Trạng thái phiếu mượn
     * @return List các phiếu mượn có trạng thái đó
     */
    List<Borrowing> findByStatus(BorrowingStatus status);
    
    /**
     * Tìm phiếu mượn đang hoạt động (BORROWED)
     * @return List các phiếu mượn đang hoạt động
     */
    List<Borrowing> findActiveBorrowings();
    
    /**
     * Tìm phiếu mượn quá hạn
     * @return List các phiếu mượn quá hạn
     */
    List<Borrowing> findOverdueBorrowings();
    
    /**
     * Tìm phiếu mượn quá hạn theo số ngày
     * @param days Số ngày quá hạn
     * @return List các phiếu mượn quá hạn hơn số ngày chỉ định
     */
    List<Borrowing> findOverdueBorrowings(int days);
    
    /**
     * Tìm phiếu mượn theo khoảng thời gian mượn
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return List các phiếu mượn trong khoảng thời gian đó
     */
    List<Borrowing> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm phiếu mượn theo khoảng thời gian hạn trả
     * @param startDate Ngày bắt đầu
     * @param endDate Ngày kết thúc
     * @return List các phiếu mượn có hạn trả trong khoảng thời gian đó
     */
    List<Borrowing> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Tìm phiếu mượn của thành viên theo trạng thái
     * @param memberId ID thành viên
     * @param status Trạng thái
     * @return List các phiếu mượn của thành viên có trạng thái đó
     */
    List<Borrowing> findByMemberIdAndStatus(Long memberId, BorrowingStatus status);
    
    /**
     * Tìm phiếu mượn của sách theo trạng thái
     * @param bookId ID sách
     * @param status Trạng thái
     * @return List các phiếu mượn của sách có trạng thái đó
     */
    List<Borrowing> findByBookIdAndStatus(Long bookId, BorrowingStatus status);
    
    /**
     * Đếm số phiếu mượn theo trạng thái
     * @param status Trạng thái
     * @return Số lượng phiếu mượn có trạng thái đó
     */
    long countByStatus(BorrowingStatus status);
    
    /**
     * Đếm số phiếu mượn của thành viên
     * @param memberId ID thành viên
     * @return Số lượng phiếu mượn của thành viên đó
     */
    long countByMemberId(Long memberId);
    
    /**
     * Đếm số phiếu mượn quá hạn
     * @return Số lượng phiếu mượn quá hạn
     */
    long countOverdueBorrowings();
    
    /**
     * Tính tổng tiền phạt
     * @return Tổng số tiền phạt
     */
    Double getTotalFineAmount();
    
    /**
     * Tìm sách được mượn nhiều nhất
     * @param limit Số lượng sách cần lấy
     * @return List các sách được mượn nhiều
     */
    List<Object[]> findMostBorrowedBooks(int limit);
    
    /**
     * Tìm thành viên mượn sách nhiều nhất
     * @param limit Số lượng thành viên cần lấy
     * @return List các thành viên mượn nhiều sách
     */
    List<Object[]> findMostActiveMembers(int limit);
}
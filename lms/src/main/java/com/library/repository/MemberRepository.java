package com.library.repository;

import com.library.entity.Member;
import com.library.entity.Member.MemberStatus;
import java.util.List;

/**
 * Repository interface cho Member entity
 * Chứa các phương thức đặc biệt cho Member
 */
public interface MemberRepository extends BaseRepository<Member, Long> {
    
    /**
     * Tìm thành viên theo tên
     * @param name Tên thành viên
     * @return List các thành viên có tên tương tự
     */
    List<Member> findByName(String name);
    
    /**
     * Tìm thành viên theo email
     * @param email Email thành viên
     * @return Member nếu tìm thấy, null nếu không
     */
    Member findByEmail(String email);
    
    /**
     * Tìm thành viên theo số điện thoại
     * @param phone Số điện thoại
     * @return Member nếu tìm thấy, null nếu không
     */
    Member findByPhone(String phone);
    
    /**
     * Tìm thành viên theo trạng thái
     * @param status Trạng thái thành viên
     * @return List các thành viên có trạng thái đó
     */
    List<Member> findByStatus(MemberStatus status);
    
    /**
     * Tìm thành viên đang mượn sách
     * @return List các thành viên có sách đang mượn
     */
    List<Member> findMembersWithActiveBorrowings();
    
    /**
     * Tìm thành viên có thể mượn sách (active và chưa đạt giới hạn)
     * @return List các thành viên có thể mượn sách
     */
    List<Member> findEligibleMembers();
    
    /**
     * Tìm thành viên mượn sách nhiều nhất
     * @param limit Số lượng thành viên cần lấy
     * @return List các thành viên mượn nhiều sách
     */
    List<Member> findMostActiveMembers(int limit);
    
    /**
     * Tìm thành viên theo từ khóa (tìm trong name, email, phone)
     * @param keyword Từ khóa tìm kiếm
     * @return List các thành viên phù hợp
     */
    List<Member> searchMembers(String keyword);
    
    /**
     * Đếm số thành viên theo trạng thái
     * @param status Trạng thái
     * @return Số lượng thành viên có trạng thái đó
     */
    long countByStatus(MemberStatus status);
    
    /**
     * Đếm số thành viên đang mượn sách
     * @return Số lượng thành viên có sách đang mượn
     */
    long countMembersWithActiveBorrowings();
    
    /**
     * Kiểm tra email đã tồn tại chưa
     * @param email Email cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByEmail(String email);
    
    /**
     * Kiểm tra số điện thoại đã tồn tại chưa
     * @param phone Số điện thoại cần kiểm tra
     * @return true nếu đã tồn tại, false nếu chưa
     */
    boolean existsByPhone(String phone);
}
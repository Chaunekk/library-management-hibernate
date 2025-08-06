package com.library.repository;

import com.library.entity.Author;
import java.util.List;

/**
 * Repository interface cho Author entity
 * Chứa các phương thức đặc biệt cho Author
 */
public interface AuthorRepository extends BaseRepository<Author, Long> {
    
    /**
     * Tìm tác giả theo tên
     * @param name Tên tác giả
     * @return List các tác giả có tên tương tự
     */
    List<Author> findByName(String name);
    
    /**
     * Tìm tác giả theo quốc tịch
     * @param nationality Quốc tịch
     * @return List các tác giả có quốc tịch đó
     */
    List<Author> findByNationality(String nationality);
    
    /**
     * Tìm tác giả theo năm sinh
     * @param birthYear Năm sinh
     * @return List các tác giả sinh năm đó
     */
    List<Author> findByBirthYear(Integer birthYear);
    
    /**
     * Tìm tác giả có nhiều sách nhất
     * @param limit Số lượng tác giả cần lấy
     * @return List các tác giả có nhiều sách
     */
    List<Author> findMostProductiveAuthors(int limit);
    
    /**
     * Tìm tác giả theo từ khóa (tìm trong name và biography)
     * @param keyword Từ khóa tìm kiếm
     * @return List các tác giả phù hợp
     */
    List<Author> searchAuthors(String keyword);
    
    /**
     * Đếm số tác giả theo quốc tịch
     * @param nationality Quốc tịch
     * @return Số lượng tác giả có quốc tịch đó
     */
    long countByNationality(String nationality);
    
    /**
     * Kiểm tra tác giả có sách nào không
     * @param authorId ID tác giả
     * @return true nếu có sách, false nếu không
     */
    boolean hasBooks(Long authorId);
}
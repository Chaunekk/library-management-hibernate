package com.library.repository;

import com.library.entity.Book;
import java.util.List;

/**
 * Repository interface cho Book entity
 * Chứa các phương thức đặc biệt cho Book
 */
public interface BookRepository extends BaseRepository<Book, Long> {
    
    /**
     * Tìm sách theo tiêu đề
     * @param title Tiêu đề sách
     * @return List các sách có tiêu đề tương tự
     */
    List<Book> findByTitle(String title);
    
    /**
     * Tìm sách theo ISBN
     * @param isbn Mã ISBN
     * @return Book nếu tìm thấy, null nếu không
     */
    Book findByIsbn(String isbn);
    
    /**
     * Tìm sách theo thể loại
     * @param category Thể loại sách
     * @return List các sách thuộc thể loại đó
     */
    List<Book> findByCategory(String category);
    
    /**
     * Tìm sách có sẵn (chưa được mượn)
     * @return List các sách có sẵn
     */
    List<Book> findAvailableBooks();
    
    /**
     * Tìm sách theo tác giả
     * @param authorName Tên tác giả
     * @return List các sách của tác giả đó
     */
    List<Book> findByAuthorName(String authorName);
    
    /**
     * Tìm sách phổ biến nhất (được mượn nhiều)
     * @param limit Số lượng sách cần lấy
     * @return List các sách phổ biến
     */
    List<Book> findMostPopularBooks(int limit);
    
    /**
     * Tìm sách theo từ khóa (tìm trong title và category)
     * @param keyword Từ khóa tìm kiếm
     * @return List các sách phù hợp
     */
    List<Book> searchBooks(String keyword);
    
    /**
     * Đếm số sách có sẵn
     * @return Số lượng sách có sẵn
     */
    long countAvailableBooks();
    
    /**
     * Đếm số sách theo thể loại
     * @param category Thể loại
     * @return Số lượng sách thuộc thể loại đó
     */
    long countByCategory(String category);
}
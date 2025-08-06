package com.library.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface cơ bản cho tất cả Repository classes
 * Định nghĩa các phương thức CRUD cơ bản
 * @param <T> Entity type
 * @param <ID> Primary key type
 */
public interface BaseRepository<T, ID> {
    
    /**
     * Lưu entity vào database
     * @param entity Entity cần lưu
     * @return Entity đã được lưu
     */
    T save(T entity);
    
    /**
     * Cập nhật entity trong database
     * @param entity Entity cần cập nhật
     * @return Entity đã được cập nhật
     */
    T update(T entity);
    
    /**
     * Xóa entity khỏi database
     * @param entity Entity cần xóa
     */
    void delete(T entity);
    
    /**
     * Xóa entity theo ID
     * @param id ID của entity cần xóa
     */
    void deleteById(ID id);
    
    /**
     * Tìm entity theo ID
     * @param id ID cần tìm
     * @return Optional chứa entity nếu tìm thấy
     */
    Optional<T> findById(ID id);
    
    /**
     * Lấy tất cả entities
     * @return List chứa tất cả entities
     */
    List<T> findAll();
    
    /**
     * Đếm tổng số entities
     * @return Số lượng entities
     */
    long count();
    
    /**
     * Kiểm tra entity có tồn tại theo ID không
     * @param id ID cần kiểm tra
     * @return true nếu tồn tại, false nếu không
     */
    boolean existsById(ID id);
}
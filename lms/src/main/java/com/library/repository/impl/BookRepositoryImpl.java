package com.library.repository.impl;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementation của BookRepository
 * Thực hiện các phương thức thao tác với Book entity
 */
public class BookRepositoryImpl implements BookRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(BookRepositoryImpl.class);
    
    @Override
    public Book save(Book book) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
            logger.info("Đã lưu sách: {}", book.getTitle());
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi lưu sách: {}", e.getMessage());
            throw new RuntimeException("Không thể lưu sách", e);
        }
    }
    
    @Override
    public Book update(Book book) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(book);
            transaction.commit();
            logger.info("Đã cập nhật sách: {}", book.getTitle());
            return book;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi cập nhật sách: {}", e.getMessage());
            throw new RuntimeException("Không thể cập nhật sách", e);
        }
    }
    
    @Override
    public void delete(Book book) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(book);
            transaction.commit();
            logger.info("Đã xóa sách: {}", book.getTitle());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa sách: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa sách", e);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                session.delete(book);
                logger.info("Đã xóa sách có ID: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa sách theo ID: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa sách", e);
        }
    }
    
    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Book book = session.get(Book.class, id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách theo ID: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<Book> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book", Book.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi lấy tất cả sách: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách sách", e);
        }
    }
    
    @Override
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Book b", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm sách: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Book b WHERE b.bookId = :id", Long.class);
            query.setParameter("id", id);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra sách tồn tại: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Book> findByTitle(String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book b WHERE b.title LIKE :title", Book.class);
            query.setParameter("title", "%" + title + "%");
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách theo tiêu đề: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm sách theo tiêu đề", e);
        }
    }
    
    @Override
    public Book findByIsbn(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book b WHERE b.isbn = :isbn", Book.class);
            query.setParameter("isbn", isbn);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách theo ISBN: {}", e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Book> findByCategory(String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book b WHERE b.category = :category", Book.class);
            query.setParameter("category", category);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách theo thể loại: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm sách theo thể loại", e);
        }
    }
    
    @Override
    public List<Book> findAvailableBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery("FROM Book b WHERE b.available = true", Book.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách có sẵn: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm sách có sẵn", e);
        }
    }
    
    @Override
    public List<Book> findByAuthorName(String authorName) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery(
                "SELECT DISTINCT b FROM Book b JOIN b.authors a WHERE a.name LIKE :authorName", 
                Book.class
            );
            query.setParameter("authorName", "%" + authorName + "%");
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách theo tác giả: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm sách theo tác giả", e);
        }
    }
    
    @Override
    public List<Book> findMostPopularBooks(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery(
                "FROM Book b ORDER BY b.borrowCount DESC", 
                Book.class
            );
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách phổ biến: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm sách phổ biến", e);
        }
    }
    
    @Override
    public List<Book> searchBooks(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Book> query = session.createQuery(
                "FROM Book b WHERE b.title LIKE :keyword OR b.category LIKE :keyword", 
                Book.class
            );
            query.setParameter("keyword", "%" + keyword + "%");
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm sách: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm kiếm sách", e);
        }
    }
    
    @Override
    public long countAvailableBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Book b WHERE b.available = true", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm sách có sẵn: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public long countByCategory(String category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Book b WHERE b.category = :category", Long.class);
            query.setParameter("category", category);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm sách theo thể loại: {}", e.getMessage());
            return 0;
        }
    }
}
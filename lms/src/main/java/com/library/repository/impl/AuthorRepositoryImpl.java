package com.library.repository;

import com.library.entity.Author;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementation của AuthorRepository
 * Thực hiện các phương thức thao tác với Author entity
 */
public class AuthorRepositoryImpl implements AuthorRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthorRepositoryImpl.class);
    
    @Override
    public Author save(Author author) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(author);
            transaction.commit();
            logger.info("Đã lưu tác giả: {}", author.getName());
            return author;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi lưu tác giả: {}", e.getMessage());
            throw new RuntimeException("Không thể lưu tác giả", e);
        }
    }
    
    @Override
    public Author update(Author author) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(author);
            transaction.commit();
            logger.info("Đã cập nhật tác giả: {}", author.getName());
            return author;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi cập nhật tác giả: {}", e.getMessage());
            throw new RuntimeException("Không thể cập nhật tác giả", e);
        }
    }
    
    @Override
    public void delete(Author author) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(author);
            transaction.commit();
            logger.info("Đã xóa tác giả: {}", author.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa tác giả: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa tác giả", e);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Author author = session.get(Author.class, id);
            if (author != null) {
                session.delete(author);
                logger.info("Đã xóa tác giả có ID: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa tác giả theo ID: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa tác giả", e);
        }
    }
    
    @Override
    public Optional<Author> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Author author = session.get(Author.class, id);
            return Optional.ofNullable(author);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm tác giả theo ID: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<Author> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Author> query = session.createQuery("FROM Author", Author.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi lấy tất cả tác giả: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách tác giả", e);
        }
    }
    
    @Override
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(a) FROM Author a", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm tác giả: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(a) FROM Author a WHERE a.authorId = :id", Long.class);
            query.setParameter("id", id);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra tác giả tồn tại: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Author> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Author> query = session.createQuery("FROM Author a WHERE a.name LIKE :name", Author.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm tác giả theo tên: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm tác giả theo tên", e);
        }
    }
    
    @Override
    public List<Author> findByNationality(String nationality) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Author> query = session.createQuery("FROM Author a WHERE a.nationality = :nationality", Author.class);
            query.setParameter("nationality", nationality);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm tác giả theo quốc tịch: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm tác giả theo quốc tịch", e);
        }
    }
    
    @Override
    public List<Author> findByBirthYear(Integer birthYear) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Author> query = session.createQuery("FROM Author a WHERE a.birthYear = :birthYear", Author.class);
            query.setParameter("birthYear", birthYear);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm tác giả theo năm sinh: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm tác giả theo năm sinh", e);
        }
    }
    
    @Override
    public List<Author> findMostProductiveAuthors(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Author> query = session.createQuery(
                "SELECT a FROM Author a ORDER BY SIZE(a.books) DESC", 
                Author.class
            );
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm tác giả có nhiều sách: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm tác giả có nhiều sách", e);
        }
    }
    
    @Override
    public List<Author> searchAuthors(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Author> query = session.createQuery(
                "FROM Author a WHERE a.name LIKE :keyword OR a.biography LIKE :keyword", 
                Author.class
            );
            query.setParameter("keyword", "%" + keyword + "%");
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm tác giả: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm kiếm tác giả", e);
        }
    }
    
    @Override
    public long countByNationality(String nationality) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(a) FROM Author a WHERE a.nationality = :nationality", 
                Long.class
            );
            query.setParameter("nationality", nationality);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm tác giả theo quốc tịch: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean hasBooks(Long authorId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(b) FROM Book b JOIN b.authors a WHERE a.authorId = :authorId", 
                Long.class
            );
            query.setParameter("authorId", authorId);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra tác giả có sách: {}", e.getMessage());
            return false;
        }
    }
}
package com.library.repository;

import com.library.entity.Borrowing;
import com.library.entity.Borrowing.BorrowingStatus;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation của BorrowingRepository
 * Thực hiện các phương thức thao tác với Borrowing entity
 */
public class BorrowingRepositoryImpl implements BorrowingRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(BorrowingRepositoryImpl.class);
    
    @Override
    public Borrowing save(Borrowing borrowing) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(borrowing);
            transaction.commit();
            logger.info("Đã lưu phiếu mượn ID: {}", borrowing.getBorrowingId());
            return borrowing;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi lưu phiếu mượn: {}", e.getMessage());
            throw new RuntimeException("Không thể lưu phiếu mượn", e);
        }
    }
    
    @Override
    public Borrowing update(Borrowing borrowing) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(borrowing);
            transaction.commit();
            logger.info("Đã cập nhật phiếu mượn ID: {}", borrowing.getBorrowingId());
            return borrowing;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi cập nhật phiếu mượn: {}", e.getMessage());
            throw new RuntimeException("Không thể cập nhật phiếu mượn", e);
        }
    }
    
    @Override
    public void delete(Borrowing borrowing) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(borrowing);
            transaction.commit();
            logger.info("Đã xóa phiếu mượn ID: {}", borrowing.getBorrowingId());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa phiếu mượn: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa phiếu mượn", e);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Borrowing borrowing = session.get(Borrowing.class, id);
            if (borrowing != null) {
                session.delete(borrowing);
                logger.info("Đã xóa phiếu mượn có ID: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa phiếu mượn theo ID: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa phiếu mượn", e);
        }
    }
    
    @Override
    public Optional<Borrowing> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Borrowing borrowing = session.get(Borrowing.class, id);
            return Optional.ofNullable(borrowing);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo ID: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<Borrowing> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery("FROM Borrowing", Borrowing.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi lấy tất cả phiếu mượn: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách phiếu mượn", e);
        }
    }
    
    @Override
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Borrowing b", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm phiếu mượn: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Borrowing b WHERE b.borrowingId = :id", Long.class);
            query.setParameter("id", id);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra phiếu mượn tồn tại: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Borrowing> findByMemberId(Long memberId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery("FROM Borrowing b WHERE b.member.memberId = :memberId", Borrowing.class);
            query.setParameter("memberId", memberId);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo thành viên: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn theo thành viên", e);
        }
    }
    
    @Override
    public List<Borrowing> findByBookId(Long bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery("FROM Borrowing b WHERE b.book.bookId = :bookId", Borrowing.class);
            query.setParameter("bookId", bookId);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo sách: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn theo sách", e);
        }
    }
    
    @Override
    public List<Borrowing> findByStatus(BorrowingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery("FROM Borrowing b WHERE b.status = :status", Borrowing.class);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo trạng thái: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn theo trạng thái", e);
        }
    }
    
    @Override
    public List<Borrowing> findActiveBorrowings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery("FROM Borrowing b WHERE b.status = :status", Borrowing.class);
            query.setParameter("status", BorrowingStatus.BORROWED);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn đang hoạt động: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn đang hoạt động", e);
        }
    }
    
    @Override
    public List<Borrowing> findOverdueBorrowings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery(
                "FROM Borrowing b WHERE b.status IN (:borrowed, :overdue) AND b.dueDate < :currentDate", 
                Borrowing.class
            );
            query.setParameter("borrowed", BorrowingStatus.BORROWED);
            query.setParameter("overdue", BorrowingStatus.OVERDUE);
            query.setParameter("currentDate", LocalDate.now());
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn quá hạn: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn quá hạn", e);
        }
    }
    
    @Override
    public List<Borrowing> findOverdueBorrowings(int days) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            LocalDate cutoffDate = LocalDate.now().minusDays(days);
            Query<Borrowing> query = session.createQuery(
                "FROM Borrowing b WHERE b.status IN (:borrowed, :overdue) AND b.dueDate < :cutoffDate", 
                Borrowing.class
            );
            query.setParameter("borrowed", BorrowingStatus.BORROWED);
            query.setParameter("overdue", BorrowingStatus.OVERDUE);
            query.setParameter("cutoffDate", cutoffDate);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn quá hạn {} ngày: {}", days, e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn quá hạn", e);
        }
    }
    
    @Override
    public List<Borrowing> findByBorrowDateBetween(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery(
                "FROM Borrowing b WHERE b.borrowDate BETWEEN :startDate AND :endDate", 
                Borrowing.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo khoảng thời gian mượn: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn theo khoảng thời gian", e);
        }
    }
    
    @Override
    public List<Borrowing> findByDueDateBetween(LocalDate startDate, LocalDate endDate) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery(
                "FROM Borrowing b WHERE b.dueDate BETWEEN :startDate AND :endDate", 
                Borrowing.class
            );
            query.setParameter("startDate", startDate);
            query.setParameter("endDate", endDate);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo khoảng thời gian hạn trả: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn theo khoảng thời gian hạn trả", e);
        }
    }
    
    @Override
    public List<Borrowing> findByMemberIdAndStatus(Long memberId, BorrowingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery(
                "FROM Borrowing b WHERE b.member.memberId = :memberId AND b.status = :status", 
                Borrowing.class
            );
            query.setParameter("memberId", memberId);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo thành viên và trạng thái: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn theo thành viên và trạng thái", e);
        }
    }
    
    @Override
    public List<Borrowing> findByBookIdAndStatus(Long bookId, BorrowingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Borrowing> query = session.createQuery(
                "FROM Borrowing b WHERE b.book.bookId = :bookId AND b.status = :status", 
                Borrowing.class
            );
            query.setParameter("bookId", bookId);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm phiếu mượn theo sách và trạng thái: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm phiếu mượn theo sách và trạng thái", e);
        }
    }
    
    @Override
    public long countByStatus(BorrowingStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Borrowing b WHERE b.status = :status", Long.class);
            query.setParameter("status", status);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm phiếu mượn theo trạng thái: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public long countByMemberId(Long memberId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(b) FROM Borrowing b WHERE b.member.memberId = :memberId", Long.class);
            query.setParameter("memberId", memberId);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm phiếu mượn theo thành viên: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public long countOverdueBorrowings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(b) FROM Borrowing b WHERE b.status IN (:borrowed, :overdue) AND b.dueDate < :currentDate", 
                Long.class
            );
            query.setParameter("borrowed", BorrowingStatus.BORROWED);
            query.setParameter("overdue", BorrowingStatus.OVERDUE);
            query.setParameter("currentDate", LocalDate.now());
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm phiếu mượn quá hạn: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public Double getTotalFineAmount() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Double> query = session.createQuery("SELECT SUM(b.fineAmount) FROM Borrowing b", Double.class);
            Double result = query.uniqueResult();
            return result != null ? result : 0.0;
        } catch (Exception e) {
            logger.error("Lỗi khi tính tổng tiền phạt: {}", e.getMessage());
            return 0.0;
        }
    }
    
    @Override
    public List<Object[]> findMostBorrowedBooks(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT b.book, COUNT(b) FROM Borrowing b GROUP BY b.book ORDER BY COUNT(b) DESC", 
                Object[].class
            );
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm sách được mượn nhiều nhất: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm sách được mượn nhiều nhất", e);
        }
    }
    
    @Override
    public List<Object[]> findMostActiveMembers(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Object[]> query = session.createQuery(
                "SELECT b.member, COUNT(b) FROM Borrowing b GROUP BY b.member ORDER BY COUNT(b) DESC", 
                Object[].class
            );
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên hoạt động nhiều nhất: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm thành viên hoạt động nhiều nhất", e);
        }
    }
}
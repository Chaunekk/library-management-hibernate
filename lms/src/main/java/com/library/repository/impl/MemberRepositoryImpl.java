package com.library.repository.impl;

import com.library.entity.Member;
import com.library.entity.Member.MemberStatus;
import com.library.repository.MemberRepository;
import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Implementation của MemberRepository
 * Thực hiện các phương thức thao tác với Member entity
 */
public class MemberRepositoryImpl implements MemberRepository {
    
    private static final Logger logger = LoggerFactory.getLogger(MemberRepositoryImpl.class);
    
    @Override
    public Member save(Member member) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(member);
            transaction.commit();
            logger.info("Đã lưu thành viên: {}", member.getName());
            return member;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi lưu thành viên: {}", e.getMessage());
            throw new RuntimeException("Không thể lưu thành viên", e);
        }
    }
    
    @Override
    public Member update(Member member) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(member);
            transaction.commit();
            logger.info("Đã cập nhật thành viên: {}", member.getName());
            return member;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi cập nhật thành viên: {}", e.getMessage());
            throw new RuntimeException("Không thể cập nhật thành viên", e);
        }
    }
    
    @Override
    public void delete(Member member) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(member);
            transaction.commit();
            logger.info("Đã xóa thành viên: {}", member.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa thành viên: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa thành viên", e);
        }
    }
    
    @Override
    public void deleteById(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Member member = session.get(Member.class, id);
            if (member != null) {
                session.delete(member);
                logger.info("Đã xóa thành viên có ID: {}", id);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Lỗi khi xóa thành viên theo ID: {}", e.getMessage());
            throw new RuntimeException("Không thể xóa thành viên", e);
        }
    }
    
    @Override
    public Optional<Member> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Member member = session.get(Member.class, id);
            return Optional.ofNullable(member);
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên theo ID: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<Member> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery("FROM Member", Member.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi lấy tất cả thành viên: {}", e.getMessage());
            throw new RuntimeException("Không thể lấy danh sách thành viên", e);
        }
    }
    
    @Override
    public long count() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(m) FROM Member m", Long.class);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm thành viên: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(m) FROM Member m WHERE m.memberId = :id", Long.class);
            query.setParameter("id", id);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra thành viên tồn tại: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<Member> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery("FROM Member m WHERE m.name LIKE :name", Member.class);
            query.setParameter("name", "%" + name + "%");
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên theo tên: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm thành viên theo tên", e);
        }
    }
    
    @Override
    public Member findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery("FROM Member m WHERE m.email = :email", Member.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên theo email: {}", e.getMessage());
            return null;
        }
    }
    
    @Override
    public Member findByPhone(String phone) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery("FROM Member m WHERE m.phone = :phone", Member.class);
            query.setParameter("phone", phone);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên theo số điện thoại: {}", e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Member> findByStatus(MemberStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery("FROM Member m WHERE m.status = :status", Member.class);
            query.setParameter("status", status);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên theo trạng thái: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm thành viên theo trạng thái", e);
        }
    }
    
    @Override
    public List<Member> findMembersWithActiveBorrowings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery("FROM Member m WHERE m.activeBorrowings > 0", Member.class);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên có sách đang mượn: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm thành viên có sách đang mượn", e);
        }
    }
    
    @Override
    public List<Member> findEligibleMembers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery(
                "FROM Member m WHERE m.status = :status AND m.activeBorrowings < 5", 
                Member.class
            );
            query.setParameter("status", MemberStatus.ACTIVE);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên có thể mượn sách: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm thành viên có thể mượn sách", e);
        }
    }
    
    @Override
    public List<Member> findMostActiveMembers(int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery(
                "FROM Member m ORDER BY m.totalBorrowed DESC", 
                Member.class
            );
            query.setMaxResults(limit);
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm thành viên hoạt động nhiều: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm thành viên hoạt động nhiều", e);
        }
    }
    
    @Override
    public List<Member> searchMembers(String keyword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Member> query = session.createQuery(
                "FROM Member m WHERE m.name LIKE :keyword OR m.email LIKE :keyword OR m.phone LIKE :keyword", 
                Member.class
            );
            query.setParameter("keyword", "%" + keyword + "%");
            return query.list();
        } catch (Exception e) {
            logger.error("Lỗi khi tìm kiếm thành viên: {}", e.getMessage());
            throw new RuntimeException("Không thể tìm kiếm thành viên", e);
        }
    }
    
    @Override
    public long countByStatus(MemberStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM Member m WHERE m.status = :status", 
                Long.class
            );
            query.setParameter("status", status);
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm thành viên theo trạng thái: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public long countMembersWithActiveBorrowings() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM Member m WHERE m.activeBorrowings > 0", 
                Long.class
            );
            return query.uniqueResult();
        } catch (Exception e) {
            logger.error("Lỗi khi đếm thành viên có sách đang mượn: {}", e.getMessage());
            return 0;
        }
    }
    
    @Override
    public boolean existsByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM Member m WHERE m.email = :email", 
                Long.class
            );
            query.setParameter("email", email);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra email tồn tại: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean existsByPhone(String phone) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(m) FROM Member m WHERE m.phone = :phone", 
                Long.class
            );
            query.setParameter("phone", phone);
            return query.uniqueResult() > 0;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra số điện thoại tồn tại: {}", e.getMessage());
            return false;
        }
    }
}
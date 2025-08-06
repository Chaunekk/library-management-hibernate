package com.library.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class để quản lý Hibernate SessionFactory
 * Sử dụng Singleton pattern để đảm bảo chỉ có 1 SessionFactory trong toàn bộ app
 */
public class HibernateUtil {
    // Logger để ghi log các hoạt động
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    // Private constructor để ngăn tạo instance
    private HibernateUtil() {
        // Utility class không cần constructor public
    }

    // SessionFactory instance - static để dùng chung cho toàn bộ app
    private static SessionFactory sessionFactory;

    // Static block - chạy 1 lần khi class được load
    static {
        try {
            // Tạo SessionFactory từ file hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
            logger.info("Hibernate SessionFactory created successfully");
        } catch (Exception e) {
            // Log lỗi và throw exception nếu không tạo được SessionFactory
            logger.error("Failed to create SessionFactory", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Lấy SessionFactory instance
     * @return SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Đóng SessionFactory khi ứng dụng kết thúc
     */
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
            logger.info("Hibernate SessionFactory closed");
        }
    }
}
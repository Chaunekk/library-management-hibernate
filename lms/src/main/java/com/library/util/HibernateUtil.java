package com.library.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate Utility class để quản lý SessionFactory (Singleton pattern).
 */
public final class HibernateUtil {

    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

    // Singleton instance
    private static final SessionFactory sessionFactory = buildSessionFactory();

    // Private constructor để ngăn tạo instance
    private HibernateUtil() {
    }

    /**
     * Khởi tạo SessionFactory từ file hibernate.cfg.xml.
     */
    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml"); // nạp cấu hình từ classpath

            // Nếu dùng annotated entity, có thể thêm vào đây
            // configuration.addAnnotatedClass(Book.class);
            // configuration.addAnnotatedClass(Author.class);

            logger.info("Hibernate SessionFactory created successfully");
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Initial SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Lấy SessionFactory (dùng chung cho toàn ứng dụng).
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Đóng SessionFactory khi ứng dụng kết thúc.
     */
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            logger.info("Hibernate SessionFactory closed");
        }
    }
}
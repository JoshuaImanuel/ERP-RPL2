package com.erp.model;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration()
                    .configure()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Karyawan.class)
                    .addAnnotatedClass(Barang.class)
                    .addAnnotatedClass(Pembelian.class)
                    .addAnnotatedClass(Penjualan.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Gagal membuat SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
package com.crypto_feed_saver.hibernate_utils;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtils {
    private static SessionFactory sessionFactory;

    private HibernateUtils() {
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        StandardServiceRegistry configuration = new StandardServiceRegistryBuilder()
                .configure().build();

        sessionFactory = new MetadataSources(configuration).buildMetadata().buildSessionFactory();

        return sessionFactory;
    }
}

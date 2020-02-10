package com.crypto_feed_saver.repository;

import com.crypto_feed_saver.domain.Quote;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateQuoteRepository implements QuoteRepository {
    private static final Logger log = LoggerFactory.getLogger(QuoteRepository.class);
    private final SessionFactory sessionFactory;

    public HibernateQuoteRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Quote quote) throws Exception {
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.save(quote);
            transaction.commit();
            log.info("Quote {} successfully saved into DB", quote);
        } catch (Exception e) {
            transaction.rollback();
            throw new Exception(e);
        }
    }
}



package com.crypto_feed_saver.repository;

import com.crypto_feed_saver.domain.Quote;

public interface QuoteRepository {
    void save(Quote quote) throws Exception;
}

package com.crypto_feed_saver.services;

import com.crypto_feed_saver.converter.TickerConverter;
import com.crypto_feed_saver.domain.Quote;
import com.crypto_feed_saver.domain.TickersBuffer;
import com.crypto_feed_saver.repository.QuoteRepository;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BufferHandler {
    private static final Logger log = LoggerFactory.getLogger(BufferHandler.class);
    private final QuoteRepository repository;
    private final TickerConverter converter;

    public BufferHandler(QuoteRepository repository, TickerConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public void handle(TickersBuffer buffer, String exchangeName) {
        if (buffer.isEmpty()) {
            log.info("Skip empty {} buffer", exchangeName);
            return;
        }
        buffer.getTicks().stream()
                .map(ticker -> convertTick(ticker, exchangeName))
                .filter(this::validate)
                .forEach(this::save);

        buffer.clear();
    }

    private void save(Quote quote) {
        try {
            repository.save(quote);
        } catch (Exception e) {
            log.error("Failed to save {} into DB", quote, e);
        }
    }

    private boolean validate(Quote quote) {
        return quote != null;
    }

    private Quote convertTick(Ticker ticker, String exchangeName) {
        try {
            Quote quote = converter.convert(ticker, exchangeName);
            log.trace("{} was converted to the quote", ticker);
            return quote;
        } catch (Exception e) {
            log.error("Can not convert {} to the quote", ticker, e);
            // do not throw exception to avoid loosing remaining ticks.
            // stream use filter to skip null quotes.
            return null;
        }
    }
}

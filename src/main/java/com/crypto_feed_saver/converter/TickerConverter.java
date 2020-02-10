package com.crypto_feed_saver.converter;

import com.crypto_feed_saver.domain.Quote;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.time.Instant;

public class TickerConverter {
    public Quote convert(Ticker ticker, String exchangeName) {
        return Quote.builder().setTimestamp(Instant.now())
                .setAsk(ticker.getAsk())
                .setBid(ticker.getBid())
                .setExchangeName(exchangeName)
                .setName(ticker.getCurrencyPair().toString())
                .build();
    }
}
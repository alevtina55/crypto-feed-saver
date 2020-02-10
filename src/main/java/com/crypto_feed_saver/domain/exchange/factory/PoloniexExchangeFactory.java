package com.crypto_feed_saver.domain.exchange.factory;

import com.crypto_feed_saver.domain.exchange.DefaultExchange;
import com.crypto_feed_saver.domain.exchange.Exchange;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import info.bitrich.xchangestream.poloniex2.PoloniexStreamingExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoloniexExchangeFactory implements ExchangeFactory {
    private final static Logger log = LoggerFactory.getLogger(PoloniexExchangeFactory.class);

    @Override
    public Exchange create() {
        StreamingExchange streamingExchange = StreamingExchangeFactory.INSTANCE
                .createExchange(PoloniexStreamingExchange.class.getName());
        String name = "POLONIEX";
        Exchange exchange = new DefaultExchange(streamingExchange, name);

        log.info("New {} exchange created", name);

        return exchange;
    }
}

package com.crypto_feed_saver.domain.exchange;

import info.bitrich.xchangestream.core.ProductSubscription;
import info.bitrich.xchangestream.core.ProductSubscription.ProductSubscriptionBuilder;
import info.bitrich.xchangestream.core.StreamingExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BinanceExchange extends DefaultExchange {
    private static final Logger log = LoggerFactory.getLogger(BinanceExchange.class);
    private static final int CONNECT_TIMEOUT_S = 30;
    private final List<CurrencyPair> currencyPairs;

    public BinanceExchange(StreamingExchange exchange, String name, List<CurrencyPair> currencyPairs) {
        super(exchange, name);
        this.currencyPairs = currencyPairs;
    }

    @Override
    public void connect() {
        getExchange()
                .connect(createProductSubscription())
                .timeout(CONNECT_TIMEOUT_S, TimeUnit.SECONDS)
                .blockingAwait();
        log.info("Exchange connected: {}", getName());
    }

    private ProductSubscription createProductSubscription() {
        ProductSubscriptionBuilder builder = ProductSubscription.create();

        currencyPairs.forEach(builder::addTicker);
        return builder.build();
    }
}

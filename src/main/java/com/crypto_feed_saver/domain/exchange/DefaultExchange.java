package com.crypto_feed_saver.domain.exchange;

import info.bitrich.xchangestream.core.StreamingExchange;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DefaultExchange implements Exchange {
    private static final Logger log = LoggerFactory.getLogger(DefaultExchange.class);
    private static final int CONNECT_TIMEOUT_S = 30;
    private final StreamingExchange exchange;
    private final String name;

    public DefaultExchange(StreamingExchange exchange, String name) {
        this.exchange = exchange;
        this.name = name;
    }

    @Override
    public void connect() {
        exchange.connect().timeout(CONNECT_TIMEOUT_S, TimeUnit.SECONDS).blockingAwait();
        log.info("Connected: {}", name);
    }

    @Override
    public Observable<Ticker> getTickUpdates(CurrencyPair currencyPair) {
        return exchange.getStreamingMarketDataService().getTicker(currencyPair);
    }

    @Override
    public void disconnect() {
        exchange.disconnect();
    }

    public StreamingExchange getExchange() {
        return exchange;
    }

    public String getName() {
        return name;
    }
}

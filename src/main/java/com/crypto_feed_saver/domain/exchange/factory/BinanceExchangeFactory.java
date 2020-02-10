package com.crypto_feed_saver.domain.exchange.factory;

import com.crypto_feed_saver.domain.exchange.BinanceExchange;
import com.crypto_feed_saver.domain.exchange.Exchange;
import info.bitrich.xchangestream.binance.BinanceStreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchange;
import info.bitrich.xchangestream.core.StreamingExchangeFactory;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BinanceExchangeFactory implements ExchangeFactory {
    private final static Logger log = LoggerFactory.getLogger(BinanceExchangeFactory.class);
    private final List<CurrencyPair> currencyPairs;

    public BinanceExchangeFactory(List<CurrencyPair> currencyPairs) {
        this.currencyPairs = currencyPairs;
    }

    @Override
    public Exchange create() {
        StreamingExchange streamingExchange = StreamingExchangeFactory.INSTANCE
                .createExchange(BinanceStreamingExchange.class.getName());
        String name = "BINANCE";
        Exchange exchange = new BinanceExchange(streamingExchange, name, currencyPairs);

        log.info("New {} exchange was created", name);

        return exchange;
    }
}

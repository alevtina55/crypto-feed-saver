package com.crypto_feed_saver.domain.exchange;

import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

public interface Exchange {
    void connect();

    Observable<Ticker> getTickUpdates(CurrencyPair currencyPair);

    String getName();

    void disconnect();
}

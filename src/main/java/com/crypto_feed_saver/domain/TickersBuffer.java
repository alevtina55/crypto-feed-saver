package com.crypto_feed_saver.domain;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TickersBuffer {
    private final Map<CurrencyPair, Ticker> buffer = new HashMap<>();

    public void add(Ticker ticker) {
        buffer.put(ticker.getCurrencyPair(), ticker);
    }

    public List<Ticker> getTicks() {
        return new ArrayList<>(buffer.values());
    }

    public void clear() {
        buffer.clear();
    }

    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public String toString() {
        return "TickersBuffer{" + "buffer=" + buffer +
                '}';
    }
}

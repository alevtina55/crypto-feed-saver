package com.crypto_feed_saver.settings;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.knowm.xchange.currency.CurrencyPair;

import java.util.List;

public class Settings {
    private final List<CurrencyPair> instruments;
    private final long flushPeriodS;

    public Settings(@JsonProperty("flush_period_s") long flushPeriodS,
                    @JsonProperty("instruments") List<CurrencyPair> instruments) {
        this.flushPeriodS = flushPeriodS;
        this.instruments = instruments;
    }

    public long getFlushPeriodS() {
        return flushPeriodS;
    }

    public List<CurrencyPair> getCurrencyPairs() {
        return instruments;
    }

    public void validate() {
        checkFlushPeriod();
        checkInstruments();
    }

    private void checkFlushPeriod() {
        if (flushPeriodS <= 0) {
            throw new IllegalSettingsException("Wrong flush period");
        }
    }

    private void checkInstruments() {
        if (instruments.isEmpty()) {
            throw new IllegalSettingsException("Empty instruments list");
        }
    }

    @Override
    public String toString() {
        return "Settings{" + "flushPeriod=" + flushPeriodS +
                ", instruments=" + instruments +
                '}';
    }
}

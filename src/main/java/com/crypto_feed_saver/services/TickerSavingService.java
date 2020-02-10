package com.crypto_feed_saver.services;

import com.crypto_feed_saver.domain.TickersBuffer;
import com.crypto_feed_saver.domain.exchange.Exchange;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.schedulers.Schedulers;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class TickerSavingService implements AutoCloseable {
    private final Logger log;
    private final CompositeDisposable compositeDisposable;
    private final Exchange exchange;
    private final List<CurrencyPair> currencyPairs;
    private final TickersBuffer ticksBuffer;
    private final long flushPeriodS;

    private final BufferHandler bufferHandler;

    public TickerSavingService(Exchange exchange, List<CurrencyPair> currencyPairs, long flushPeriod,
                               BufferHandler bufferHandler) {
        this(exchange, currencyPairs, flushPeriod, bufferHandler, new TickersBuffer());
    }

    private TickerSavingService(Exchange exchange, List<CurrencyPair> currencyPairs, long flushPeriod,
                                BufferHandler bufferHandler, TickersBuffer ticksBuffer) {
        this.exchange = exchange;
        this.currencyPairs = currencyPairs;
        this.flushPeriodS = flushPeriod;
        this.bufferHandler = bufferHandler;
        this.ticksBuffer = ticksBuffer;
        this.compositeDisposable = new CompositeDisposable();
        this.log = LoggerFactory.getLogger(exchange.getName() + "-" + TickerSavingService.class.getSimpleName());
    }

    public void run() {
        log.info("Ticker saving service is running");
        connectExchange();
        currencyPairs.forEach(this::startObservingTicks);

        Disposable disposable = Schedulers.single()
                .schedulePeriodicallyDirect(this::handleBuffer, flushPeriodS, flushPeriodS, TimeUnit.SECONDS);

        compositeDisposable.add(disposable);
    }

    private void handleBuffer() {
        try {
            bufferHandler.handle(ticksBuffer, exchange.getName());
        } catch (Exception e) {
            log.error("Failed to handle buffer", e);
        }
    }

    private void connectExchange() {
        try {
            exchange.connect();
        } catch (Exception e) {
            Exceptions.propagate(new Exception("Failed connect to" + exchange.getName(), e));
        }
    }

    private void startObservingTicks(CurrencyPair currencyPair) {
        Disposable disposable = exchange.getTickUpdates(currencyPair)
                .observeOn(Schedulers.single())
                .filter(this::validateTicker)
                .subscribe(this::handleTicker, e -> log.error("Error receiving ticker: ", e));

        compositeDisposable.add(disposable);
    }

    private boolean validateTicker(Ticker ticker) {
        return Objects.nonNull(ticker.getAsk()) && Objects.nonNull(ticker.getBid())
                && Objects.nonNull(ticker.getCurrencyPair());
    }

    private void handleTicker(Ticker ticker) {
        try {
            log.debug("Tick received: {}", ticker);
            saveToBuffer(ticker);
        } catch (Exception e) {
            log.error("Failed to handle ticker: {}", ticker, e);
        }
    }

    private void saveToBuffer(Ticker ticker) {
        ticksBuffer.add(ticker);
    }

    @Override
    public void close() {
        log.info("Closing");
        compositeDisposable.dispose();
        compositeDisposable.clear();
        exchange.disconnect();
        log.info("Cleanup completed");
    }
}

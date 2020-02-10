package com.crypto_feed_saver;

import com.crypto_feed_saver.converter.TickerConverter;
import com.crypto_feed_saver.domain.exchange.factory.BinanceExchangeFactory;
import com.crypto_feed_saver.domain.exchange.factory.ExchangeFactory;
import com.crypto_feed_saver.domain.exchange.factory.PoloniexExchangeFactory;
import com.crypto_feed_saver.hibernate_utils.HibernateUtils;
import com.crypto_feed_saver.repository.HibernateQuoteRepository;
import com.crypto_feed_saver.repository.QuoteRepository;
import com.crypto_feed_saver.services.BufferHandler;
import com.crypto_feed_saver.services.TickerSavingService;
import com.crypto_feed_saver.settings.Settings;
import com.crypto_feed_saver.settings.SettingsReader;
import org.knowm.xchange.currency.CurrencyPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(Main::onUncaughtException);

        Settings settings = SettingsReader.read("settings.yaml", Settings.class);
        settings.validate();
        log.info("Settings are successfully loaded: {}", settings);

        List<CurrencyPair> currencyPairs = settings.getCurrencyPairs();
        QuoteRepository quoteRepository = new HibernateQuoteRepository(HibernateUtils.getSessionFactory());
        TickerConverter converter = new TickerConverter();

        Stream.of(new PoloniexExchangeFactory(), new BinanceExchangeFactory(currencyPairs))
                .map(ExchangeFactory::create)
                .map(exchange -> new TickerSavingService(exchange, currencyPairs, settings.getFlushPeriodS(),
                                                         new BufferHandler(quoteRepository, converter)))
                .forEach(Main::runTickerSavingService);

    }

    private static void runTickerSavingService(TickerSavingService service) {
        service.run();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> close(service)));
    }

    private static void close(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            log.error("Problem while closing", e);
        }
    }

    private static void onUncaughtException(Thread thread, Throwable e) {
        log.error("Thread: {}, Uncaught error: ", thread, e);
    }
}
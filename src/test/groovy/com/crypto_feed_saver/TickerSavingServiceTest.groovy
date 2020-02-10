package com.crypto_feed_saver

import com.crypto_feed_saver.domain.TickersBuffer
import com.crypto_feed_saver.domain.exchange.Exchange
import com.crypto_feed_saver.services.BufferHandler
import com.crypto_feed_saver.services.TickerSavingService
import io.reactivex.Observable
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.marketdata.Ticker
import spock.lang.Specification

class TickerSavingServiceTest extends Specification {
    def exchange = Mock(Exchange)
    def ticksBuffer = Mock(TickersBuffer)
    def handler = Mock(BufferHandler)
    def instrument = Mock(CurrencyPair)
    def instrument1 = Mock(CurrencyPair)
    def instruments = [instrument, instrument1]

    def "should run without exceptions"() {
        given:
        def service = new TickerSavingService(exchange, instruments, 1, handler, ticksBuffer)
        def ticker = new Ticker.Builder()
                .ask(BigDecimal.TEN)
                .bid(BigDecimal.ONE)
                .currencyPair(CurrencyPair.BTC_USD)
                .build()

        def ticker1 = new Ticker.Builder()
                .ask(BigDecimal.TEN)
                .bid(BigDecimal.ONE)
                .currencyPair(CurrencyPair.ETH_USD)
                .build()

        1 * exchange.connect()
        _ * exchange.getName() >> "name"

        1 * exchange.getTickUpdates(instrument) >> Observable.just(ticker1).repeat(2)
        1 * exchange.getTickUpdates(instrument1) >> Observable.just(ticker).repeat(2)

        4 * ticksBuffer.add(_)
        1 * handler.handle(_, "name")
        0 * exchange._

        when:
        service.run()

        then:
        Thread.sleep(1100)

        noExceptionThrown()
    }
}

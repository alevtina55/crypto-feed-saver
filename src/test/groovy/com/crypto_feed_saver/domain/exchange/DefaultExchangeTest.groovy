package com.crypto_feed_saver.domain.exchange

import info.bitrich.xchangestream.core.StreamingExchange
import info.bitrich.xchangestream.core.StreamingMarketDataService
import io.reactivex.Completable
import org.knowm.xchange.currency.CurrencyPair
import spock.lang.Specification

class DefaultExchangeTest extends Specification {
    def exchange = Mock(StreamingExchange)
    def defExchange = new DefaultExchange(exchange, "name")

    def "verify that connect was called"() {
        when:
        defExchange.connect()

        then:
        1 * exchange.connect() >> Completable.complete()
        0 * exchange._

        noExceptionThrown()
    }

    def "verify getting ticker"() {
        given:
        def currencyPair = CurrencyPair.BTC_USD
        def marketDataService = Mock(StreamingMarketDataService)

        when:
        defExchange.getTickUpdates(currencyPair)

        then:
        1 * exchange.getStreamingMarketDataService() >> marketDataService
        1 * marketDataService.getTicker(CurrencyPair.BTC_USD)
        0 * exchange._
        0 * marketDataService._

        noExceptionThrown()
    }

    def "should verify disconnect was called"() {
        when:
        defExchange.disconnect()

        then:
        1 * exchange.disconnect()
        0 * exchange._

        noExceptionThrown()
    }
}

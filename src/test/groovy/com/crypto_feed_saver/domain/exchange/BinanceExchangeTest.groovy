package com.crypto_feed_saver.domain.exchange

import info.bitrich.xchangestream.core.StreamingExchange
import io.reactivex.Completable
import org.knowm.xchange.currency.CurrencyPair
import spock.lang.Specification

class BinanceExchangeTest extends Specification {
    def streamingExchange = Mock(StreamingExchange)
    def name = "name"
    def currencyPair1 = CurrencyPair.ETH_USD
    def currencyPair2 = CurrencyPair.BTC_USD
    def currencyPairs = [currencyPair1, currencyPair2]

    def binanceExchange = new BinanceExchange(streamingExchange, name, currencyPairs)

    def "should verify connect was called"() {
        when:
        binanceExchange.connect()

        then:
        1 * streamingExchange.connect({
            it.ticker.containsAll(currencyPairs) && it.ticker.size == currencyPairs.size()
        }) >> Completable.complete()

        0 * streamingExchange._
    }
}

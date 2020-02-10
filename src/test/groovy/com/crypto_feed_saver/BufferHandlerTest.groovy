package com.crypto_feed_saver

import com.crypto_feed_saver.converter.TickerConverter
import com.crypto_feed_saver.domain.Quote
import com.crypto_feed_saver.domain.TickersBuffer
import com.crypto_feed_saver.repository.QuoteRepository
import com.crypto_feed_saver.services.BufferHandler
import org.knowm.xchange.currency.CurrencyPair
import org.knowm.xchange.dto.marketdata.Ticker
import spock.lang.Specification

import java.time.Instant

class BufferHandlerTest extends Specification {
    def repository = Mock(QuoteRepository)
    def converter = Mock(TickerConverter)
    def buffer = new TickersBuffer()
    def handler = new BufferHandler(repository, converter)
    Ticker ticker
    Ticker ticker1
    Quote quote
    Quote quote1

    def setup() {
        ticker = new Ticker.Builder()
                .ask(BigDecimal.TEN)
                .bid(BigDecimal.ONE)
                .currencyPair(CurrencyPair.BTC_USD)
                .build()

        ticker1 = new Ticker.Builder()
                .ask(BigDecimal.TEN)
                .bid(BigDecimal.ONE)
                .currencyPair(CurrencyPair.ETH_BTC)
                .build()

        quote = Quote.builder()
                .setAsk(BigDecimal.TEN)
                .setBid(BigDecimal.ONE)
                .setExchangeName("exchangeName")
                .setName(CurrencyPair.BTC_USD.toString())
                .setTimestamp(Instant.now()).build()

        quote1 = Quote.builder()
                .setAsk(BigDecimal.TEN)
                .setBid(BigDecimal.ONE)
                .setExchangeName("exchangeName")
                .setName(CurrencyPair.ETH_BTC.toString())
                .setTimestamp(Instant.now()).build()
    }

    def "should verify repository and converter was not called"() {
        when:
        handler.handle(buffer, "exchangeName")

        then:
        0 * repository._
        0 * converter._
    }

    def "should verify repository and converter was called"() {
        given:
        buffer.add(ticker)
        buffer.add(ticker1)

        when:
        handler.handle(buffer, "exchangeName")

        then:
        1 * converter.convert(ticker, "exchangeName") >> quote
        1 * converter.convert(ticker1, "exchangeName") >> quote1

        1 * repository.save(quote)
        1 * repository.save(quote1)

        0 * converter._
        0 * repository._
    }
}

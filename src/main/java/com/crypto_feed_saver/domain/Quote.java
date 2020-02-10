package com.crypto_feed_saver.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "QUOTES")
public class Quote {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "TIME", nullable = false, updatable = false)
    private Instant timestamp;

    @Column(name = "BID", scale = 12, nullable = false, updatable = false)
    private BigDecimal bid;

    @Column(name = "ASK", scale = 12, nullable = false, updatable = false)
    private BigDecimal ask;

    @Column(name = "EXCHANGE", nullable = false, length = 20, updatable = false)
    private String exchangeName;

    @Column(name = "NAME", nullable = false, length = 20, updatable = false)
    private String name;

    public Quote() {

    }

    private Quote(QuoteBuilder builder) {
        this.timestamp = builder.timestamp;
        this.bid = builder.bid;
        this.ask = builder.ask;
        this.exchangeName = builder.exchangeName;
        this.name = builder.name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public void setAsk(BigDecimal ask) {
        this.ask = ask;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Quote{" + "timestamp=" + timestamp +
                ", bid=" + bid +
                ", ask=" + ask +
                ", exchangeName='" + exchangeName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static QuoteBuilder builder() {
        return new QuoteBuilder();
    }

    public static class QuoteBuilder {
        private Instant timestamp;
        private BigDecimal bid;
        private BigDecimal ask;
        private String exchangeName;
        private String name;

        public QuoteBuilder setTimestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public QuoteBuilder setBid(BigDecimal bid) {
            this.bid = bid;
            return this;
        }

        public QuoteBuilder setAsk(BigDecimal ask) {
            this.ask = ask;
            return this;
        }

        public QuoteBuilder setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
            return this;
        }

        public QuoteBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public Quote build() {
            return new Quote(this);
        }
    }
}

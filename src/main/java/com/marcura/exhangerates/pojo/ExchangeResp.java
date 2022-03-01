package com.marcura.exhangerates.pojo;

import java.math.BigDecimal;

public class ExchangeResp {
    private String from;
    private String to;
    private BigDecimal exchange;


    public ExchangeResp(String from, String to, BigDecimal exchange) {
        this.from = from;
        this.to = to;
        this.exchange = exchange;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }
}

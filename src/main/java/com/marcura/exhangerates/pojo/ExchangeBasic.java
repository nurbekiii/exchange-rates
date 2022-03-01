package com.marcura.exhangerates.pojo;

import com.marcura.exhangerates.entity.ExchangeRate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class ExchangeBasic {
    private Boolean success;
    private Long timestamp;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;

    public ExchangeBasic(Boolean success, Long timestamp, String base, String date, Map rates) {
        this.success = success;
        this.timestamp = timestamp;
        this.base = base;
        this.date = date;
        this.rates = rates;
    }

    public ExchangeRate getExchangeRate() {
        //default, ISO_LOCAL_DATE
        LocalDate date1 = LocalDate.parse(date);

        String rates_ex = getRatesAsString();

        ExchangeRate rate = new ExchangeRate(null, timestamp, base, date1, rates_ex);

        return rate;
    }

    public String getRatesAsString() {
        String mapAsString = rates.keySet().stream()
                .map(key -> "\"" + key + "\"" + ":" + rates.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map getRates() {
        return rates;
    }

    public void setRates(Map rates) {
        this.rates = rates;
    }
}

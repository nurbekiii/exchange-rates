package com.marcura.exhangerates.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exchange_rates")
public class ExchangeRate {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd";

    @Id
    @SequenceGenerator(name = "exchange_rates_gen", sequenceName = "exchange_rates_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "exchange_rates_gen")
    private Integer id;

    @Column(name = "timestamp_date")
    private Long timestampDate;

    @Column(name = "base")
    private String base;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    @Column(name = "reg_date")
    private LocalDate regDate;

    @Column(name = "rates")
    private String rates;

    public ExchangeRate() {

    }

    public ExchangeRate(Integer id, Long timestampDate, String base, LocalDate regDate, String rates) {
        this.id = id;
        this.timestampDate = timestampDate;
        this.base = base;
        this.regDate = regDate;
        this.rates = rates;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTimestampDate() {
        return timestampDate;
    }

    public void setTimestampDate(Long timestampDate) {
        this.timestampDate = timestampDate;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", timestampDate=" + timestampDate +
                ", base='" + base + '\'' +
                ", regDate=" + regDate +
                ", rates='" + rates + '\'' +
                '}';
    }
}

package com.marcura.exhangerates.service.exchange;


import com.marcura.exhangerates.entity.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExchangeRateService {

    ExchangeRate createExchangeRate(ExchangeRate rate);

    Optional<ExchangeRate> getLatestExchangeRateByBaseToDate(String base, LocalDate date);

    List<ExchangeRate> getExchangeRateByBaseToDate(String base, LocalDate date);

    List<ExchangeRate> getExchangeRateByBase(String base);

    List<ExchangeRate> getExchangeRates();

    Page<ExchangeRate> getAllExchangeRates(Pageable pageable);
}
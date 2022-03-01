package com.marcura.exhangerates.service.exchange;

import com.marcura.exhangerates.entity.ExchangeRate;
import com.marcura.exhangerates.repos.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Override
    public List<ExchangeRate> getExchangeRateByBase(String base) {
        return exchangeRateRepository.getExchangeRateByBase(base);
    }

    @Override
    public Optional<ExchangeRate> getLatestExchangeRateByBaseToDate(String base, LocalDate date) {
        //get latest rate for the base currency to DATE
        List<ExchangeRate> list = exchangeRateRepository.getLatestExchangeRateByBaseAndRegDateOrderByIdDesc(base, date);
        if (list != null && list.size() > 0) {
            return Optional.of(list.get(0));
        }

        return Optional.ofNullable(null);
    }

    @Override
    public List<ExchangeRate> getExchangeRateByBaseToDate(String base, LocalDate date) {
        return exchangeRateRepository.getExchangeRateByBaseAndRegDate(base, date);
    }

    @Override
    public ExchangeRate createExchangeRate(ExchangeRate rate) {
        //get existing rate if present in DB
        if (rate.getId() != null) {
            ExchangeRate entity = exchangeRateRepository.getExchangeRateById(rate.getId());
            if (entity != null)
                return entity;
        }

        //save new exchange rate
        ExchangeRate rate1 = new ExchangeRate();
        rate1.setBase(rate.getBase());
        rate1.setTimestampDate(rate.getTimestampDate());
        rate1.setRegDate(rate.getRegDate());
        rate1.setRates(rate.getRates());
        return exchangeRateRepository.save(rate1);
    }

    @Override
    public List<ExchangeRate> getExchangeRates() {
        return exchangeRateRepository.findAll();
    }

    @Override
    public Page<ExchangeRate> getAllExchangeRates(Pageable pageable) {
        return exchangeRateRepository.findAll(pageable);
    }
}

package com.marcura.exhangerates.service.rate_update;

import com.marcura.exhangerates.entity.ExchangeRate;
import com.marcura.exhangerates.service.exchange.ExchangeRateService;
import com.marcura.exhangerates.service.rest.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@EnableScheduling
@Service
public class RateUpdaterServiceImpl implements RateUpdaterService {

    @Autowired
    private RestService restService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    //@Scheduled(cron="*/10 * * * * *", zone="GMT") //every ten seconds.
    @Scheduled(cron="0 5 0 * * *", zone="GMT") // 12:05 AM or 00:05
    public Boolean updateRates() {

        ExchangeRate rate = restService.getExchangeRate("EUR");
        System.out.println(rate.toString());
        exchangeRateService.createExchangeRate(rate);

        return true;
    }
}

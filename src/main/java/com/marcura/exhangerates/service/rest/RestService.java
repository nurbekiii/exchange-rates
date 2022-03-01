package com.marcura.exhangerates.service.rest;

import com.marcura.exhangerates.entity.ExchangeRate;
import com.marcura.exhangerates.pojo.ExchangeBasic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    @Value("${exchange.base_url}")
    private String fixerUrl;

    private final RestTemplate restTemplate;

    public RestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ExchangeRate getExchangeRate(String base) {
        //get rates from http://data.fixer.io

        String url = String.format("%s&base=%s", fixerUrl, base);
        System.out.println(url);

        //remote call
        ExchangeBasic basicInfo = restTemplate.getForObject(url, ExchangeBasic.class);
        if (basicInfo.getSuccess()) {
            //got json response
            return basicInfo.getExchangeRate();
        }
        return null;
    }

}
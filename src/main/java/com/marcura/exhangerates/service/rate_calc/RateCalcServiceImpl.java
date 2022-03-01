package com.marcura.exhangerates.service.rate_calc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcura.exhangerates.entity.ExchangeRate;
import com.marcura.exhangerates.exception.InvalidParameterException;
import com.marcura.exhangerates.exception.NotFoundException;
import com.marcura.exhangerates.pojo.ExchangeResp;
import com.marcura.exhangerates.service.exchange.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RateCalcServiceImpl implements RateCalcService {
    @Autowired
    private ExchangeRateService exchangeRateService;

    private static Map<String, Double> spreads = new HashMap<>();


    @PostConstruct
    private void holdSpreads() {
        spreads.put("USD", 0d);
        spreads.put("JPY,HKD,KRW", 3.25);
        spreads.put("MYR,INR,MXN", 4.50);
        spreads.put("RUB,CNY,ZAR", 6.00);
        spreads.put("OTHER", 2.75);
    }

    private Double getCurrencySpread(String curr) {
        for (Map.Entry<String, Double> entry : spreads.entrySet()) {
            if (entry.getKey().contains(curr))
                return entry.getValue();
        }
        return spreads.get("OTHER");
    }


    @Override
    public Optional<ExchangeResp> getExchangeRateByBaseToDate(String fromCurr, String toCurr, String date) throws InvalidParameterException, NotFoundException {
        ExchangeResp resp;

        if (fromCurr == null || fromCurr.isEmpty())
            throw new InvalidParameterException("Parameter [from] is invalid");

        if (toCurr == null || toCurr.isEmpty())
            throw new InvalidParameterException("Parameter [to] is invalid");

        String dateStr = Optional.ofNullable(date).orElse(LocalDate.now().toString());
        LocalDate dateFin = LocalDate.parse(dateStr);

        String from = fromCurr.toUpperCase();
        String to = toCurr.toUpperCase();

        if (from.equals(to)) {
            resp = new ExchangeResp(from, to, BigDecimal.valueOf(1.0));
            return Optional.of(resp);
        }

        //calculate rate
        if (from.equals("EUR")) {
            resp = getRateForEURBase(from, to, dateFin);
        } else {
            resp = getRateForOtherBase(from, to, dateFin);
        }

        //currencies found, rate is calculated
        if (resp != null)
            return Optional.of(resp);

        //if nothing found, then throw exception
        //throw new NotFoundException("Error: exchange rate for currencies " + from + ", " + to + " not found for the given date " + dateFin);
        throwException(from, to, dateFin);

        return null;
    }

    private ExchangeResp getRateForEURBase(String from, String to, LocalDate date) {
        //Get rate
        ExchangeRate rate = getRateFromDB(from, date);
        if (rate != null) {
            String rates = rate.getRates();
            try {
                //json string to Map<currency, rate>
                Map<String, Double> map = getRatesMap(rates);
                Double rateVal = map.get(to);
                ExchangeResp resp = new ExchangeResp(from, to, BigDecimal.valueOf(rateVal));
                return resp;
            } catch (Exception t) {
                //t.printStackTrace();
            }
        }
        return null;
    }

    private ExchangeResp getRateForOtherBase(String from, String to, LocalDate date) {
        //Get rate
        ExchangeRate rate = getRateFromDB("EUR", date);
        if (rate != null) {
            String rates = rate.getRates();
            try {
                //json string to Map<currency, rate>
                Map<String, Double> map = getRatesMap(rates);
                //rate for Currency1
                Double rateVal1 = map.get(from);
                BigDecimal rateVal11 = BigDecimal.valueOf(rateVal1);
                //rate for Currency2
                Double rateVal2 = !to.equals("EUR") ? map.get(to) : 1d; //if to=EUR then EUR to EUR is 1
                BigDecimal rateVal22 = BigDecimal.valueOf(rateVal2);

                if (rateVal1 == null || rateVal2 == null)
                    throwException(from, to, date); //rates are not found, maybe incorrect Currencies sent

                //get spread of each currency
                Double spread1 = getCurrencySpread(from);
                Double spread2 = getCurrencySpread(to);

                //Max of 2 spreads
                BigDecimal maxSpread = BigDecimal.valueOf(spread1 > spread2 ? spread1 : spread2);
                BigDecimal percent100 = BigDecimal.valueOf(100d);
                BigDecimal percentDiff = percent100.subtract(maxSpread);

                //Ratio calculated by Appendix 2 formula
                BigDecimal ratio = rateVal22.divide(rateVal11, 10, RoundingMode.CEILING);
                BigDecimal rateVal = ratio.multiply(percentDiff).divide(percent100);

                ExchangeResp resp = new ExchangeResp(from, to, rateVal);
                return resp;
            } catch (Exception t) {
                //t.printStackTrace();
            }
        }
        return null;
    }

    private void throwException(String from, String to, LocalDate date) throws NotFoundException {
        throw new NotFoundException("Error: exchange rate for currencies " + from + ", " + to + " not found for the given date " + date);
    }

    private ExchangeRate getRateFromDB(String base, LocalDate date) {
        //rate from DB for EUR as Base
        Optional<ExchangeRate> opt = exchangeRateService.getLatestExchangeRateByBaseToDate(base, date);
        if (opt != null && opt.isPresent()) {
            return opt.get();
        }
        return null;
    }

    private Map<String, Double> getRatesMap(String jsonStr) throws JsonProcessingException {

        HashMap<String, Double> map = new ObjectMapper().readValue(jsonStr, HashMap.class);
        return map;
    }
}

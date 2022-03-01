package com.marcura.exhangerates.service.rate_calc;

import com.marcura.exhangerates.exception.InvalidParameterException;
import com.marcura.exhangerates.exception.NotFoundException;
import com.marcura.exhangerates.pojo.ExchangeResp;

import java.util.Optional;

public interface RateCalcService {

    Optional<ExchangeResp> getExchangeRateByBaseToDate(String fromCurr, String toCurr, String date) throws InvalidParameterException, NotFoundException;
}
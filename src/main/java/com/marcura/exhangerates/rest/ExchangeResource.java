package com.marcura.exhangerates.rest;

import com.marcura.exhangerates.exception.InvalidParameterException;
import com.marcura.exhangerates.exception.NotFoundException;
import com.marcura.exhangerates.pojo.ExchangeResp;
import com.marcura.exhangerates.service.rate_calc.RateCalcService;
import com.marcura.exhangerates.service.rate_update.RateUpdaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class ExchangeResource {
    private final RateCalcService rateCalcService;
    private final RateUpdaterService rateUpdaterService;

    public ExchangeResource(RateCalcService rateCalcService, RateUpdaterService rateUpdaterService) {
        this.rateCalcService = rateCalcService;
        this.rateUpdaterService = rateUpdaterService;
    }

    @GetMapping("/exchange")
    public ResponseEntity<ExchangeResp> getExchangeRateInfo(@RequestParam String from, @RequestParam String to, @RequestParam(required = false) String date) throws InvalidParameterException, NotFoundException {
        //GET /exchange?from=EUR&to=PLN&date=2020-12-05
        Optional<ExchangeResp> resp = rateCalcService.getExchangeRateByBaseToDate(from, to, date);
        if (resp.isPresent())
            return new ResponseEntity<>(resp.get(), HttpStatus.OK);

        throw new NotFoundException("Invalid params supplied");
    }

    @PutMapping("/exchange")
    public ResponseEntity<String> updateRates() {
        boolean res = rateUpdaterService.updateRates();
        String result = (res ? "Exchange rates are updated" : "Exchange rates are NOT updated");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/exchange555")
    public ResponseEntity<String> updateRatesEx() {
        boolean res = rateUpdaterService.updateRates();
        String result = (res ? "Exchange rates are updated" : "Exchange rates are NOT updated");
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleException(NotFoundException ex) {
        //Data not Found
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<String> handleExceptionEx(InvalidParameterException ex) {
        //Invalid parameters sent
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); //HttpStatus.FORBIDDEN
    }
}

package com.marcura.exhangerates.repos;

import com.marcura.exhangerates.entity.ExchangeRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Integer> {
    Page<ExchangeRate> findAll(Pageable pageable);

    List<ExchangeRate> getExchangeRateByBase(String base);

    List<ExchangeRate> getExchangeRateByBaseAndRegDate(String base, LocalDate regDate);

    List<ExchangeRate> getLatestExchangeRateByBaseAndRegDateOrderByIdDesc(String base, LocalDate regDate);

    ExchangeRate getExchangeRateById(int id);
}


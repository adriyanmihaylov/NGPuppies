package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.web.dto.CurrencyDto;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.services.base.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public List<Currency> getAll() {
        return currencyRepository.getAll();
    }

    @Override
    public Currency getByName(String name) {
        if (name == null) {
            return null;
        }

        return currencyRepository.getByName(name);
    }

    @Override
    public boolean create(String currencyName, double fixing) throws InvalidParameterException, SQLException {
        if (currencyName == null) {
            throw new InvalidParameterException("Please enter currency name!");
        }
        if (fixing < 0) {
            throw new InvalidParameterException("Currency fixing can not be negative!");
        }

        Currency currency = new Currency(currencyName, fixing);
        return currencyRepository.create(currency);
    }

    @Override
    public List<CurrencyDto> updateFixings(List<CurrencyDto> currencyDtoList) {
        List<CurrencyDto> failedCurrencies = new ArrayList<>();
        for (CurrencyDto currencyDto : currencyDtoList) {
            if (currencyDto.getName() == null || currencyDto.getFixing() < 0) {
                failedCurrencies.add(currencyDto);
                continue;
            }
            if (!currencyRepository.update(currencyDto.getName(), currencyDto.getFixing())) {
                failedCurrencies.add(currencyDto);
            }
        }

        return failedCurrencies;
    }
}

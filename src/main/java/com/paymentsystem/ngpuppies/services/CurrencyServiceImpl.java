package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
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
    public List<Currency> updateFixings(List<Currency> currencies) {
        List<Currency> failedCurrencies = new ArrayList<>();
        for (Currency currency : currencies) {
            if (currency.getName() == null || currency.getFixing() < 0) {
                failedCurrencies.add(currency);
                continue;
            }
            if (!currencyRepository.update(currency.getName(), currency.getFixing())) {
                failedCurrencies.add(currency);
            }
        }

        return failedCurrencies;
    }
}

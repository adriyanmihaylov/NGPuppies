package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.services.base.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return currencyRepository.getByName(name);
    }

    @Override
    public boolean create(Currency currency) throws Exception {
        return currencyRepository.create(currency);
    }

    @Override
    public boolean delete(Currency currency) {
        return currencyRepository.delete(currency);
    }
}

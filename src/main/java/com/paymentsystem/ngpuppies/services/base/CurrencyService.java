package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAll();

    Currency getByName(String name);

    boolean create(Currency currency) throws Exception;

    boolean delete(Currency currency);
}

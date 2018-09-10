package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyService {
    List<Currency> getAll();

    Currency getByName(String name);

    boolean create(String currencyName, double fixing) throws SQLException;

    List<Currency> updateFixings(List<Currency> list);
}
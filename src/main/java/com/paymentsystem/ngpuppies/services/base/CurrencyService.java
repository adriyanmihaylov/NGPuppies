package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyService {
    List<Currency> getAll();

    Currency getByName(String name);

    boolean create(Currency currency) throws SQLException;

    boolean delete(Currency currency);
}

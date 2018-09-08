package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Currency;

import java.sql.SQLException;
import java.util.List;

public interface CurrencyRepository {
    List<Currency> getAll();

    Currency getByName(String name);

    boolean create(Currency currency) throws SQLException;

    boolean update(Currency currency);
}

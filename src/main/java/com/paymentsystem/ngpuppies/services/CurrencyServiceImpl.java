package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.web.dto.CurrencyDTO;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.services.base.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return currencyRepository.getByName(name);
    }

    @Override
    public boolean create(Currency currency) throws SQLException {
        return currencyRepository.create(currency);
    }

    @Override
    public List<CurrencyDTO> updateFixings(List<CurrencyDTO> currencyDTOList) {
        List<CurrencyDTO> failedCurrencies = new ArrayList<>();
        for (CurrencyDTO currencyDTO : currencyDTOList) {
            Currency currency = currencyRepository.getByName(currencyDTO.getName());

            if (currency == null) {
                failedCurrencies.add(currencyDTO);
                continue;
            }
            try {
                currency.setFixing(currencyDTO.getFixing());
                if (!currencyRepository.update(currency)) {
                    failedCurrencies.add(currencyDTO);
                }
            } catch (Exception e) {
                failedCurrencies.add(currencyDTO);
            }
        }

        return failedCurrencies;
    }
}

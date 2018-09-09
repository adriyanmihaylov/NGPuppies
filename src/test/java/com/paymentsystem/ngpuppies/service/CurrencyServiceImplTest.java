package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.services.CurrencyServiceImpl;
import com.paymentsystem.ngpuppies.web.dto.CurrencyDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private Currency currency;
    private CurrencyDto currencyDto;
    private List<Currency> currencyList;
    @Before
    public void beforeTest() {
        currencyList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            currencyList.add(new Currency("Name",1.95 + i) );
        }
        currency = new Currency("EUR",1.95);
        currency.setId(1);

        currencyDto = new CurrencyDto("NEW EUR",1.99);
    }

    @Test
    public void getAll_onSuccess_shouldReturnListOfCurrency() {
        when(currencyRepository.getAll()).thenReturn(currencyList);

        List<Currency> result = currencyService.getAll();

        Assert.assertEquals(currencyList,result);
    }
    @Test
    public void getByName_whenServiceNameIsNull_shouldReturnNull() {
        Currency result = currencyService.getByName(null);

        Assert.assertNull(result);
    }

    @Test
    public void getByName_onSuccess_shouldReturnCurrency() {
        when(currencyRepository.getByName(currency.getName())).thenReturn(currency);

        Currency result = currencyService.getByName(currency.getName());

        Assert.assertEquals(currency, result);
    }

    @Test(expected = InvalidParameterException.class)
    public void create_whenNameIsNull_shouldThrowException() throws SQLException {
        currencyService.create(null,1.99);
    }

    @Test(expected = InvalidParameterException.class)
    public void create_whenFixingIsNegative_shouldThrowException() throws SQLException {
        currencyService.create("name",-1.99);
    }

    @Test
    public void create_onSuccess_shouldReturnTrue() throws SQLException {
        when(currencyRepository.create(any(Currency.class))).thenReturn(true);

        boolean result = currencyService.create("name", 1.954);

        Assert.assertTrue(result);
    }

    @Test
    public void updateFixings_whenCurrenciesNamesAreNull_shouldReturnListOfThoseCurrencies() {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            currencyDtoList.add(new CurrencyDto(null, 1.955));
        }

        List<CurrencyDto> resultList = currencyService.updateFixings(currencyDtoList);

        Assert.assertEquals(currencyDtoList.size(), resultList.size());
    }
    @Test
    public void updateFixings_whenCurrenciesFixingsAreNegative_shouldReturnListOfThisCurrencies() {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            currencyDtoList.add(new CurrencyDto("name", -1.955));
        }

        List<CurrencyDto> resultList = currencyService.updateFixings(currencyDtoList);

        Assert.assertEquals(currencyDtoList.size(), resultList.size());
    }

    @Test
    public void updateFixings_whenRepositoryReturnsFalse_shouldReturnListOfFailedCurrencies() throws SQLException {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            currencyDtoList.add(new CurrencyDto("name", 1.955));
        }
        when(currencyRepository.update(any(String.class),any(double.class))).thenReturn(false);

        List<CurrencyDto> resultList = currencyService.updateFixings(currencyDtoList);

        Assert.assertEquals(currencyDtoList.size(), resultList.size());
    }

    @Test
    public void updateFixings_whenPassedInvalidCurrencies_shouldReturnListOfThisCurrencies() throws SQLException {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            currencyDtoList.add(new CurrencyDto("name", 1.955));
        }
        int expectedResult = 4;
        for (int i = 0; i < expectedResult; i++) {
            currencyDtoList.add(new CurrencyDto(null, 1.99));
        }

        when(currencyRepository.update(any(String.class),any(double.class))).thenReturn(true);

        List<CurrencyDto> resultList = currencyService.updateFixings(currencyDtoList);

        Assert.assertEquals(expectedResult, resultList.size());
    }

    @Test
    public void updateFixings_onSuccess_shouldReturnEmptyList() throws SQLException {
        List<CurrencyDto> currencyDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            currencyDtoList.add(new CurrencyDto("name", 1.955));
        }
        when(currencyRepository.update(any(String.class),any(double.class))).thenReturn(true);

        List<CurrencyDto> resultList = currencyService.updateFixings(currencyDtoList);

        Assert.assertEquals(0, resultList.size());
    }

}

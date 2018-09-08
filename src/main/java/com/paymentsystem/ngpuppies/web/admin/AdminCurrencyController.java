package com.paymentsystem.ngpuppies.web.admin;


import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.web.dto.CurrencyDTO;
import com.paymentsystem.ngpuppies.web.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.web.dto.ValidList;
import com.paymentsystem.ngpuppies.services.base.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/currency")
public class AdminCurrencyController {
    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/all")
    public ResponseEntity<List<Currency>> getAllCurrencies() {
        List<Currency> currencies = currencyService.getAll();

        return new ResponseEntity<>(currencies, HttpStatus.OK);
    }

    @GetMapping("/update1")
    public ValidList<CurrencyDTO> getCurrencyUpdateTemplate() {
        ValidList<CurrencyDTO> validList = new ValidList<>();
        for (int i = 0; i < 2; i++) {
            validList.add(new CurrencyDTO());
        }

        return validList;
    }

    @PutMapping("/update")
    public ResponseEntity<?> setFixing(@RequestBody @Valid ValidList<CurrencyDTO> currencyValidList,
                                       BindingResult bindingResult) {
        List<CurrencyDTO> invalid = currencyService.updateFixings(currencyValidList.getList());
        if (invalid.size() > 0) {
            return new ResponseEntity<>(invalid, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createCurrency(@RequestBody @Valid CurrencyDTO currencyDTO,
                                                          BindingResult bindingResult) {
        try {
            Currency currency = new Currency(currencyDTO.getName());
            currencyService.create(currency);

            return new ResponseEntity<>(new ResponseMessage("Currency created!"), HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
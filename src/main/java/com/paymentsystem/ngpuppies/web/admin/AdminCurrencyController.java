package com.paymentsystem.ngpuppies.web.admin;


import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.web.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.validation.ValidList;
import com.paymentsystem.ngpuppies.services.base.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.InvalidParameterException;
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

    @GetMapping("/update")
    public ValidList<Currency> getCurrencyUpdateTemplate() {
        ValidList<Currency> validList = new ValidList<>();
        for (int i = 0; i < 2; i++) {
            validList.add(new Currency());
        }

        return validList;
    }

    @PutMapping("/update")
    public ResponseEntity<?> setFixing(@RequestBody @Valid ValidList<Currency> currencyValidList,
                                       BindingResult bindingResult) {
        List<Currency> invalid = currencyService.updateFixings(currencyValidList.getList());
        if (invalid.size() > 0) {
            return new ResponseEntity<>(invalid, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createCurrency(@RequestBody @Valid Currency currencyDto,
                                                          BindingResult bindingResult) {
        try {
            if(currencyService.create(currencyDto.getName(),currencyDto.getFixing())) {
                return new ResponseEntity<>(HttpStatus.OK);
            }

            return new ResponseEntity<>(new ResponseMessage("Currency created!"), HttpStatus.OK);
        } catch (InvalidParameterException | SQLException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
package com.paymentsystem.ngpuppies.web.admin;


import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.dto.CurrencyDTO;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.dto.ValidList;
import com.paymentsystem.ngpuppies.services.base.CurrencyService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @GetMapping("/set/fixing")
    public ValidList<CurrencyDTO> getCurrencyUpdateTemplate() {
        ValidList<CurrencyDTO> list = new ValidList<>();
        for (int i = 0; i < 2; i++) {
            list.add(new CurrencyDTO());
        }
        return list;
    }

    @PutMapping("/set/fixing")
    public ResponseEntity<?> setFixing(@RequestBody @Valid ValidList<CurrencyDTO> currencyValidList,
                                       BindingResult bindingResult) {
        List<CurrencyDTO> invalid = currencyService.updateFixings(currencyValidList.getList());
        if (invalid.size() > 0) {
            return new ResponseEntity<>(invalid, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
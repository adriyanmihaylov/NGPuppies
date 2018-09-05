package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.services.base.CurrencyService;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.OfferedServicesService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.web.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/invoice")
public class AdminInvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private OfferedServicesService offeredServicesService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private SubscriberService subscriberService;

    private ResponseHandler responseHandler;

    private final DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

    @GetMapping("/generate")
    public InvoiceDTO createInvoice() {
        return new InvoiceDTO();
    }

    @PostMapping("/generate")
    public ResponseEntity<Response> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return responseHandler.bindingResultHandler(bindingResult);
        }
        try {

            Subscriber subscriber = subscriberService.getByNumber(invoiceDTO.getSubscriberPhone());
            if (subscriber == null) {
                return responseHandler.returnResponse("Subscriber phone not found!", HttpStatus.BAD_REQUEST);
            }

            OfferedServices offeredServices = offeredServicesService.getByName(invoiceDTO.getService().toUpperCase());
            if (offeredServices == null) {
                return responseHandler.returnResponse("Offered service not found!", HttpStatus.BAD_REQUEST);
            }

            Currency currency = currencyService.getByName(invoiceDTO.getCurrency().toUpperCase());
            if (currency == null) {
                return responseHandler.returnResponse("Currency not found!", HttpStatus.BAD_REQUEST);
            }

            Invoice invoice = new Invoice();
            invoice.setSubscriber(subscriber);
            invoice.setCurrency(currency);
            invoice.setStartDate(dateFormat.parse(invoiceDTO.getStartDate()));
            invoice.setEndDate(dateFormat.parse(invoiceDTO.getEndDate()));
            invoice.setAmount(Double.parseDouble(invoiceDTO.getAmount()));
            invoice.setOfferedServices(offeredServices);

            invoiceService.create(invoice);

        } catch (Exception e) {
            return responseHandler.returnResponse("Something went wrong! Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseHandler.returnResponse("Invoice successfully added!", HttpStatus.OK);
    }
}
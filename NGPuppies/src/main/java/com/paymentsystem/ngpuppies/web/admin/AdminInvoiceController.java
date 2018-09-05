package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.models.dto.ValidInvoiceList;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private ResponseHandler responseHandler;

    private final DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");

    @GetMapping("/generate")
    @ResponseBody
    public ValidInvoiceList<InvoiceDTO> createInvoice() {
        ValidInvoiceList<InvoiceDTO> invoiceDTOList = new ValidInvoiceList<>();
        for (int i = 0; i < 2; i++) {
            invoiceDTOList.add(new InvoiceDTO());
        }
        return invoiceDTOList;
    }

    @PostMapping("/generate")
    @ResponseBody
    public ResponseEntity<?> createInvoice(@RequestBody @Valid ValidInvoiceList<InvoiceDTO> invoiceDTOList, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            FieldError error = bindingResult.getFieldErrors().get(0);
            String message = error.getDefaultMessage();
            return new ResponseEntity<>(new Response(message), HttpStatus.BAD_REQUEST);
        }

        List<InvoiceDTO> failedInvoices = new ArrayList<>();
        try {

            for (InvoiceDTO invoiceDTO : invoiceDTOList.getList()) {

                if(!dateValidator(invoiceDTO.getStartDate(),invoiceDTO.getEndDate())) {
                    failedInvoices.add(invoiceDTO);
                    continue;
                }

                Subscriber subscriber = subscriberService.getByNumber(invoiceDTO.getSubscriberPhone());
                if (subscriber == null) {
                    failedInvoices.add(invoiceDTO);
                    continue;
                }
                OfferedServices offeredServices = offeredServicesService.getByName(invoiceDTO.getService());
                if (offeredServices == null) {
                    failedInvoices.add(invoiceDTO);
                    continue;
                }
                if (!subscriber.getSubscriberServices().contains(offeredServices)) {
                    failedInvoices.add(invoiceDTO);
                    continue;
                }

                Invoice invoice = new Invoice(subscriber,
                        dateFormat.parse(invoiceDTO.getStartDate()),
                        dateFormat.parse(invoiceDTO.getEndDate()),
                        Double.parseDouble(invoiceDTO.getAmountBGN()),
                        offeredServices);

                try {
                    if (!invoiceService.create(invoice)) {
                        failedInvoices.add(invoiceDTO);
                    }
                } catch (Exception e) {
                    failedInvoices.add(invoiceDTO);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(failedInvoices, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(failedInvoices, HttpStatus.OK);
    }

    private boolean dateValidator(String fromDate, String toDate) {
        if (fromDate.equals(toDate)) {
            return true;
        }
        return LocalDate.parse(fromDate)
                .isBefore(LocalDate.parse(toDate));
    }
}
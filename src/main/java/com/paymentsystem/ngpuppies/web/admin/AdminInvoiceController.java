package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.web.dto.InvoiceDto;
import com.paymentsystem.ngpuppies.web.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.validation.ValidList;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.models.viewModels.InvoiceViewModel;
import com.paymentsystem.ngpuppies.validation.anotations.ValidDate;
import com.paymentsystem.ngpuppies.validation.anotations.ValidPhone;
import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/invoice")
public class AdminInvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public AdminInvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping("/generate")
    public ValidList<InvoiceDto> createInvoice() {
        ValidList<InvoiceDto> invoiceDTOList = new ValidList<>();
        for (int i = 0; i < 2; i++) {
            invoiceDTOList.add(new InvoiceDto());
        }
        return invoiceDTOList;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> createInvoice(@Valid @RequestBody ValidList<InvoiceDto> invoiceDTOList,
                                           BindingResult bindingResult) {
        List<InvoiceDto> failedInvoices = new ArrayList<>();
        try {
            for (InvoiceDto invoiceDto : invoiceDTOList.getList()) {
                try {
                    InvoiceDto failedInvoice = invoiceService.create(invoiceDto);

                    if (failedInvoice != null) {
                        failedInvoices.add(failedInvoice);
                    }
                } catch (IllegalArgumentException e) {
                    //Date is invalid - startDate is after endDate
                    failedInvoices.add(invoiceDto);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(failedInvoices, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(failedInvoices, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInvoice(@RequestParam("id") @NotNull Integer id,
                                           @RequestParam("phone") @ValidPhone String subscriberPhone) {
        try {
            if (invoiceService.delete(id, subscriberPhone)) {
                return new ResponseEntity<>(new ResponseMessage("Invoice deleted!"), HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/unpaid/subscriber")
    public List<InvoiceViewModel> getAllUnpaidInvoicesOfSubscriberInDescOrder(@RequestParam("phone") @ValidPhone String phoneNumber) {
        return invoiceService.getAllUnpaidInvoicesOfSubscriberInDescOrder(phoneNumber).stream()
                .map(InvoiceViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<InvoiceViewModel>> getAllUnpaidInvoices() {
        try {
            return new ResponseEntity<>(invoiceService.getAllUnpaidInvoices()
                    .stream()
                    .map(InvoiceViewModel::fromModel)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/unpaid/from={from}")
    public ResponseEntity<List<InvoiceViewModel>> getAllUnpaidInvoicesFromDateToDate(@PathVariable("from") @ValidDate String fromDate,
                                                                                     @RequestParam("to") @ValidDate String toDate) {
        try {
            return new ResponseEntity<>(invoiceService.geAllUnpaidInvoicesFromDateToDate(fromDate, toDate)
                    .stream()
                    .map(InvoiceViewModel::fromModel)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/unpaid/service")
    public ResponseEntity<List<InvoiceViewModel>> getAllUnpaidInvoicesOfService(@RequestParam("name") @ValidServiceName String serviceName) {
        try {
            return new ResponseEntity<>(invoiceService.getAllUnpaidInvoicesOfService(serviceName)
                    .stream()
                    .map(InvoiceViewModel::fromModel)
                    .collect(Collectors.toList()), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{phone}/paid")
    public ResponseEntity<List<InvoiceViewModel>> getSubscriberPaidInvoicesFromDateToDate(@PathVariable("phone") @ValidPhone String subscriberPhone,
                                                                                          @RequestParam("from") @ValidDate String fromDate,
                                                                                          @RequestParam("to") @ValidDate String endDate) {

        try {
            List<Invoice> invoices = invoiceService.getSubscriberInvoicesFromDateToDate(subscriberPhone, fromDate, endDate);

            return new ResponseEntity<>(invoices.stream()
                    .map(InvoiceViewModel::fromModel)
                    .collect(Collectors.toList()),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
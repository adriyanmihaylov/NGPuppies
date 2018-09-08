package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.dto.ValidList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("${common.basepath}/invoice")
public class AdminInvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/generate")
    public ValidList<InvoiceDTO> createInvoice() {
        ValidList<InvoiceDTO> invoiceDTOList = new ValidList<>();
        for (int i = 0; i < 2; i++) {
            invoiceDTOList.add(new InvoiceDTO());
        }
        return invoiceDTOList;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> createInvoice(@Valid @RequestBody ValidList<InvoiceDTO> invoiceDTOList,
                                           BindingResult bindingResult) {
        List<InvoiceDTO> failedInvoices = new ArrayList<>();
        try {
            for (InvoiceDTO invoiceDTO : invoiceDTOList.getList()) {
                try {
                    InvoiceDTO failedInvoice = invoiceService.create(invoiceDTO);

                    if (failedInvoice != null) {
                        failedInvoices.add(failedInvoice);
                    }
                } catch (IllegalArgumentException e) {
                    //Date is invalid - startDate is after endDate
                    failedInvoices.add(invoiceDTO);
                }
            }
        } catch (Exception e) {
            return new ResponseEntity<>(failedInvoices, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(failedInvoices, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteInvoice(@RequestParam("id") int id,
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
            return new ResponseEntity<>(invoiceService.geAllUnpaidInvoicesFromDateToDate(fromDate,toDate)
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
}
package com.paymentsystem.ngpuppies.web.admin;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.models.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.models.dto.ValidList;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.models.viewModels.InvoiceViewModel;
import com.paymentsystem.ngpuppies.validation.anotations.ValidPhone;
import com.paymentsystem.ngpuppies.validation.anotations.ValidServiceName;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
                InvoiceDTO failedInvoice = invoiceService.create(invoiceDTO);

                if (failedInvoice != null) {
                    failedInvoices.add(failedInvoice);
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
        Invoice invoice = invoiceService.getById(id);

        if (invoice != null) {
            if (subscriberPhone.equals(invoice.getSubscriber().getPhone())) {
                if (invoiceService.delete(invoice)) {
                    return new ResponseEntity<>(new ResponseMessage("Invoice deleted!"), HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/unpaid/subscriber")
    public List<InvoiceViewModel> getAllUnpaidInvoicesOfSubscriberInDescOrder(@RequestParam("phone") @ValidPhone String phoneNumber) {
        return invoiceService.getAllUnpaidInvoicesOfSubscriberInDescOrder(phoneNumber).stream()
                .map(InvoiceViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/unpaid")
    public List<InvoiceViewModel> getAllUnpaidInvoices() {
        return invoiceService.getAllUnpaidInvoices()
                .stream()
                .map(InvoiceViewModel::fromModel)
                .collect(Collectors.toList());
    }

    @GetMapping("/unpaid/service")
    public List<InvoiceViewModel> getAllUnpaidInvoicesOfService(@RequestParam("name") @ValidServiceName String serviceName) {
        return invoiceService.getAllUnpaidInvoicesOfService(serviceName)
                .stream()
                .map(InvoiceViewModel::fromModel)
                .collect(Collectors.toList());
    }
}
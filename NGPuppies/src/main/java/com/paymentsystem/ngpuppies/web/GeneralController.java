package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.models.viewModels.InvoiceViewModel;
import com.paymentsystem.ngpuppies.models.viewModels.SubscriberViewModel;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.validator.base.ValidDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT')")
@RequestMapping("${common.basepath}")
@Validated
public class GeneralController {
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/subscriber/{phone}/invoice/completed")
    public ResponseEntity<List<InvoiceViewModel>> getSubscriberPaidInvoices(@PathVariable("phone") String subscriberPhone,
                                                                            @RequestParam("from") @ValidDate String fromDate,
                                                                            @RequestParam("to") @ValidDate String endDate,
                                                                            Authentication authentication) {
        try {
            if (!validateDate(fromDate, endDate)) {
                return new ResponseEntity("Invalid date", HttpStatus.BAD_REQUEST);
            }
            User loggedUser = null;
            try {
                loggedUser = (User) authentication.getPrincipal();
            } catch (Exception e) {
                return new ResponseEntity("Not a valid user", HttpStatus.BAD_REQUEST);
            }

            Subscriber subscriber = subscriberService.getSubscriberByPhone(subscriberPhone);
            switch (loggedUser.getAuthority().getName()) {
                case ROLE_CLIENT:
                    if (!subscriber.getClient().getUsername().equals(loggedUser.getUsername())) {
                        subscriber = null;
                    }
                    break;
            }

            if (subscriber != null) {
                List<Invoice> allPayedInvoices = invoiceService.getAllPaidInvoicesOfSubscriberInDescOrder(subscriber.getId(), fromDate, endDate);
                List<InvoiceViewModel> invoiceViewModels = new ArrayList<>();
                if (allPayedInvoices != null) {
                    invoiceViewModels = allPayedInvoices.stream()
                            .map(InvoiceViewModel::fromModel)
                            .collect(Collectors.toList());
                }
                return new ResponseEntity<>(invoiceViewModels, HttpStatus.OK);
            }
            return new ResponseEntity("Subscriber not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean validateDate(String start, String end) {
        if (start.equals(end)) {
            return true;
        }
        return LocalDate.parse(start)
                .isBefore(LocalDate.parse(end));
    }
}
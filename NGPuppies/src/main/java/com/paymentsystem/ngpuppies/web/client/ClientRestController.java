package com.paymentsystem.ngpuppies.web.client;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.models.viewModels.InvoiceViewModel;
import com.paymentsystem.ngpuppies.models.viewModels.SubscriberViewModel;
import com.paymentsystem.ngpuppies.models.viewModels.TopSubscriberViewModel;
import com.paymentsystem.ngpuppies.validator.base.ValidDate;
import com.paymentsystem.ngpuppies.validator.base.ValidPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${common.basepath}/client")
@PreAuthorize("hasRole('ROLE_CLIENT')")
@Validated
public class ClientRestController {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/subscriber")
    public SubscriberViewModel getSubscriberOfCurrentlyLoggedClient(@RequestParam("phone") @ValidPhone String phoneNumber,
                                                                    Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);
            if (subscriber != null) {
                if (subscriber.getClient().getId() != client.getId()) {
                    return null;
                }

                return SubscriberViewModel.fromModel(subscriber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/invoices/unpaid")
    public List<InvoiceViewModel> getAllUnpaidInvoices(Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            List<InvoiceViewModel> allUnpaidInvoicesOfCurrentClient =
                    invoiceService.geAllUnpaidInvoicesOfAllClientSubscribers(client.getId())
                            .stream()
                            .map(InvoiceViewModel::fromModel)
                            .collect(Collectors.toList());

            return allUnpaidInvoicesOfCurrentClient;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/subscriber/{phone}/invoices")
    public List<InvoiceViewModel> getAllInvoicesOfSubscriber(@PathVariable("phone") @ValidPhone String phoneNumber,
                                                             Authentication authentication) {
        try {
            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(phoneNumber, authentication);

            if (subscriber != null) {
                return invoiceService.getAllInvoicesOfSubscriberBySubscriberId(subscriber.id)
                        .stream()
                        .map(InvoiceViewModel::fromModel)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/subscriber/{phone}/invoices/unpaid")
    public List<InvoiceViewModel> getAllUnpaidInvoicesOfSubscriber(@PathVariable("phone") @ValidPhone String phoneNumber,
                                                                   Authentication authentication) {
        try {
            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(phoneNumber, authentication);

            if (subscriber != null) {
                return invoiceService.getAllUnpaidInvoicesOfSubscriberInDescOrder(subscriber.id)
                        .stream()
                        .map(InvoiceViewModel::fromModel)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @GetMapping("/subscriber/{phone}/average")
    public Double getSubscriberAverageSumOfPaidInvoices(@PathVariable("phone") String subscriberPhone,
                                                        @RequestParam("from") @ValidDate String fromDate,
                                                        @RequestParam("to") @ValidDate String endDate,
                                                        Authentication authentication) {
        try {
            if (!validateDate(fromDate,endDate)) {
                return 0D;
            }

            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(subscriberPhone, authentication);
            if (subscriber != null) {
                return subscriberService.getSubscriberAverageSumOfPaidInvoices(subscriber.id, fromDate, endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0D;
    }

    @GetMapping("/subscriber/{phone}/invoices/max")
    public InvoiceViewModel getSubscriberLargestPaidInvoice(@PathVariable("phone") @ValidPhone String subscriberPhone,
                                                            @RequestParam("from") @ValidDate String fromDate,
                                                            @RequestParam("to") @ValidDate String endDate,
                                                            Authentication authentication) {
        try {
            if (!validateDate(fromDate,endDate)) {
                return null;
            }

            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(subscriberPhone, authentication);
            if (subscriber != null) {
                return InvoiceViewModel.fromModel(invoiceService.getSubscriberLargestPaidInvoice(subscriber.id, fromDate, endDate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @PutMapping("/invoice/pay")
    public ResponseEntity<?> payInvoices(@RequestBody() List<Integer> invoicesIds, Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            List<Invoice> allInvoices = invoiceService.getInvoicesByIdAndClientId(invoicesIds, client.getId());

            if (allInvoices.size() > 0 && invoiceService.payInvoices(allInvoices)) {
                return ResponseEntity.ok("Invoices were payed!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong! Please try again later!");
        }

        return ResponseEntity.badRequest().body("Bills were not updated!");
    }

    @GetMapping("/invoice/last10")
    public List<InvoiceViewModel> getLastTenPayedInvoices(Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            return invoiceService.getTenMostRecentInvoices(client.getId())
                    .stream()
                    .map(InvoiceViewModel::fromModel)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @GetMapping("/subscriber/top10")
    public List<TopSubscriberViewModel> getTenAllTimeSubscribersWithBiggestBillsPaid(Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            List<Subscriber> subscribers = subscriberService.getTenAllTimeSubscribersWithBiggestBillsPaid(client.getId());

            return subscribers.stream()
                    .map(TopSubscriberViewModel::fromModel)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean validateDate(String start, String end) {
        if (start.equals(end)) {
            return true;
        }
        return LocalDate.parse(start)
                .isBefore(LocalDate.parse(end));
    }
}
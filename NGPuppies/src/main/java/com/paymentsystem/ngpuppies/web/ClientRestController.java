package com.paymentsystem.ngpuppies.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.SubscriberServiceImpl;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.validator.DateValidator;
import com.paymentsystem.ngpuppies.viewModels.InvoiceSimpleViewModel;
import com.paymentsystem.ngpuppies.viewModels.SubscriberSimpleViewModel;
import com.paymentsystem.ngpuppies.viewModels.SubscriberViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${common.basepath}/client")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private InvoiceService invoiceService;

    private final DateValidator DATE_VALIDATOR = new DateValidator();

    @GetMapping("/subscriber={phone}")
    public SubscriberSimpleViewModel getSubscriberOfCurrentClient(@PathVariable("phone") String phoneNumber, Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            Subscriber subscriber = subscriberService.getByNumber(phoneNumber);
            if (subscriber != null) {
                if (subscriber.getClient().getId() != client.getId()) {
                    return null;
                }

                return SubscriberSimpleViewModel.fromModel(subscriber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/subscriber={phone}/average")
    public Double getSubscriberAverageInvoiceSumPaid(@PathVariable("phone") String subscriberPhone,
                                                     @RequestParam("from") @DateTimeFormat(pattern = "YYYY-MM-DD") String fromDate,
                                                     @RequestParam("to") @DateTimeFormat(pattern = "YYYY-MM-DD") String endDate,
                                                     Authentication authentication) {
        try {
            if (!DATE_VALIDATOR.validateDate(fromDate) || !DATE_VALIDATOR.validateDate(endDate)) {
                return 0D;
            }

            SubscriberSimpleViewModel subscriber = getSubscriberOfCurrentClient(subscriberPhone,authentication);
            if(subscriber != null) {
                return subscriberService.getSubscriberAverageInvoiceSumPaid(subscriber.id,fromDate,endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0D;
    }

    @GetMapping("/invoices/unpaid")
    public List<InvoiceSimpleViewModel> getAllUnpaidInvoices(Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            List<InvoiceSimpleViewModel> allUnpaidInvoicesOfCurrentClient =
                    invoiceService.geAllUnpaidInvoicesOfAllClientSubscribers(client.getId())
                            .stream()
                            .map(InvoiceSimpleViewModel::fromModel)
                            .collect(Collectors.toList());

            return allUnpaidInvoicesOfCurrentClient;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/subscriber={phone}/invoices")
    public List<InvoiceSimpleViewModel> getAllInvoicesOfSubscriber(@PathVariable("phone") String phoneNumber, Authentication authentication) {
        try {
            SubscriberSimpleViewModel subscriber = getSubscriberOfCurrentClient(phoneNumber, authentication);

            if (subscriber != null) {
                return invoiceService.getAllInvoicesOfSubscriberBySubscriberId(subscriber.id)
                        .stream()
                        .map(InvoiceSimpleViewModel::fromModel)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/subscriber={phone}/invoices/unpaid")
    public List<InvoiceSimpleViewModel> getAllUnpaidInvoicesOfSubscriber(@PathVariable("phone") String phoneNumber, Authentication authentication) {
        try {
            SubscriberSimpleViewModel subscriber = getSubscriberOfCurrentClient(phoneNumber, authentication);

            if (subscriber != null) {
                return invoiceService.getAllUnpaidInvoicesOfSubscriberInDescOrder(subscriber.id)
                        .stream()
                        .map(InvoiceSimpleViewModel::fromModel)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @GetMapping("/subscriber={phone}/invoices/paid")
    public List<InvoiceSimpleViewModel> getSubscriberPayedInvoices(@PathVariable("phone") String subscriberPhone,
                                                                   @RequestParam("from") @DateTimeFormat(pattern = "YYYY-MM-DD") String fromDate,
                                                                   @RequestParam("to") @DateTimeFormat(pattern = "YYYY-MM-DD") String endDate,
                                                                   Authentication authentication) {
        try {
            if (!DATE_VALIDATOR.validateDate(fromDate) || !DATE_VALIDATOR.validateDate(endDate)) {
                return new ArrayList<>();
            }

            SubscriberSimpleViewModel subscriber = getSubscriberOfCurrentClient(subscriberPhone, authentication);
            if (subscriber != null) {
                List<Invoice> allPayedInvoices = invoiceService.getAllPaidInvoicesOfSubscriberInDescOrder(subscriber.id, fromDate, endDate);
                if (allPayedInvoices != null) {
                    return allPayedInvoices.stream()
                            .map(InvoiceSimpleViewModel::fromModel)
                            .collect(Collectors.toList());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @PutMapping("/pay/invoice")
    public ResponseEntity<?> payInvoices(@RequestParam("id") List<Integer> invoicesIds, Authentication authentication) {
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
    public List<InvoiceSimpleViewModel> getLastTenPayedInvoices(Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            return invoiceService.getTenMostRecentInvoices(client.getId())
                    .stream()
                    .map(InvoiceSimpleViewModel::fromModel)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @GetMapping("/subscriber/top10")
    public List<SubscriberViewModel> getTenTopSubscribersWithBiggestBillsPayed(Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            Map<Subscriber, Double> subscribers = subscriberService.getTopTenSubscribers(client.getId());
            List<SubscriberViewModel> result = new ArrayList<>();

            for(Map.Entry<Subscriber,Double> entry : subscribers.entrySet()) {
                result.add(SubscriberViewModel.fromModel(entry.getKey(), entry.getValue()));
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/all-subscribers")
    public List<Subscriber> getAllSubscribers(Authentication authentication) {
        return new ArrayList<>();
    }
}
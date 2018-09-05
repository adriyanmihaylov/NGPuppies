package com.paymentsystem.ngpuppies.web.client;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.validator.DateValidator;
import com.paymentsystem.ngpuppies.viewModels.InvoiceSimpleViewModel;
import com.paymentsystem.ngpuppies.viewModels.SubscriberViewModel;
import com.paymentsystem.ngpuppies.viewModels.TopSubscriberViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${common.basepath}")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class ClientRestController {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private InvoiceService invoiceService;

    private DateValidator dateValidator = new DateValidator();

    @GetMapping("/subscriber/{phone}")
    public SubscriberViewModel getSubscriberOfCurrentlyLoggedClient(@PathVariable("phone") String phoneNumber,
                                                                    Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();
            Subscriber subscriber = subscriberService.getByNumber(phoneNumber);
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

    @GetMapping("/subscriber/{phone}/invoices")
    public List<InvoiceSimpleViewModel> getAllInvoicesOfSubscriber(@PathVariable("phone") String phoneNumber,
                                                                   Authentication authentication) {
        try {
            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(phoneNumber, authentication);

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

    @GetMapping("/subscriber/{phone}/invoices/unpaid")
    public List<InvoiceSimpleViewModel> getAllUnpaidInvoicesOfSubscriber(@PathVariable("phone") String phoneNumber,
                                                                         Authentication authentication) {
        try {
            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(phoneNumber, authentication);

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

    @GetMapping("/subscriber/{phone}/invoices/paid")
    public ResponseEntity<List<InvoiceSimpleViewModel>> getSubscriberPaidInvoices(@PathVariable("phone") String subscriberPhone,
                                                                                  @RequestParam("from") @DateTimeFormat(pattern = "YYYY-MM-DD") String fromDate,
                                                                                  @RequestParam("to") @DateTimeFormat(pattern = "YYYY-MM-DD") String endDate,
                                                                                  Authentication authentication) {
        try {
            if (!validateDate(fromDate,endDate)) {
                return new ResponseEntity("Invalid date", HttpStatus.BAD_REQUEST);
            }
            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(subscriberPhone, authentication);
            if (subscriber != null) {
                List<Invoice> allPayedInvoices = invoiceService.getAllPaidInvoicesOfSubscriberInDescOrder(subscriber.id, fromDate, endDate);
                List<InvoiceSimpleViewModel> invoiceSimpleViewModels = new ArrayList<>();
                if (allPayedInvoices != null) {
                    invoiceSimpleViewModels = allPayedInvoices.stream()
                            .map(InvoiceSimpleViewModel::fromModel)
                            .collect(Collectors.toList());
                }
                return new ResponseEntity<>(invoiceSimpleViewModels, HttpStatus.OK);
            }
            return new ResponseEntity("Subscriber not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/subscriber/{phone}/average")
    public Double getSubscriberAverageSumOfPaidInvoices(@PathVariable("phone") String subscriberPhone,
                                                        @RequestParam("from") @DateTimeFormat(pattern = "YYYY-MM-DD") String fromDate,
                                                        @RequestParam("to") @DateTimeFormat(pattern = "YYYY-MM-DD") String endDate,
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
    public InvoiceSimpleViewModel getSubscriberLargestPaidInvoice(@PathVariable("phone") String subscriberPhone,
                                                                  @RequestParam("from") @DateTimeFormat(pattern = "YYYY-MM-DD") String fromDate,
                                                                  @RequestParam("to") @DateTimeFormat(pattern = "YYYY-MM-DD") String endDate,
                                                                  Authentication authentication) {
        try {
            if (!validateDate(fromDate,endDate)) {
                return null;
            }

            SubscriberViewModel subscriber = getSubscriberOfCurrentlyLoggedClient(subscriberPhone, authentication);
            if (subscriber != null) {
                return InvoiceSimpleViewModel.fromModel(invoiceService.getSubscriberLargestPaidInvoice(subscriber.id, fromDate, endDate));
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
        if (dateValidator.validateDate(start) && dateValidator.validateDate(end)) {
            if (start.equals(end)) {
                return true;
            }
            return LocalDate.parse(start)
                    .isBefore(LocalDate.parse(end));
        }
        return false;
    }
}
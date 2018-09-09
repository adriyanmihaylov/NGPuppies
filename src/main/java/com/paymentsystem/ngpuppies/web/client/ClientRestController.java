package com.paymentsystem.ngpuppies.web.client;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.web.dto.InvoicePaymentDTO;
import com.paymentsystem.ngpuppies.web.dto.ResponseMessage;
import com.paymentsystem.ngpuppies.web.dto.ValidList;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.models.viewModels.*;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import com.paymentsystem.ngpuppies.validation.anotations.ValidDate;
import com.paymentsystem.ngpuppies.validation.anotations.ValidPhone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("${common.basepath}/client")
@PreAuthorize("hasRole('ROLE_CLIENT')")
public class ClientRestController {

    @Autowired
    private SubscriberService subscriberService;

    @Autowired
    private TelecomServService telecomServService;

    @Autowired
    private InvoiceService invoiceService;

    private Subscriber getSubscriberOfCurrentlyLoggedClient(String phoneNumber,Authentication authentication) {
        Client client = (Client) authentication.getPrincipal();
        Subscriber subscriber = subscriberService.getSubscriberByPhone(phoneNumber);

        if (subscriber != null && subscriber.getClient() != null) {
            if (subscriber.getClient().getId() == client.getId()) {
                return subscriber;
            }
        }

        return null;
    }

    @GetMapping("/subscriber")
    public ResponseEntity<SubscriberViewModel> getSubscriber(@RequestParam("phone") @ValidPhone String phoneNumber,
                                                                    Authentication authentication) {
        try {
            Subscriber subscriber = getSubscriberOfCurrentlyLoggedClient(phoneNumber, authentication);

            if(subscriber == null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            SubscriberViewModel subscriberViewModel = SubscriberViewModel.fromModel(subscriber);

            return new ResponseEntity<>(subscriberViewModel, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/invoices/unpaid")
    public ResponseEntity<List<InvoiceViewModel>> getAllUnpaidInvoices(Authentication authentication) {
        try {
            Client client = (Client) authentication.getPrincipal();

            return new ResponseEntity<>(invoiceService.geAllUnpaidInvoicesOfAllClientSubscribers(client.getId())
                    .stream()
                    .map(InvoiceViewModel::fromModel)
                    .collect(Collectors.toList()), HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/subscriber/{phone}/invoices")
    public List<InvoiceViewModel> getAllInvoicesOfSubscriber(@PathVariable("phone") @ValidPhone String phoneNumber,
                                                             Authentication authentication) {
        try {
            Subscriber subscriber = getSubscriberOfCurrentlyLoggedClient(phoneNumber, authentication);

            if (subscriber != null) {
                return invoiceService.getAllInvoicesOfSubscriberBySubscriberId(subscriber.getId())
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
            Subscriber subscriber = getSubscriberOfCurrentlyLoggedClient(phoneNumber, authentication);

            if (subscriber != null) {
                return invoiceService.getAllUnpaidInvoicesOfSubscriberInDescOrder(phoneNumber)
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
    public ResponseEntity<ResponseMessage> getSubscriberAverageSumOfPaidInvoices(@PathVariable("phone") String subscriberPhone,
                                                                                 @RequestParam("from") @ValidDate String fromDate,
                                                                                 @RequestParam("to") @ValidDate String endDate,
                                                                                 Authentication authentication) {
        try {
            Subscriber subscriber = getSubscriberOfCurrentlyLoggedClient(subscriberPhone, authentication);
            if (subscriber != null) {
                Double avgAmount = subscriberService.getSubscriberAverageSumOfPaidInvoices(subscriber, fromDate, endDate);

                return new ResponseEntity<>(new ResponseMessage(avgAmount.toString()), HttpStatus.OK);
            }
        } catch (InvalidParameterException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/subscriber/{phone}/invoice/max")
    public ResponseEntity<InvoiceViewModel> getSubscriberLargestPaidInvoice(@PathVariable("phone") @ValidPhone String subscriberPhone,
                                                            @RequestParam("from") @ValidDate String fromDate,
                                                            @RequestParam("to") @ValidDate String endDate,
                                                            Authentication authentication) {
        try {
            Subscriber subscriber = getSubscriberOfCurrentlyLoggedClient(subscriberPhone, authentication);

            InvoiceViewModel viewModel = InvoiceViewModel.fromModel(invoiceService.getSubscriberLargestPaidInvoiceForPeriodOfTime(subscriber, fromDate, endDate));

            return new ResponseEntity<>(viewModel, HttpStatus.OK);

        } catch (InvalidParameterException e) {
            throw new InvalidParameterException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/invoice/pay")
    public ValidList<InvoicePaymentDTO> getInvoicePayModel() {
        ValidList<InvoicePaymentDTO> invoicePayDTOValidList = new ValidList<>();
        for (int i = 0; i < 2; i++) {
            invoicePayDTOValidList.add(new InvoicePaymentDTO());
        }

        return invoicePayDTOValidList;
    }

    @PutMapping("/invoice/pay")
    @ResponseBody
    public ResponseEntity<?> payInvoices(@RequestBody() @Valid ValidList<InvoicePaymentDTO> invoicePayDTOList,
                                         Authentication authentication,
                                         BindingResult bindingResult) {
        List<InvoicePaymentDTO> unpaidInvoices = new ArrayList<>();
        try {
            Client client = (Client) authentication.getPrincipal();
            unpaidInvoices = invoiceService.payInvoices(invoicePayDTOList.getList(), client.getId());

            if(unpaidInvoices.size() > 0) {
                return new ResponseEntity<>(unpaidInvoices,HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(unpaidInvoices, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
            List<Subscriber> subscribers = subscriberService.getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(client.getId());

            return subscribers.stream()
                    .map(TopSubscriberViewModel::fromModel)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/my/services")
    public ResponseEntity<?> getAllTelecomServicesThatTheClientPaysFor(Authentication authentication) {
        try {

            Client client = (Client) authentication.getPrincipal();
            List<TelecomServSimpleViewModel> viewModels = telecomServService.getAllServicesOfClient(client).stream()
                    .map(TelecomServSimpleViewModel::fromModel)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(viewModels, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ResponseMessage("Something went wrong! Please try again later!"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("invoice/{phone}/paid")
    public ResponseEntity<List<InvoiceViewModel>> getSubscriberPaidInvoicesFromDateToDate(@PathVariable("phone") @ValidPhone String subscriberPhone,
                                                                                          @RequestParam("from") @ValidDate String fromDate,
                                                                                          @RequestParam("to") @ValidDate String endDate,
                                                                                          Authentication authentication) {

        try {
            Subscriber subscriber = getSubscriberOfCurrentlyLoggedClient(subscriberPhone,authentication);
            if(subscriber == null) {
                throw new IllegalArgumentException("There is no such subscriber");
            }

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

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ResponseMessage> handleAuthenticationException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
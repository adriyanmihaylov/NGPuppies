package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.models.dto.InvoicePaymentDTO;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private TelecomServService telecomServService;
    @Autowired
    private SubscriberService subscriberService;

    @Override
    public Invoice getById(int id) {
        return invoiceRepository.getById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.getAll();
    }

    @Override
    public InvoiceDTO create(InvoiceDTO invoiceDTO) {

        validateDate(invoiceDTO.getStartDate(), invoiceDTO.getEndDate());

        Subscriber subscriber = subscriberService.getSubscriberByPhone(invoiceDTO.getSubscriberPhone());
        if (subscriber == null) {
            return invoiceDTO;
        }
        TelecomServ telecomServ = telecomServService.getByName(invoiceDTO.getService());
        if (telecomServ == null) {
            return invoiceDTO;
        }
        if (!subscriber.getSubscriberServices().contains(telecomServ)) {
            return invoiceDTO;
        }

        Invoice invoice = new Invoice(subscriber,
                LocalDate.parse(invoiceDTO.getStartDate()),
                LocalDate.parse(invoiceDTO.getEndDate()),
                Double.parseDouble(invoiceDTO.getAmountBGN()),
                telecomServ);

        try {
            if (!invoiceRepository.create(invoice)) {
                return invoiceDTO;
            }
        } catch (Exception e) {
            return invoiceDTO;
        }
        return null;
    }

    @Override
    public boolean update(List<Invoice> invoices) {
        return invoiceRepository.update(invoices);
    }

    @Override
    public boolean delete(int invoiceId, String subscriberPhone) throws InvalidParameterException {
        Invoice invoice = invoiceRepository.getById(invoiceId);
        if (invoice == null) {
            throw new InvalidParameterException("There is no invoice with Id: " + invoiceId);
        }

        if (!invoice.getSubscriber().getPhone().equals(subscriberPhone)) {
            throw new IllegalArgumentException("Invoice Id: " + invoiceId + " is not for the selected subscriber!");
        }

        return invoiceRepository.delete(invoice);
    }

    @Override
    public List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(int subscriberId) {
        return invoiceRepository.getAllInvoicesOfSubscriberBySubscriberId(subscriberId);
    }

    @Override
    public List<Invoice> getAllUnpaidInvoices() {
        return invoiceRepository.getAllUnpaidInvoices();
    }

    @Override
    public List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId) {
        return invoiceRepository.geAllUnpaidInvoicesOfAllClientSubscribers(clientId);
    }

    @Override
    public List<InvoicePaymentDTO> payInvoices(List<InvoicePaymentDTO> invoices, int clientId) {
        List<InvoicePaymentDTO> unpaidInvoices = new ArrayList<>();
        for (InvoicePaymentDTO invoicePaymentDTO : invoices) {
            Invoice invoice = invoiceRepository.getById(invoicePaymentDTO.getId());

            if (invoice != null && invoice.getSubscriber().getClient() != null) {
                if (invoice.getPayedDate() == null) {
                    if (invoice.getSubscriber().getClient().getId() == clientId) {
                        Currency currency = currencyRepository.getByName(invoicePaymentDTO.getCurrency());

                        if (currency != null) {

                            invoice.setCurrency(currency);
                            invoice.setAmount(invoice.getBGNAmount() / currency.getFixing());
                            invoice.setPayedDate(LocalDate.now());
                            Subscriber subscriber = invoice.getSubscriber();
                            subscriber.setTotalAmount(invoice.getBGNAmount() + subscriber.getTotalAmount());

                            if (invoiceRepository.payInvoice(invoice)) {
                                continue;
                            }
                        }
                    }
                }
            }
            unpaidInvoices.add(invoicePaymentDTO);
        }

        return unpaidInvoices;
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone) {
        return invoiceRepository.getAllUnpaidInvoicesOfSubscriberInDescOrder(subscriberPhone);
    }

    @Override
    public List<Invoice> getTenMostRecentInvoices(int clientId) {
        return invoiceRepository.getTenMostRecentInvoices(clientId);
    }

    @Override
    public List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(int subscriberId, String fromDate, String endDate) throws InvalidParameterException {
        validateDate(fromDate, endDate);

        return invoiceRepository.getAllPaidInvoicesOfSubscriberByPeriodOfTimeInDescOrder(subscriberId,LocalDate.parse(fromDate),LocalDate.parse(endDate));
    }

    @Override
    public Invoice getSubscriberLargestPaidInvoice(Subscriber subscriber, String fromDate, String endDate) throws InvalidParameterException {

        validateDate(fromDate, endDate);

        if (subscriber == null) {
            throw new InvalidParameterException("There is no such subscriber!");
        }

        return invoiceRepository.getSubscriberLargestPaidInvoiceForPeriodOfTime(subscriber.getId(), LocalDate.parse(fromDate),LocalDate.parse(endDate));
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfService(String serviceName) throws InvalidParameterException {
        TelecomServ telecomServ = telecomServService.getByName(serviceName);
        if (telecomServ == null) {
            throw new InvalidParameterException("There is no such service!");
        }

        return invoiceRepository.getAllUnpaidInvoicesOfService(serviceName);
    }

    @Override
    public List<Invoice> geAllUnpaidInvoicesFromDateToDate(String fromDate, String toDate) {
        validateDate(fromDate, toDate);

        return invoiceRepository.geAllUnpaidInvoicesFromDateToDate(LocalDate.parse(fromDate), LocalDate.parse(toDate));
    }

    private boolean validateDate(String start, String end) throws InvalidParameterException {
        if (start.equals(end)) {
            return true;
        }

        if (LocalDate.parse(start).isAfter(LocalDate.parse(end))) {
            throw new InvalidParameterException("Invalid date range!");
        }

        return true;
    }
}
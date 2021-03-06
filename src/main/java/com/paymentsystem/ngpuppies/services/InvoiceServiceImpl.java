package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import com.paymentsystem.ngpuppies.validation.DateValidator;
import com.paymentsystem.ngpuppies.web.dto.InvoiceDto;
import com.paymentsystem.ngpuppies.web.dto.InvoicePaymentDto;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final CurrencyRepository currencyRepository;
    private final TelecomServRepository telecomServRepository;
    private final SubscriberRepository subscriberRepository;

    private DateValidator dateValidator = new DateValidator();

    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              CurrencyRepository currencyRepository,
                              TelecomServRepository telecomServRepository,
                              SubscriberRepository subscriberRepository) {
        this.invoiceRepository = invoiceRepository;
        this.currencyRepository = currencyRepository;
        this.telecomServRepository = telecomServRepository;
        this.subscriberRepository = subscriberRepository;
    }

    @Override
    public Invoice getById(int id) {
        return invoiceRepository.getById(id);
    }

    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.getAll();
    }

    @Override
    public InvoiceDto create(InvoiceDto invoiceDto) {
        Subscriber subscriber = subscriberRepository.getSubscriberByPhoneNumber(invoiceDto.getSubscriberPhone());
        if (subscriber == null) {
            return invoiceDto;
        }
        TelecomServ telecomServ = telecomServRepository.getByName(invoiceDto.getService());
        if (telecomServ == null) {
            return invoiceDto;
        }
        if (subscriber.getSubscriberServices() == null || !subscriber.getSubscriberServices().contains(telecomServ)) {
            return invoiceDto;
        }
        LocalDate from;
        LocalDate to;
        try {
            from = dateValidator.extractDateFromString(invoiceDto.getStartDate());
            to = dateValidator.extractDateFromString(invoiceDto.getEndDate());
            dateValidator.validateDates(from,to);
        } catch (InvalidParameterException e) {
            return invoiceDto;
        }
        Invoice invoice = new Invoice(subscriber,
                from,
                to,
                Double.parseDouble(invoiceDto.getAmountBGN()),
                telecomServ);

        if (invoiceRepository.create(invoice)) {
            return null;
        }

        return invoiceDto;
    }

    @Override
    public boolean delete(int invoiceId, String subscriberPhone) throws InvalidParameterException {
        Invoice invoice = invoiceRepository.getById(invoiceId);
        if (invoice == null) {
            throw new InvalidParameterException("There is no invoice with Id: " + invoiceId);
        }

        if (!invoice.getSubscriber().getPhone().equals(subscriberPhone)) {
            throw new InvalidParameterException("Invoice Id: " + invoiceId + " is not for the selected subscriber!");
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
    public List<InvoicePaymentDto> payInvoices(List<InvoicePaymentDto> invoices, int clientId) {
        List<InvoicePaymentDto> unpaidInvoices = new ArrayList<>();
        for (InvoicePaymentDto invoicePaymentDto : invoices) {
            Invoice invoice = invoiceRepository.getById(invoicePaymentDto.getId());

            if (invoice != null && invoice.getSubscriber().getClient() != null) {
                if (invoice.getPayedDate() == null) {
                    if (invoice.getSubscriber().getClient().getId() == clientId) {
                        Currency currency = currencyRepository.getByName(invoicePaymentDto.getCurrency());

                        if (currency != null) {

                            invoice.setCurrency(currency);
                            invoice.setAmount(round(invoice.getBGNAmount() / currency.getFixing()));
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
            unpaidInvoices.add(invoicePaymentDto);
        }

        return unpaidInvoices;
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone) throws InvalidParameterException {
        if(subscriberPhone == null) {
            throw new InvalidParameterException("Phone must not be empty!");
        }
        return invoiceRepository.getAllUnpaidInvoicesOfSubscriberInDescOrder(subscriberPhone);
    }

    @Override
    public List<Invoice> getTenMostRecentInvoices(int clientId) {
        return invoiceRepository.getTenMostRecentInvoices(clientId);
    }

    @Override
    public List<Invoice> getSubscriberInvoicesFromDateToDate(String subscriberPhone, String fromDate, String endDate) throws InvalidParameterException {
        LocalDate from = dateValidator.extractDateFromString(fromDate);
        LocalDate to = dateValidator.extractDateFromString(endDate);
        dateValidator.validateDates(from, to);

        return invoiceRepository.getSubscriberPaidInvoicesFromDateToDate(subscriberPhone, from, to);
    }

    @Override
    public Invoice getSubscriberLargestPaidInvoiceForPeriodOfTime(Subscriber subscriber, String fromDate, String endDate) throws InvalidParameterException {
        if (subscriber == null) {
            throw new InvalidParameterException("There is no such subscriber!");
        }

        LocalDate from = dateValidator.extractDateFromString(fromDate);
        LocalDate to = dateValidator.extractDateFromString(endDate);
        dateValidator.validateDates(from, to);

        return invoiceRepository.getSubscriberLargestPaidInvoiceForPeriodOfTime(subscriber.getId(), from, to);
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfService(String serviceName) throws InvalidParameterException {
        return invoiceRepository.getAllUnpaidInvoicesOfService(serviceName);
    }

    @Override
    public List<Invoice> geAllUnpaidInvoicesFromDateToDate(String fromDate, String endDate) throws InvalidParameterException {
        LocalDate from = dateValidator.extractDateFromString(fromDate);
        LocalDate to = dateValidator.extractDateFromString(endDate);
        dateValidator.validateDates(from, to);

        return invoiceRepository.geAllUnpaidInvoicesFromDateToDate(from, to);
    }

    public static double round(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.dto.InvoicePaymentDTO;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.TelecomServService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private TelecomServService telecomServService;

    @Override
    public Invoice getById(Integer id) {
        return invoiceRepository.getById(id);
    }
    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.getAll();
    }

    @Override
    public boolean create(Invoice invoice) {
        return invoiceRepository.create(invoice);
    }

    @Override
    public List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId) {
        return invoiceRepository.getAllInvoicesOfSubscriberBySubscriberId(subscriberId);
    }


    @Override
    public boolean update(List<Invoice> invoices) {
        return invoiceRepository.update(invoices);
    }

    @Override
    public boolean delete(Invoice invoice) {
        return invoiceRepository.delete(invoice);
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
    public List<InvoicePaymentDTO> payInvoices(List<InvoicePaymentDTO> invoices, Integer clientId) {
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
                            invoice.setPayedDate(new Date());
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
    public List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate) {
        return invoiceRepository.getAllPaidInvoicesOfSubscriberByPeriodOfTimeInDescOrder(subscriberId,fromDate,endDate);
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone) {
        return invoiceRepository.getAllUnpaidInvoicesOfSubscriberInDescOrder(subscriberPhone);
    }

    @Override
    public List<Invoice> getTenMostRecentInvoices(Integer clientId) {
        return invoiceRepository.getTenMostRecentInvoices(clientId);
    }

    @Override
    public Invoice getSubscriberLargestPaidInvoice(Integer subscriberId, String fromDate, String endDate) {
        return invoiceRepository.getSubscriberLargestPaidInvoiceForPeriodOfTime(subscriberId,fromDate,endDate);
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfService(String serviceName) {
        TelecomServ telecomServ = telecomServService.getByName(serviceName);
        if (telecomServ == null) {
            return new ArrayList<>();
        }

        return invoiceRepository.getAllUnpaidInvoicesOfService(serviceName);
    }
}
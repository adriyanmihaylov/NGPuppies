package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.models.dto.InvoicePayDTO;
import com.paymentsystem.ngpuppies.models.dto.ValidList;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import com.paymentsystem.ngpuppies.services.base.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    @Override
    public Invoice getById(Integer id) {
        return invoiceRepository.getById(id);
    }
    @Override
    public List<Invoice> getAll() {
        return invoiceRepository.getAll();
    }

    @Override
    public List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId) {
        return invoiceRepository.getAllInvoicesOfSubscriberBySubscriberId(subscriberId);
    }

    @Override
    public boolean create(Invoice invoice) {
        return invoiceRepository.create(invoice);
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
    public List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId) {
        return invoiceRepository.geAllUnpaidInvoicesOfAllClientSubscribers(clientId);
    }

    @Override
    public Set<Invoice> getInvoicesByIdAndClientId(List<InvoicePayDTO> invoices, Integer clientId) {
        return invoiceRepository.getInvoicesByIdAndClientId(invoices, clientId);
    }

    @Override
    public List<InvoicePayDTO> payInvoices(List<InvoicePayDTO> invoices, Integer clientId) {
        List<InvoicePayDTO> unpaidInvoices = new ArrayList<>();
        for (InvoicePayDTO invoicePayDTO: invoices) {
            Invoice invoice = invoiceRepository.getById(invoicePayDTO.getId());

            if (invoice != null && invoice.getSubscriber().getClient() != null) {
                if (invoice.getPayedDate() == null) {
                    if (invoice.getSubscriber().getClient().getId() == clientId) {
                        Currency currency = currencyRepository.getByName(invoicePayDTO.getCurrency());

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
            unpaidInvoices.add(invoicePayDTO);
        }

        return unpaidInvoices;
    }

    @Override
    public List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate) {
        return invoiceRepository.getAllPaidInvoicesOfSubscriberByPeriodOfTimeInDescOrder(subscriberId,fromDate,endDate);
    }

    @Override
    public List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(Integer subscriberId) {
        return invoiceRepository.getAllUnpaidInvoicesOfSubscriberInDescOrder(subscriberId);
    }

    @Override
    public List<Invoice> getTenMostRecentInvoices(Integer clientId) {
        return invoiceRepository.getTenMostRecentInvoices(clientId);
    }

    @Override
    public Invoice getSubscriberLargestPaidInvoice(Integer subscriberId, String fromDate, String endDate) {
        return invoiceRepository.getSubscriberLargestPaidInvoiceForPeriodOfTime(subscriberId,fromDate,endDate);
    }
}
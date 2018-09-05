package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

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
    public List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId) {
        return invoiceRepository.geAllUnpaidInvoicesOfAllClientSubscribers(clientId);
    }

    @Override
    public List<Invoice> getInvoicesByIdAndClientId(List<Integer> invoices, Integer id) {
        return invoiceRepository.getInvoicesByIdAndClientId(invoices, id);
    }

    @Override
    public boolean payInvoices(List<Invoice> allInvoices) {
        return invoiceRepository.payInvoices(allInvoices);
    }

    @Override
    public List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate) {
        return invoiceRepository.getAllPaidInvoicesOfSubscriberInDescOrder(subscriberId,fromDate,endDate);
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
        return invoiceRepository.getSubscriberLargestPaidInvoice(subscriberId,fromDate,endDate);
    }
}
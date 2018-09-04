package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Invoice;

import java.util.List;

public interface InvoiceRepository {
    List<Invoice> getAll();

    boolean create(Invoice invoice);

    boolean update(List<Invoice> invoices);

    boolean payInvoices(List<Invoice> allInvoices);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    List<Invoice> getInvoicesByIdAndClientId(List<Integer> invoices, Integer id);

    List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriber, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(Integer subscriberId);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId);

    List<Invoice> getTenMostRecentInvoices(Integer clientId);

    Invoice getSubscriberLargestPaidInvoice(Integer subscriberId, String fromDate, String endDate);
}
package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Invoice;

import java.util.List;

public interface InvoiceService {

    Invoice getById(Integer id);

    List<Invoice> getAll();

    boolean create(Invoice invoice);

    boolean update(List<Invoice> invoice);

    boolean delete(Invoice invoice);

    boolean payInvoices(List<Invoice> allInvoices);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    List<Invoice> getInvoicesByIdAndClientId(List<Integer> invoices, Integer id);

    List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(Integer subscriberId);

    List<Invoice> getTenMostRecentInvoices(Integer clientId);

    Invoice getSubscriberLargestPaidInvoice(Integer subscriberId, String fromDate, String endDate);
}
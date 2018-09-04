package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Invoice;

import java.util.List;

public interface InvoiceService {
    List<Invoice> getAll();

    boolean create(Invoice invoice);

    boolean update(List<Invoice> invoice);

    List<Invoice> getAllInvoicesOfSubscriberByPhoneNumber(String phoneNumber, boolean isPayed);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    List<Invoice> getInvoicesByIdAndClientId(List<Integer> invoices, Integer id);

    boolean payInvoices(List<Invoice> allInvoices);

    List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(Integer subscriberId);

    List<Invoice> getTenMostRecentInvoices(Integer clientId);
}
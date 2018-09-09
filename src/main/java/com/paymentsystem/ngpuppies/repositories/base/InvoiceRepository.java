package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Invoice;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceRepository {
    List<Invoice> getAll();

    Invoice getById(Integer id);

    boolean create(Invoice invoices);

    boolean delete(Invoice invoice);

    boolean payInvoice(Invoice Invoice);

    List<Invoice> getAllUnpaidInvoices();

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    List<Invoice> getSubscriberPaidInvoicesFromDateToDate(String subscriberPhone, LocalDate fromDate, LocalDate toDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(int subscriberId);

    List<Invoice> getTenMostRecentInvoices(Integer clientId);

    Invoice getSubscriberLargestPaidInvoiceForPeriodOfTime(Integer subscriberId, LocalDate fromDate, LocalDate toDate);

    List<Invoice> getAllUnpaidInvoicesOfService(String serviceName);

    List<Invoice> geAllUnpaidInvoicesFromDateToDate(LocalDate fromDate, LocalDate toDate);
}
package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.dto.InvoicePayDTO;
import com.paymentsystem.ngpuppies.models.dto.ValidList;

import java.util.List;
import java.util.Set;

public interface InvoiceRepository {
    List<Invoice> getAll();

    Invoice getById(Integer id);

    boolean create(Invoice invoices);

    boolean update(List<Invoice> invoices);

    boolean delete(Invoice invoice);

    boolean payInvoice(Invoice Invoice);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    Set<Invoice> getInvoicesByIdAndClientId(List<InvoicePayDTO> invoices, Integer id);

    List<Invoice> getAllPaidInvoicesOfSubscriberByPeriodOfTimeInDescOrder(Integer subscriber, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(Integer subscriberId);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId);

    List<Invoice> getTenMostRecentInvoices(Integer clientId);

    Invoice getSubscriberLargestPaidInvoiceForPeriodOfTime(Integer subscriberId, String fromDate, String endDate);
}
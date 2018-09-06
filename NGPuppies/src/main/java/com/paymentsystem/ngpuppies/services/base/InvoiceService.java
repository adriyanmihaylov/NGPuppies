package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.dto.InvoicePayDTO;
import com.paymentsystem.ngpuppies.models.dto.ValidList;

import java.util.List;
import java.util.Set;

public interface InvoiceService {

    Invoice getById(Integer id);

    List<Invoice> getAll();

    boolean create(Invoice invoice);

    boolean update(List<Invoice> invoice);

    boolean delete(Invoice invoice);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    Set<Invoice> getInvoicesByIdAndClientId(List<InvoicePayDTO> invoices, Integer id);

    List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(Integer subscriberId);

    List<Invoice> getTenMostRecentInvoices(Integer clientId);

    Invoice getSubscriberLargestPaidInvoice(Integer subscriberId, String fromDate, String endDate);

    List<InvoicePayDTO> payInvoices(List<InvoicePayDTO> invoices, Integer clientId);
}
package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.models.dto.InvoicePaymentDTO;

import java.util.ArrayList;
import java.util.List;

public interface InvoiceService {

    Invoice getById(Integer id);

    List<Invoice> getAll();

    public InvoiceDTO create(InvoiceDTO invoiceDTO);

    boolean update(List<Invoice> invoice);

    boolean delete(Invoice invoice);

    List<Invoice> getAllUnpaidInvoices();

    List<InvoicePaymentDTO> payInvoices(List<InvoicePaymentDTO> invoices, Integer clientId);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(Integer subscriberId);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    List<Invoice> getAllPaidInvoicesOfSubscriberInDescOrder(Integer subscriberId, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone);

    List<Invoice> getTenMostRecentInvoices(Integer clientId);

    Invoice getSubscriberLargestPaidInvoice(Integer subscriberId, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfService(String serviceName);
}
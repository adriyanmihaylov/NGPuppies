package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.web.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.web.dto.InvoicePaymentDTO;

import java.security.InvalidParameterException;
import java.util.List;

public interface InvoiceService {

    Invoice getById(int id);

    List<Invoice> getAll();

    InvoiceDTO create(InvoiceDTO invoiceDTO);

    boolean update(List<Invoice> invoice);

    boolean delete(int id, String subscriberPhone);

    List<Invoice> getAllUnpaidInvoices();

    List<InvoicePaymentDTO> payInvoices(List<InvoicePaymentDTO> invoices, int clientId);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(int subscriberId);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    List<Invoice> getSubscriberInvoicesFromDateToDate(String subscriberPhone, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone);

    List<Invoice> getTenMostRecentInvoices(int clientId);

    Invoice getSubscriberLargestPaidInvoice(Subscriber subscriber, String fromDate, String endDate) throws InvalidParameterException;

    List<Invoice> getAllUnpaidInvoicesOfService(String serviceName) throws InvalidParameterException;

    List<Invoice> geAllUnpaidInvoicesFromDateToDate(String fromDate, String toDate);
}
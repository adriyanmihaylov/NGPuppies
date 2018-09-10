package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.web.dto.InvoiceDto;
import com.paymentsystem.ngpuppies.web.dto.InvoicePaymentDto;

import java.security.InvalidParameterException;
import java.util.List;

public interface InvoiceService {

    Invoice getById(int id);

    List<Invoice> getAll();

    InvoiceDto create(InvoiceDto invoiceDto);


    boolean delete(int id, String subscriberPhone);

    List<Invoice> getAllUnpaidInvoices();

    List<InvoicePaymentDto> payInvoices(List<InvoicePaymentDto> invoices, int clientId);

    List<Invoice> getAllInvoicesOfSubscriberBySubscriberId(int subscriberId);

    List<Invoice> geAllUnpaidInvoicesOfAllClientSubscribers(int clientId);

    List<Invoice> getSubscriberInvoicesFromDateToDate(String subscriberPhone, String fromDate, String endDate);

    List<Invoice> getAllUnpaidInvoicesOfSubscriberInDescOrder(String subscriberPhone);

    List<Invoice> getTenMostRecentInvoices(int clientId);

    Invoice getSubscriberLargestPaidInvoiceForPeriodOfTime(Subscriber subscriber, String fromDate, String endDate) throws InvalidParameterException;

    List<Invoice> getAllUnpaidInvoicesOfService(String serviceName) throws InvalidParameterException;

    List<Invoice> geAllUnpaidInvoicesFromDateToDate(String fromDate, String toDate);
}
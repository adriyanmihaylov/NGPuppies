package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.Invoice;

public class InvoiceSimpleViewModel {
    public int id;
    public String startDate;

    public String endDate;

    public double amount;

    public String service;

    public String currency;

    public String status;

    public String subscriberPhone;

    public String subscriberName;


    public static InvoiceSimpleViewModel fromModel(Invoice invoice) {
        InvoiceSimpleViewModel viewModel = new InvoiceSimpleViewModel();

        if (invoice != null) {
            viewModel.id = invoice.getId();
            viewModel.startDate = invoice.getStartDate().toString().substring(0,10);
            viewModel.endDate = invoice.getEndDate().toString().substring(0,10);
            viewModel.amount = invoice.getAmount();
            viewModel.service = invoice.getOfferedServices().getName();
            viewModel.subscriberPhone = invoice.getSubscriber().getPhone();
            viewModel.currency = invoice.getCurrency().getName();
            if(invoice.getPayedDate() != null) {
                viewModel.status = "Paid on " + invoice.getPayedDate().toString().substring(0,10);
            } else {
                viewModel.status = "Not paid";
            }

            viewModel.subscriberName = invoice.getSubscriber().getFirstName() + " "
                    + invoice.getSubscriber().getLastName();

        }
        return viewModel;
    }
}
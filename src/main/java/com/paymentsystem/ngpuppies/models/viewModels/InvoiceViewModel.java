package com.paymentsystem.ngpuppies.models.viewModels;

import com.paymentsystem.ngpuppies.models.Invoice;

public class InvoiceViewModel {
    public int id;

    public String startDate;

    public String endDate;

    public double amountPaid;

    public String currency;

    public double amount;

    public String service;

    public String status;

    public String subscriberPhone;

    public String subscriberName;

    public static InvoiceViewModel fromModel(Invoice invoice) {
        InvoiceViewModel viewModel = new InvoiceViewModel();

        if (invoice != null) {
            viewModel.id = invoice.getId();
            viewModel.startDate = invoice.getStartDate().toString().substring(0, 10);
            viewModel.endDate = invoice.getEndDate().toString().substring(0, 10);
            viewModel.amount = invoice.getBGNAmount();
            viewModel.service = invoice.getTelecomServ().getName();
            viewModel.subscriberPhone = invoice.getSubscriber().getPhone();
            if (invoice.getPayedDate() != null) {
                viewModel.status = "Paid on " + invoice.getPayedDate().toString().substring(0, 10);
                viewModel.amountPaid = invoice.getAmount();
                viewModel.currency = invoice.getCurrency().getName();
            } else {
                viewModel.status = "Not paid";
                viewModel.amountPaid = 0;
                viewModel.currency = null;
            }

            viewModel.subscriberName = invoice.getSubscriber().getFirstName() + " "
                    + invoice.getSubscriber().getLastName();

        }
        return viewModel;
    }
}
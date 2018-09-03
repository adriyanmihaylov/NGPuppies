package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.OfferedServices;

import java.util.Date;

public class BillingRecordsViewModel {
    public int id;

    public Date startDate;

    public Date endDate;

    public double amount;

    public OfferedServices service;

    public Currency currency;

    public Boolean status;

    public SubscriberSimpleViewModel subscriber;

    public static BillingRecordsViewModel fromModel(BillingRecord billingRecord) {
        BillingRecordsViewModel vm = new BillingRecordsViewModel();

        if (billingRecord != null) {
            vm.id = billingRecord.getId();
            vm.startDate = billingRecord.getStartDate();
            vm.endDate = billingRecord.getEndDate();
            vm.amount = billingRecord.getAmount();
            vm.service = billingRecord.getOfferedServices();
            vm.currency = billingRecord.getCurrency();
            vm.status = billingRecord.isPayed();
            vm.subscriber = SubscriberSimpleViewModel.fromModel(billingRecord.getSubscriber());
        }
        return vm;
    }
}

package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.OfferedService;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.sql.Date;

public class BillingRecordsViewModel {
    public int id;

    public Date startDate;

    public Date endDate;

    public double amount;

    public OfferedService service;

    public Currency currency;

    public Boolean status;

    public SubscriberSimpleViewModel subscriber;

    public static BillingRecordsViewModel fromModel(BillingRecord billingRecord) {
        BillingRecordsViewModel bm = new BillingRecordsViewModel();
        bm.id = billingRecord.getId();
        bm.startDate = billingRecord.getStartDate();
        bm.endDate = billingRecord.getEndDate();
        bm.amount = billingRecord.getAmount();
        bm.service = billingRecord.getOfferedService();
        bm.currency = billingRecord.getCurrency();
        bm.status = billingRecord.isPayed();
        bm.subscriber = SubscriberSimpleViewModel.fromModel(billingRecord.getSubscriber());
        return bm;
    }
}

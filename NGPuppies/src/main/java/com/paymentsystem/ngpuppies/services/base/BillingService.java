package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.BillingRecord;

import java.util.List;

public interface BillingService {
    List<BillingRecord> getAll();

    List<BillingRecord> getAllBillingMethodsOfSubscriberByPhoneNumber(String phoneNumber);

    boolean create(BillingRecord billingRecord);

    boolean update(BillingRecord billingRecord);

    List<BillingRecord> getByDate(String startDate, String endDate);

    List<BillingRecord> searchBills(Boolean isPayed);
}
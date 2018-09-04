package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Subscriber;

import java.util.List;

public interface BillingRecordRepository {
    List<BillingRecord> getAll();

    List<BillingRecord> getAllBillingMethodsOfSubscriberByPhoneNumber(String phoneNumber);

    boolean create(BillingRecord billingRecord);

    boolean update(BillingRecord billingRecord);

    List<BillingRecord> getByDate(String startDate, String endDate);

    List<BillingRecord> searchBills(Boolean isPayed);
}
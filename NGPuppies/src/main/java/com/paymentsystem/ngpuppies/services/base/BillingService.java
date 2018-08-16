package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.BillingRecord;

import java.util.List;

public interface BillingService {
    List<BillingRecord> getAll();
    BillingRecord getBySubscriber(String phoneNumber);
    String deleteBySubscriber(String phoneNumber);
    BillingRecord getById(int id);
    String deleteById(int id);
    String create(BillingRecord billingRecordToBeCreated);
    String update(BillingRecord updatedBillingRecord);
}

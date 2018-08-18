package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.BillingRecord;

import java.util.List;

public interface BillingRecordRepository {
    List<BillingRecord> getAll();
    BillingRecord getBySubscriber(String phoneNumber);
    boolean deleteBySubscriber(String phoneNumber);
    BillingRecord getById(int id);
    boolean deleteById(int id);
    boolean create(BillingRecord billingRecordToBeCreated);
    boolean update(BillingRecord updatedBillingRecord);
}

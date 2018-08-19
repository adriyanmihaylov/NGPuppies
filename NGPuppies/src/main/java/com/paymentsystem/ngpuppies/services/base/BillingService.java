package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.BillingRecord;

import java.util.List;

public interface BillingService {
    List<BillingRecord> getAll();

    BillingRecord getBySubscriber(String phoneNumber);

    String deleteBySubscriber(String phoneNumber);
<<<<<<< HEAD
=======

    BillingRecord getById(int id);

    String deleteById(int id);

>>>>>>> 0c9cc5a7234f9a528e54d75c0d9ad3fa7912239f
    String create(BillingRecord billingRecordToBeCreated);

    String update(BillingRecord updatedBillingRecord);
}
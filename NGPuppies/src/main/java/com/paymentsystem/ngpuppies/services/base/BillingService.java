package com.paymentsystem.ngpuppies.services.base;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.sql.Date;
import java.util.List;

public interface BillingService {
    List<BillingRecord> getAll();

    BillingRecord getBySubscriber(String phoneNumber);

    String deleteBySubscriber(String phoneNumber);

    String create(BillingRecord billingRecordToBeCreated);

    String update(BillingRecord updatedBillingRecord);

    List<BillingRecord> getByDate(String startDate, String endDate);

    List<BillingRecord> searchBills(Boolean payed);
}
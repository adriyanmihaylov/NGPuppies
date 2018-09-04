package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.repositories.base.BillingRecordRepository;
import com.paymentsystem.ngpuppies.services.base.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BillingServiceImpl implements BillingService {
    @Autowired
    private BillingRecordRepository recordRepository;

    @Override
    public List<BillingRecord> getAll() {
        return recordRepository.getAll();
    }

    @Override
    public List<BillingRecord> getAllBillingMethodsOfSubscriberByPhoneNumber(String phoneNumber) {
        return recordRepository.getAllBillingMethodsOfSubscriberByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean create(BillingRecord billingRecord) {
        return recordRepository.create(billingRecord);
    }

    @Override
    public boolean update(BillingRecord billingRecord) {
        return recordRepository.update(billingRecord);
    }

    @Override
    public List<BillingRecord> getByDate(String startDate, String endDate) {
        return recordRepository.getByDate(startDate, endDate);
    }

    @Override
    public List<BillingRecord> searchBills(Boolean payed) {
        return recordRepository.searchBills(payed);
    }
}
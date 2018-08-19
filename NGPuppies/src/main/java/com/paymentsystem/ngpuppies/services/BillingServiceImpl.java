package com.paymentsystem.ngpuppies.services;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.repositories.base.BillingRecordRepository;
import com.paymentsystem.ngpuppies.services.base.BillingService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BillingServiceImpl implements BillingService {
    private BillingRecordRepository recordRepository;
    public BillingServiceImpl(BillingRecordRepository recordRepository){
        this.recordRepository = recordRepository;
    }
    @Override
    public List<BillingRecord> getAll() {
        return recordRepository.getAll();
    }

    @Override
    public BillingRecord getBySubscriber(String phoneNumber) {
        return recordRepository.getBySubscriber(phoneNumber);
    }

    @Override
    public String deleteBySubscriber(String phoneNumber) {
        if (recordRepository.deleteBySubscriber(phoneNumber)){
            return "Success";
        }
        else {
            return "Fail";
        }
    }

    @Override
    public String create(BillingRecord billingRecordToBeCreated) {
        if (recordRepository.create(billingRecordToBeCreated)){
            return "Success";
        }else {
            return "Something went wrong";
        }
    }

    @Override
    public String update( BillingRecord updatedBillingRecord) {
        if (recordRepository.update(updatedBillingRecord)){
            return "Success";
        }else {
            return "Something went wrong";
        }
    }
}

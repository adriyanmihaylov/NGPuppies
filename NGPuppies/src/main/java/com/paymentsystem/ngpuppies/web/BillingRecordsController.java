package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.OfferedService;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.services.base.BillingService;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/billingRecords")
public class BillingRecordsController {
    private BillingService billingService;

    public BillingRecordsController(BillingService billingService){
        this.billingService = billingService;
    }

    @GetMapping("/")
    public List<BillingRecord> getAll(){
        return billingService.getAll();
    }

    @GetMapping("/getBySubscriber")
    public BillingRecord getBySubscriber(@RequestParam(name = "phoneNumber") String phoneNumber){
        return billingService.getBySubscriber(phoneNumber);
    }
    @GetMapping("/getById")
    public BillingRecord getById(@RequestParam(name = "id") String id){
        return billingService.getById(Integer.parseInt(id));
    }
    @PostMapping("/deleteBySubscriber")
    public void deleteBySubscriber(@RequestParam(name = "phoneNumber") String phoneNumber){
        billingService.deleteBySubscriber(phoneNumber);
    }

    @PostMapping("/deleteById")
    public void deleteById(@RequestParam(name = "id") String id){
        billingService.deleteById(Integer.parseInt(id));
    }

    @PostMapping("/create")
    public void create (@RequestParam(name = "startDate") String startDate,
                        @RequestParam(name = "endDate") String endDate,
                        @RequestParam(name = "amount") String amount,
                        @RequestParam(name = "serviceName") String serviceName,
                        @RequestParam(name = "currencyName") String name,
                        @RequestParam(name = "phoneNumber") String phoneNumber){

        OfferedService offeredService = new OfferedService(serviceName);
        Currency currency = new Currency(name);
        Subscriber subscriber = new Subscriber(phoneNumber);
        BillingRecord newBillingRecord = new BillingRecord(Date.valueOf(startDate), Date.valueOf(endDate), Double.parseDouble(amount),
                offeredService, currency, subscriber);

        billingService.create(newBillingRecord);
    }
    @PutMapping("/update")
        public void update (@RequestParam(name = "startDate") String startDate,
                @RequestParam(name = "endDate") String endDate,
                @RequestParam(name = "amount") String amount,
                @RequestParam(name = "serviceName") String serviceName,
                @RequestParam(name = "currencyName") String name,
                @RequestParam(name = "phoneNumber") String phoneNumber){
        OfferedService offeredService = new OfferedService(serviceName);
        Currency currency = new Currency(name);
        Subscriber subscriber = new Subscriber(phoneNumber);
        BillingRecord billingRecordToUpdate = new BillingRecord(Date.valueOf(startDate), Date.valueOf(endDate), Double.parseDouble(amount),
                offeredService, currency, subscriber);
        billingService.update(billingRecordToUpdate);
    }


}

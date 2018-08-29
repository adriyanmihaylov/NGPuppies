package com.paymentsystem.ngpuppies.web.restControllers;

import com.paymentsystem.ngpuppies.models.BillingRecord;
import com.paymentsystem.ngpuppies.models.Currency;
import com.paymentsystem.ngpuppies.models.OfferedService;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.services.base.BillingService;
import com.paymentsystem.ngpuppies.viewModels.BillingRecordsViewModel;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/${common.basepath}/billingRecords")
public class BillingRecordsRestController {
    private BillingService billingService;

    public BillingRecordsRestController(BillingService billingService){
        this.billingService = billingService;
    }

    @GetMapping("/")
    public List<BillingRecordsViewModel> getAll(){
        return billingService.getAll().stream().map(billingRecord -> BillingRecordsViewModel.fromModel((billingRecord)))
                .collect(Collectors.toList());
    }

    @GetMapping("/getBySubscriber")
    public BillingRecordsViewModel getBySubscriber(@RequestParam(name = "phoneNumber") String phoneNumber){

            return BillingRecordsViewModel.fromModel(billingService.getBySubscriber(phoneNumber));
    }
    @DeleteMapping("/deleteBySubscriber")
    public void deleteBySubscriber(@RequestParam(name = "phoneNumber") String phoneNumber){
        billingService.deleteBySubscriber(phoneNumber);
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
        public void updateByID (
                @RequestParam(name = "id", required = true) int id,
                @RequestParam(name = "startDate", required = false) String startDate,
                @RequestParam(name = "endDate", required = false) String endDate,
                @RequestParam(name = "amount", required = false) String amount,
                @RequestParam(name = "serviceName" , required = false) String serviceName,
                @RequestParam(name = "currencyName", required = false) String currencyName,
                @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                @RequestParam(name = "payed", required = false) Boolean payed){
        BillingRecord billingRecord = new BillingRecord();
        billingRecord.setId(id);
        if (startDate!=null){
            billingRecord.setStartDate(Date.valueOf(startDate));
        }
        if (endDate!=null){
            billingRecord.setEndDate(Date.valueOf(endDate));
        }
        if (amount != null){
            billingRecord.setAmount(Double.parseDouble(amount));
        }
        if (serviceName != null){
            OfferedService service = new OfferedService(serviceName);
            billingRecord.setOfferedService(service);
        }
        if (currencyName!=null){
            Currency currency = new Currency(currencyName);
            billingRecord.setCurrency(currency);
        }
        if (phoneNumber!=null){
            Subscriber subscriber = new Subscriber(phoneNumber);
            billingRecord.setSubscriber(subscriber);
        }
        if (payed!= null){
            billingRecord.setPayed(payed);
        }
        billingService.update(billingRecord);
    }
    @GetMapping("/date")
        public List<BillingRecord> getByDate(@RequestParam(name = "startDate", required = false, defaultValue = "'%'") String startDate,
                              @RequestParam(name = "endDate",required = false, defaultValue = "2999-12-31") String endDate){
       return billingService.getByDate(startDate, endDate);
    }
    @GetMapping("/payed")
    public List<BillingRecord> searchBills(@RequestParam(name = "status", defaultValue = "'%'") Boolean payed){
        return billingService.searchBills(payed);
    }

}

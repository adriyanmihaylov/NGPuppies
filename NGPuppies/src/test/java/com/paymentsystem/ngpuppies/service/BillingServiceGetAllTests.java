//package com.paymentsystem.ngpuppies.ServiceTests;
//
//import com.paymentsystem.ngpuppies.models.BillingRecord;
//import com.paymentsystem.ngpuppies.repositories.base.BillingRecordRepository;
//import com.paymentsystem.ngpuppies.services.BillingServiceImpl;
//import com.paymentsystem.ngpuppies.services.base.BillingService;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class BillingServiceGetAllTests {
//    @Mock
//    private BillingRecordRepository recordMockRepository;
//
//    private List<BillingRecord> records;
//    private BillingServiceImpl service;
//
//    @Before
//    public void beforeTest() {
//        records = new ArrayList<>();
//        when(recordMockRepository.getAll()).thenReturn(records);
//        service = new BillingServiceImpl(recordMockRepository);
//    }
//
//    @Test
//    public void getAllBillingRecords_returnListOfBillingRecords() {
//        // Arrange
//        records.add(new BillingRecord());
//        records.add(new BillingRecord());
//        records.add(new BillingRecord());
//        records.add(new BillingRecord());
//        records.add(new BillingRecord());
//
//        // Act
//        List<BillingRecord> actualRestaurants = service.getAll();
//
//        // Assert
//
//        Assert.assertEquals(actualRestaurants.size(),records.size());
//    }
//    @Test
//    public void getALlBillingRecords_whenNoRecords_ReturnEmptyList(){
//        List<BillingRecord> records = service.getAll();
//        int actualResult = records.size();
//        int expectedResult = 0;
//        Assert.assertEquals(actualResult,expectedResult);
//    }
//
//
//}

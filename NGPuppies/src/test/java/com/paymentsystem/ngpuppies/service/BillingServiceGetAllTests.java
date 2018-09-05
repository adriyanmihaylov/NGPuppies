//package com.paymentsystem.ngpuppies.ServiceTests;
//
//import com.paymentsystem.ngpuppies.models.Invoice;
//import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
//import com.paymentsystem.ngpuppies.services.BillingRecordServiceImpl;
//import com.paymentsystem.ngpuppies.services.base.InvoiceService;
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
//    private InvoiceRepository recordMockRepository;
//
//    private List<Invoice> records;
//    private BillingRecordServiceImpl service;
//
//    @Before
//    public void beforeTest() {
//        records = new ArrayList<>();
//        when(recordMockRepository.getAll()).thenReturn(records);
//        service = new BillingRecordServiceImpl(recordMockRepository);
//    }
//
//    @Test
//    public void getAllBillingRecords_returnListOfBillingRecords() {
//        // Arrange
//        records.add(new Invoice());
//        records.add(new Invoice());
//        records.add(new Invoice());
//        records.add(new Invoice());
//        records.add(new Invoice());
//
//        // Act
//        List<Invoice> actualRestaurants = service.getAll();
//
//        // Assert
//
//        Assert.assertEquals(actualRestaurants.size(),records.size());
//    }
//    @Test
//    public void getALlBillingRecords_whenNoRecords_ReturnEmptyList(){
//        List<Invoice> records = service.getAll();
//        int actualResult = records.size();
//        int expectedResult = 0;
//        Assert.assertEquals(actualResult,expectedResult);
//    }
//
//
//}

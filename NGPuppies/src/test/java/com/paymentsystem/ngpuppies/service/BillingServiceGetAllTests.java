package com.paymentsystem.ngpuppies.ServiceTests;

import com.paymentsystem.ngpuppies.models.Invoice;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.paymentsystem.ngpuppies.services.InvoiceServiceImpl;
import com.paymentsystem.ngpuppies.services.base.InvoiceService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BillingServiceGetAllTests {

    @Mock
    private InvoiceRepository invoiceMockRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private List<Invoice> invoices;

    @Before
    public void beforeTest() {
        invoices = new ArrayList<>();
        when(invoiceMockRepository.getAll()).thenReturn(invoices);
    }

    @Test
    public void getAllInvoices_shouldReturnSameListSize() {
        // Arrange
        invoices.add(new Invoice());
        invoices.add(new Invoice());
        invoices.add(new Invoice());
        invoices.add(new Invoice());
        invoices.add(new Invoice());

        // Act
        List<Invoice> actualRestaurants = invoiceService.getAll();

        // Assert

        Assert.assertEquals(actualRestaurants.size(), invoices.size());
    }
    @Test
    public void getALlBillingRecords_whenNoRecords_ReturnEmptyList(){
        List<Invoice> records = invoiceService.getAll();
        int actualResult = records.size();
        int expectedResult = 0;
        Assert.assertEquals(actualResult,expectedResult);
    }


}

package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.*;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.CurrencyRepository;
import com.paymentsystem.ngpuppies.repositories.base.InvoiceRepository;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.repositories.base.TelecomServRepository;
import com.paymentsystem.ngpuppies.services.InvoiceServiceImpl;
import com.paymentsystem.ngpuppies.web.dto.InvoiceDTO;
import com.paymentsystem.ngpuppies.web.dto.InvoicePaymentDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class InvoiceServiceImplTests {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private SubscriberRepository subscriberRepository;
    @Mock
    private TelecomServRepository telecomServRepository;
    @Mock
    private CurrencyRepository currencyRepository;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    private List<Invoice> mockListInvoices;

    private static final String VALID_EGN = "8412125487";
    private static final String VALID_PHONE_NUMBER = "878998778";
    private static Integer VALID_INVOICE_ID = 1;

    private static Invoice mockInvoice;
    private static Subscriber mockSubscriber;
    private TelecomServ mockTelecomServ;

    private static final Integer TELECOMSERV_ID = 1;
    private static final String VALID_TELECOMSERV_NAME = "INTERNET";

    private static final LocalDate VALID_FROM_DATE = LocalDate.parse("2015-10-10");
    private static final LocalDate VALID_TO_DATE = LocalDate.parse("2016-11-11");
    private static final String INVALID_FROM_DATE = "10-10a2010";
    private static final String INVALID_TO_DATE = "10-10-20000";
    private static final String VALID_DATE_STRING = "2018-05-06";
    private static final double BGN_AMOUNT = 145.55;
    private static final String VALID_BGN_AMOUNT = "145.55";

    private static InvoiceDTO INVOICEDTO_WITH_INVALID_DATE;
    private static InvoiceDTO mockInvoiceDto;
    private static Client mockClient;

    @Before
    public void beforeTest() {
        mockSubscriber = new Subscriber("Ivan", "Ivanov", VALID_PHONE_NUMBER, VALID_EGN, new Address(), 0D);
        mockSubscriber.setId(1);
        mockTelecomServ = new TelecomServ("TV");
        mockTelecomServ.setId(TELECOMSERV_ID);
        mockInvoice = new Invoice(mockSubscriber, VALID_FROM_DATE, VALID_TO_DATE, BGN_AMOUNT, mockTelecomServ);
        mockInvoice.setId(1);

        mockListInvoices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            mockListInvoices.add(new Invoice());
        }
        INVOICEDTO_WITH_INVALID_DATE = new InvoiceDTO(VALID_PHONE_NUMBER, INVALID_FROM_DATE, VALID_TO_DATE.toString(), VALID_BGN_AMOUNT, mockTelecomServ.getName());
        mockInvoiceDto = new InvoiceDTO(VALID_PHONE_NUMBER, VALID_FROM_DATE.toString(), VALID_TO_DATE.toString(), VALID_BGN_AMOUNT, mockTelecomServ.getName());

        Authority authority = new Authority(AuthorityName.ROLE_CLIENT);
        mockClient = new Client("username", "password", "eik", authority);
        mockClient.setId(1);
    }


    @Test
    public void getById_whenNoPresentInvoice_shouldReturnNull() {
        when(invoiceRepository.getById(VALID_INVOICE_ID)).thenReturn(null);

        Invoice result = invoiceService.getById(VALID_INVOICE_ID);

        Assert.assertNull(result);
    }

    @Test
    public void getAllInvoices_shouldReturnSameListSize() {
        // Arrange
        when(invoiceRepository.getAll()).thenReturn(mockListInvoices);

        // Act
        List<Invoice> resultList = invoiceService.getAll();

        // Assert
        Assert.assertEquals(mockListInvoices.size(), resultList.size());
    }

    @Test
    public void create_whenSubscriberIsNotPresentInDB_shouldReturnInvoiceDTO() {
        when(subscriberRepository.getSubscriberByPhoneNumber(mockInvoiceDto.getSubscriberPhone())).thenReturn(null);

        InvoiceDTO result = invoiceService.create(mockInvoiceDto);

        Assert.assertEquals(mockInvoiceDto, result);
    }

    @Test
    public void create_whenTelecomServIsNotPresentInDB_shouldReturnInvoiceDTO() {
        when(subscriberRepository.getSubscriberByPhoneNumber(mockInvoiceDto.getSubscriberPhone())).thenReturn(mockSubscriber);
        when(telecomServRepository.getByName(mockInvoiceDto.getService())).thenReturn(null);

        InvoiceDTO result = invoiceService.create(mockInvoiceDto);

        Assert.assertEquals(mockInvoiceDto, result);
    }

    @Test
    public void create_whenSubscriberNotUsingThisTelecomServ_shouldReturnInvoiceDTO() {
        when(subscriberRepository.getSubscriberByPhoneNumber(mockInvoiceDto.getSubscriberPhone())).thenReturn(mockSubscriber);
        when(telecomServRepository.getByName(mockInvoiceDto.getService())).thenReturn(mockTelecomServ);

        InvoiceDTO result = invoiceService.create(mockInvoiceDto);

        Assert.assertEquals(mockInvoiceDto, result);
    }

    @Test
    public void create_whenRepositoryDoesNotSaveInvoice_shouldReturnInvoiceDTO() {
        mockSubscriber.addSubscriberServices(mockTelecomServ);
        when(subscriberRepository.getSubscriberByPhoneNumber(mockInvoiceDto.getSubscriberPhone())).thenReturn(mockSubscriber);
        when(telecomServRepository.getByName(mockInvoiceDto.getService())).thenReturn(mockTelecomServ);
        when(invoiceRepository.create(any(Invoice.class))).thenReturn(false);

        InvoiceDTO expectedResult = invoiceService.create(mockInvoiceDto);

        Assert.assertEquals(mockInvoiceDto, expectedResult);
    }

    @Test
    public void create_whenDatesAreInvalid_shouldReturnInvoiceDTO() {
        mockSubscriber.addSubscriberServices(mockTelecomServ);
        when(subscriberRepository.getSubscriberByPhoneNumber(mockInvoiceDto.getSubscriberPhone())).thenReturn(mockSubscriber);
        when(telecomServRepository.getByName(mockInvoiceDto.getService())).thenReturn(mockTelecomServ);
        when(invoiceRepository.create(any(Invoice.class))).thenReturn(true);

        InvoiceDTO result = invoiceService.create(INVOICEDTO_WITH_INVALID_DATE);

        Assert.assertNotNull(result);
    }

    @Test
    public void create_whenSuccessful_shouldReturnNull() {
        mockSubscriber.addSubscriberServices(mockTelecomServ);
        when(subscriberRepository.getSubscriberByPhoneNumber(mockInvoiceDto.getSubscriberPhone())).thenReturn(mockSubscriber);
        when(telecomServRepository.getByName(mockInvoiceDto.getService())).thenReturn(mockTelecomServ);
        when(invoiceRepository.create(any(Invoice.class))).thenReturn(true);

        InvoiceDTO result = invoiceService.create(mockInvoiceDto);

        Assert.assertNull(result);
    }

    @Test(expected = InvalidParameterException.class)
    public void delete_whenInvoiceIsNotFound_shouldThrowException() {
        when(invoiceRepository.getById(mockInvoice.getId())).thenReturn(null);

        invoiceService.delete(mockInvoice.getId(), mockInvoice.getSubscriber().getPhone());
    }

    @Test(expected = InvalidParameterException.class)
    public void delete_whenSubscriberPhoneDoesNotEqualsInvoiceSubscriberPhone_shouldThrowException() {
        String subscriberPhone = "text string";
        when(invoiceRepository.getById(mockInvoice.getId())).thenReturn(mockInvoice);

        invoiceService.delete(mockInvoice.getId(), subscriberPhone);
    }

    @Test
    public void delete_whenSuccessful_shouldReturnTrue() {
        when(invoiceRepository.getById(mockInvoice.getId())).thenReturn(mockInvoice);
        when(invoiceRepository.delete(mockInvoice)).thenReturn(true);

        boolean result = invoiceService.delete(mockInvoice.getId(), mockInvoice.getSubscriber().getPhone());

        Assert.assertTrue(result);
    }

    @Test
    public void getAllInvoicesOfSubscriberBySubscriberId_whenSubscriberIsPresent_shouldReturnList() {
        when(invoiceRepository.getAllInvoicesOfSubscriberBySubscriberId(mockSubscriber.getId())).thenReturn(mockSubscriber.getInvoices());

        List<Invoice> resultList = invoiceService.getAllInvoicesOfSubscriberBySubscriberId(mockSubscriber.getId());

        Assert.assertEquals(mockSubscriber.getInvoices(), resultList);
    }

    @Test
    public void payInvoices_whenClientIdIsNotTheSameAsSubscriberClientId_shouldReturnListOfUnpaidInvoices() {
        InvoicePaymentDTO invoicePaymentDTO = new InvoicePaymentDTO(1, "EUR");
        List<InvoicePaymentDTO> paymentDTOList = new ArrayList<>();
        paymentDTOList.add(invoicePaymentDTO);
        mockInvoice.getSubscriber().setClient(mockClient);
        when(invoiceRepository.getById(invoicePaymentDTO.getId())).thenReturn(mockInvoice);

        List<InvoicePaymentDTO> resultList = invoiceService.payInvoices(paymentDTOList, mockInvoice.getSubscriber().getClient().getId() + 1);

        Assert.assertEquals(paymentDTOList, resultList);
    }

    @Test
    public void payInvoices_whenCurrencyIsNotTheSameAsInput_shouldReturnListOfUnpaidInvoices() {
        InvoicePaymentDTO invoicePaymentDTO = new InvoicePaymentDTO(1, "EUR");
        List<InvoicePaymentDTO> paymentDTOList = new ArrayList<>();
        paymentDTOList.add(invoicePaymentDTO);
        mockInvoice.getSubscriber().setClient(mockClient);
        when(invoiceRepository.getById(invoicePaymentDTO.getId())).thenReturn(mockInvoice);
        when(currencyRepository.getByName(invoicePaymentDTO.getCurrency())).thenReturn(null);

        List<InvoicePaymentDTO> resultList = invoiceService.payInvoices(paymentDTOList, mockInvoice.getSubscriber().getClient().getId());

        Assert.assertEquals(paymentDTOList, resultList);
    }

    @Test
    public void payInvoices_whenSuccess_shouldReturnEmptyList() {
        Currency currency = new Currency("eur");
        InvoicePaymentDTO invoicePaymentDTO = new InvoicePaymentDTO(1, currency.getName());
        List<InvoicePaymentDTO> paymentDTOList = new ArrayList<>();
        paymentDTOList.add(invoicePaymentDTO);
        mockInvoice.getSubscriber().setClient(mockClient);

        when(invoiceRepository.getById(invoicePaymentDTO.getId())).thenReturn(mockInvoice);
        when(currencyRepository.getByName(invoicePaymentDTO.getCurrency())).thenReturn(currency);
        when(invoiceRepository.payInvoice(any(Invoice.class))).thenReturn(true);

        List<InvoicePaymentDTO> resultList = invoiceService.payInvoices(paymentDTOList, mockInvoice.getSubscriber().getClient().getId());

        Assert.assertEquals(0, resultList.size());
    }

    @Test(expected = InvalidParameterException.class)
    public void getSubscriberInvoicesFromDateToDate_whenDateIsInvalid_shouldThrowException() {
        invoiceService.getSubscriberInvoicesFromDateToDate(VALID_PHONE_NUMBER, INVALID_FROM_DATE, INVALID_TO_DATE);
    }

    @Test
    public void getSubscriberInvoicesFromDateToDate_whenSuccess_shouldReturnListInvoices() {
        when(invoiceRepository.getSubscriberPaidInvoicesFromDateToDate(VALID_PHONE_NUMBER, VALID_FROM_DATE, VALID_TO_DATE)).thenReturn(mockListInvoices);

        invoiceService.getSubscriberInvoicesFromDateToDate(VALID_PHONE_NUMBER, VALID_FROM_DATE.toString(), VALID_TO_DATE.toString());
    }

    @Test(expected = InvalidParameterException.class)
    public void getSubscriberLargestPaidInvoiceForPeriodOfTime_whenDatesAreInvalid_shouldTrowException() {
        invoiceService.getSubscriberLargestPaidInvoiceForPeriodOfTime(mockSubscriber, INVALID_FROM_DATE, INVALID_FROM_DATE);
    }

    @Test(expected = InvalidParameterException.class)
    public void getSubscriberLargestPaidInvoiceForPeriodOfTime_whenSubscriberIsNull_shouldTrowException() {
        invoiceService.getSubscriberLargestPaidInvoiceForPeriodOfTime(null, VALID_DATE_STRING, VALID_DATE_STRING);
    }

    @Test
    public void getSubscriberLargestPaidInvoiceForPeriodOfTime_whenSubscriberIsPresentAndDatesAreEqual_shouldReturnInvoice() {
        when(invoiceService.getSubscriberLargestPaidInvoiceForPeriodOfTime(mockSubscriber, VALID_DATE_STRING, VALID_DATE_STRING)).thenReturn(mockInvoice);

        Invoice result = invoiceService.getSubscriberLargestPaidInvoiceForPeriodOfTime(mockSubscriber, VALID_DATE_STRING, VALID_DATE_STRING);

        Assert.assertNotNull(result);
    }

    @Test
    public void getAllUnpaidInvoicesOfService_whenServiceIsPresent_shouldReturnListOfInvoices() {
        when(invoiceRepository.getAllUnpaidInvoicesOfService(mockTelecomServ.getName())).thenReturn(mockListInvoices);

        List<Invoice> result = invoiceService.getAllUnpaidInvoicesOfService(mockTelecomServ.getName());

        Assert.assertEquals(mockListInvoices,result);
    }

    @Test(expected = InvalidParameterException.class)
    public void geAllUnpaidInvoicesFromDateToDate_whenToDateIsBeforeFromDate_shouldTrowException() {
        invoiceService.geAllUnpaidInvoicesFromDateToDate(VALID_TO_DATE.toString(),VALID_FROM_DATE.toString());
    }

    @Test
    public void geAllUnpaidInvoicesFromDateToDate_whenSuccess_shouldReturnListOfInvoices() {
        when(invoiceRepository.geAllUnpaidInvoicesFromDateToDate(VALID_FROM_DATE,VALID_TO_DATE)).thenReturn(mockListInvoices);

        List<Invoice> result = invoiceService.geAllUnpaidInvoicesFromDateToDate(VALID_FROM_DATE.toString(),VALID_TO_DATE.toString());

        Assert.assertEquals(mockListInvoices, result);
    }

    @Test(expected = InvalidParameterException.class)
    public void getAllUnpaidInvoicesOfSubscriberInDescOrder_whenSubscriberPhoneIsNull_shouldThrowException() {
        invoiceService.getAllUnpaidInvoicesOfSubscriberInDescOrder(null);
    }
    @Test
    public void getAllUnpaidInvoicesOfSubscriberInDescOrder_whenSuccessful_shoutReturnListOfInvoices(){
        when(invoiceRepository.getAllUnpaidInvoicesOfSubscriberInDescOrder(mockSubscriber.getPhone())).thenReturn(mockListInvoices);

        List<Invoice> result = invoiceService.getAllUnpaidInvoicesOfSubscriberInDescOrder(mockSubscriber.getPhone());

        Assert.assertEquals(mockListInvoices,result);
    }
    @Test
    public void getTenMostRecentInvoices_whenSuccess_shouldReturnListOfInvoices() {
        when(invoiceRepository.getTenMostRecentInvoices(mockClient.getId())).thenReturn(mockListInvoices);

        List<Invoice> result = invoiceService.getTenMostRecentInvoices(mockClient.getId());

        Assert.assertEquals(mockListInvoices, result);
    }

    @Test
    public void getAllUnpaidInvoices_whenSuccess_shouldReturnListOfInvoices() {
        when(invoiceRepository.getAllUnpaidInvoices()).thenReturn(mockListInvoices);

        List<Invoice> result = invoiceService.getAllUnpaidInvoices();

        Assert.assertEquals(mockListInvoices, result);
    }

    @Test
    public void geAllUnpaidInvoicesOfAllClientSubscribers_whenSuccess_shouldReturnListOfInvoices() {
        when(invoiceRepository.geAllUnpaidInvoicesOfAllClientSubscribers(mockClient.getId())).thenReturn(mockListInvoices);

        List<Invoice> result = invoiceService.geAllUnpaidInvoicesOfAllClientSubscribers(mockClient.getId());

        Assert.assertEquals(mockListInvoices, result);
    }
}
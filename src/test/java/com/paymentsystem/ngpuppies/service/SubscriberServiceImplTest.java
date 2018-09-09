package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.ClientRepositoryImpl;
import com.paymentsystem.ngpuppies.repositories.TelecomServRepositoryImpl;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.services.SubscriberServiceImpl;
import com.paymentsystem.ngpuppies.web.dto.SubscriberDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.rmi.AlreadyBoundException;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SubscriberServiceImplTest {

    private static final String VALID_CLIENT_NAME = "client";
    @Mock
    private SubscriberRepository subscribersRepository;

    @Mock
    private ClientRepositoryImpl clientRepository;

    @Mock
    private TelecomServRepositoryImpl telecomServRepository;

    @InjectMocks
    private SubscriberServiceImpl subscriberService;

    private List<Subscriber> subscriberList;

    private Subscriber mockedSubscriber;
    private Client mockedClient;
    private TelecomServ mockedTelecomServ;
    private SubscriberDTO subscriberDTO;

    private static final String VALID_PHONE_NUMBER = "878998778";
    private static final String INVALID_PHONE_NUMBER = "899999999";
    private static final String VALID_TELECOMSERV_NAME = "INTERNET";
    private static final String VALID_EGN = "8412125487";

    private static final Integer VALID_CLIENT_ID = 1;
    private static final Integer VALID_SUBSCRIBER_ID = 1;
    private static final Integer VALID_TELECOMSERV_ID = 1;


    private static final String VALID_FROM_DATE = "2015-10-10";
    private static final String VALID_TO_DATE = "2016-11-11";
    private static final String INVALID_TO_DATE = "2015-05-10";


    @Before
    public void beforeTest() {
        mockedTelecomServ = new TelecomServ("TV");
        mockedTelecomServ.setId(VALID_TELECOMSERV_ID);

        mockedClient = new Client(VALID_CLIENT_NAME, "123456", "123412341", new Authority(AuthorityName.ROLE_CLIENT));
        mockedClient.setId(VALID_CLIENT_ID);

        mockedSubscriber = new Subscriber("John", "Ivanov", VALID_PHONE_NUMBER, VALID_EGN, new Address(), (double) 0);
        mockedSubscriber.setClient(mockedClient);
        mockedSubscriber.setId(VALID_SUBSCRIBER_ID);

        subscriberDTO = new SubscriberDTO(VALID_PHONE_NUMBER, "John", "Ivanov", VALID_EGN, new Address(), mockedClient.getUsername());

        subscriberList = new ArrayList<>();
        subscriberList.add(mockedSubscriber);
        subscriberList.add(new Subscriber());
        subscriberList.add(new Subscriber());
    }

    @Test
    public void getALlSubscribers_returnListOfSubscribers() {
        // Arrange
        when(subscribersRepository.getAll()).thenReturn(subscriberList);
        // Act

        List<Subscriber> actualRestaurants = subscriberService.getAll();

        // Assert
        Assert.assertEquals(actualRestaurants.size(), subscriberList.size());
    }

    @Test
    public void getAllSubscribers_whenNoSubscribers_ReturnEmptyList() {
        when(subscribersRepository.getAll()).thenReturn(new ArrayList<>());

        List<Subscriber> subscribers = subscriberService.getAll();

        Assert.assertEquals(subscribers.size(), 0);
    }

    @Test
    public void getSubscriberByPhone_whenSubscriberPresent_returnSubscriber() {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);

        Subscriber subscriber = subscriberService.getSubscriberByPhone(VALID_PHONE_NUMBER);

        Assert.assertEquals(subscriber.getPhone(), mockedSubscriber.getPhone());
    }

    @Test
    public void getSubscriberByPhone_whenSubscriberNotPresent_returnNull() {
        when(subscribersRepository.getSubscriberByPhoneNumber(INVALID_PHONE_NUMBER)).thenReturn(null);

        Subscriber subscriber = subscriberService.getSubscriberByPhone(INVALID_PHONE_NUMBER);

        Assert.assertNull(subscriber);
    }

    @Test(expected = InvalidParameterException.class)
    public void create_whenClientIsMissing_shouldThrowException() throws SQLException {
        when(clientRepository.loadByUsername(subscriberDTO.getClient())).thenReturn(null);

        subscriberService.create(subscriberDTO);
    }

    @Test
    public void create_whenClientIsPresent_shouldReturnTrue() throws SQLException {
        when(clientRepository.loadByUsername(subscriberDTO.getClient())).thenReturn(mockedClient);
        when(subscribersRepository.create(any(Subscriber.class))).thenReturn(true);

        boolean isExecuted = subscriberService.create(subscriberDTO);

        Assert.assertTrue(isExecuted);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_whenSubscriberIsNotPresent_shouldThrowException() throws SQLException {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(null);

        subscriberService.update(VALID_PHONE_NUMBER, subscriberDTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void update_whenClientIsNotPresent_shouldThrowException() throws SQLException {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);
        when(clientRepository.loadByUsername(VALID_CLIENT_NAME)).thenReturn(null);

        subscriberService.update(VALID_PHONE_NUMBER, subscriberDTO);
    }

    @Test
    public void update_whenSubscriberAndClientArePresent_shouldReturnTrue() throws SQLException {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);
        when(clientRepository.loadByUsername(VALID_CLIENT_NAME)).thenReturn(mockedClient);
        when(subscribersRepository.update(any(Subscriber.class))).thenReturn(true);

        Boolean isSuccessful = subscriberService.update(VALID_PHONE_NUMBER, subscriberDTO);
        Assert.assertTrue(isSuccessful);
    }

    @Test
    public void deleteSubscriberByNumber_whenSuccessful_returnTrue() {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);
        when(subscribersRepository.delete(mockedSubscriber)).thenReturn(true);

        Boolean isSuccessful = subscriberService.deleteSubscriberByNumber(VALID_PHONE_NUMBER);

        Assert.assertTrue(isSuccessful);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteSubscriberByNumber_whenSubscriberIsNotPresent_shouldTrowException() {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(null);

        subscriberService.deleteSubscriberByNumber(VALID_PHONE_NUMBER);
    }

    @Test(expected = InvalidParameterException.class)
    public void addServiceToSubscriber_whenSubscriberIsNotPresent_shouldThrowException() throws AlreadyBoundException, SQLException {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(null);

        subscriberService.addServiceToSubscriber(VALID_PHONE_NUMBER, mockedTelecomServ.getName());
    }

    @Test(expected = InvalidParameterException.class)
    public void addServiceToSubscriber_whenTelecomServIsNotPresent_shouldThrowException() throws AlreadyBoundException, SQLException {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);
        when(telecomServRepository.getByName(mockedTelecomServ.getName())).thenReturn(null);

        subscriberService.addServiceToSubscriber(VALID_PHONE_NUMBER, mockedTelecomServ.getName());
    }

    @Test(expected = AlreadyBoundException.class)
    public void addServiceToSubscriber_whenSubscriberIsAlreadyUsingThisTelecomService_shouldThrowException() throws AlreadyBoundException, SQLException {
        mockedSubscriber.addSubscriberServices(mockedTelecomServ);

        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);
        when(telecomServRepository.getByName(mockedTelecomServ.getName())).thenReturn(mockedTelecomServ);

        subscriberService.addServiceToSubscriber(VALID_PHONE_NUMBER, mockedTelecomServ.getName());
    }

    @Test
    public void addServiceToSubscriber_whenSuccessful_shouldReturnTrue() throws AlreadyBoundException, SQLException {
        when(subscribersRepository.getSubscriberByPhoneNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);
        when(telecomServRepository.getByName(mockedTelecomServ.getName())).thenReturn(mockedTelecomServ);
        when(subscribersRepository.update(mockedSubscriber)).thenReturn(true);

        boolean isExecuted = subscriberService.addServiceToSubscriber(VALID_PHONE_NUMBER, mockedTelecomServ.getName());

        Assert.assertTrue(isExecuted);
    }

    @Test
    public void getTenAllTimeSubscribersOfClientWithBiggestBillsPaid_whenClientIsPresent_shouldReturnSubscribersList() {
        when(subscribersRepository.getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(VALID_SUBSCRIBER_ID)).thenReturn(subscriberList);

        List<Subscriber> resultList = subscriberService.getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(VALID_CLIENT_ID);

        Assert.assertEquals(subscriberList.size(), resultList.size());
    }

    @Test
    public void getTenAllTimeSubscribersOfClientWithBiggestBillsPaid_whenClientIsNotPresent_shouldReturnEmptyList() {
        when(subscribersRepository.getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(VALID_SUBSCRIBER_ID)).thenReturn(new ArrayList<>());

        List<Subscriber> resultList = subscriberService.getTenAllTimeSubscribersOfClientWithBiggestBillsPaid(VALID_CLIENT_ID);

        Assert.assertEquals(0, resultList.size());
    }

    @Test
    public void getAllSubscribersUsingServiceByServiceName_whenServiceIsPresent_shouldReturnSubsribersList() {
        when(telecomServRepository.getByName(VALID_TELECOMSERV_NAME)).thenReturn(mockedTelecomServ);
        when(subscribersRepository.getAllSubscribersByService(mockedTelecomServ.getId())).thenReturn(subscriberList);

        List<Subscriber> resultList = subscriberService.getAllSubscribersUsingServiceByServiceName(VALID_TELECOMSERV_NAME);

        Assert.assertEquals(subscriberList.size(), resultList.size());
    }

    @Test(expected = InvalidParameterException.class)
    public void getSubscriberAverageSumOfPaidInvoices_whenDatesAreInvalid_shoudThrowException() {
        subscriberService.getSubscriberAverageSumOfPaidInvoices(mockedSubscriber, VALID_FROM_DATE, INVALID_TO_DATE);
    }

    @Test(expected = InvalidParameterException.class)
    public void getSubscriberAverageSumOfPaidInvoices_whenSubscriberIsNotPresent_shoudThrowException() {
        subscriberService.getSubscriberAverageSumOfPaidInvoices(null, VALID_FROM_DATE, VALID_TO_DATE);
    }

    @Test
    public void getSubscriberAverageSumOfPaidInvoices_whenSuccessful_shouldReturnAverageAmount() {
        double target = 33.33;
        when(subscribersRepository.getSubscriberAverageInvoiceSumPaid(mockedSubscriber.getId(), VALID_FROM_DATE, VALID_TO_DATE)).thenReturn(target);

        double result = subscriberService.getSubscriberAverageSumOfPaidInvoices(mockedSubscriber, VALID_FROM_DATE, VALID_TO_DATE);

        Assert.assertEquals(target, result, 0);
    }


    @Test
    public void getSubscriberOfClientWithBiggestAmountPaid_whenSuccessful_shouldReturnSubscriberAndTotalAmountForThePeriodOfTime() {
        double targetAmount = 44.31;
        Object[] object = new Object[1];
        object[0] = new Object[]{mockedSubscriber, targetAmount};


        when(subscribersRepository.getSubscriberOfClientWithBiggestAmountPaid(VALID_CLIENT_ID, VALID_FROM_DATE, VALID_TO_DATE))
                .thenReturn(object);

        Map<Subscriber, Double> result = subscriberService.getSubscriberOfClientWithBiggestAmountPaid(VALID_CLIENT_ID, VALID_FROM_DATE, VALID_TO_DATE);

        for (Map.Entry<Subscriber, Double> entry : result.entrySet()) {
            Assert.assertEquals(entry.getKey(), mockedSubscriber);
            Assert.assertEquals(targetAmount, entry.getValue(), 0);
        }
    }
}
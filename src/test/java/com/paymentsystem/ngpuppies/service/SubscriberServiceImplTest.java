package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;

import com.paymentsystem.ngpuppies.models.TelecomServ;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.services.SubscriberServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.rmi.AlreadyBoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberServiceImplTest {

    @Mock
    private SubscriberRepository subscribersRepository;

    @InjectMocks
    private SubscriberServiceImpl subscriberService;

    private List<Subscriber> mockedSubscribersList;

    private Subscriber mockedSubscriber;
    private Client mockedClient;
    private TelecomServ mockedTelecomServ;

    private static final String VALID_PHONE_NUMBER = "878998778";
    private static final String INVALID_PHONE_NUMBER = "899999999";

    private static final String VALID_EGN = "8412125487";
    private static final String INVALID_EGN = "7070801035";

    private static final Integer VALID_CLIENT_ID = 1;
    private static final Integer VALID_SUBSCRIBER_ID = 1;
    private static final Integer VALID_TELECOMSERV_ID = 1;


    @Before
    public void beforeTest() {
        mockedTelecomServ = new TelecomServ("TV");
        mockedTelecomServ.setId(VALID_TELECOMSERV_ID);

        mockedClient = new Client("client", "123456", "123412341", new Authority(AuthorityName.ROLE_CLIENT));
        mockedClient.setId(VALID_CLIENT_ID);

        mockedSubscriber = new Subscriber("John", "Mcdonald", VALID_PHONE_NUMBER, VALID_EGN, new Address(), (double) 0);
        mockedSubscriber.setClient(mockedClient);
        mockedSubscriber.setId(VALID_SUBSCRIBER_ID);

        mockedSubscribersList = new ArrayList<>();
        mockedSubscribersList.add(mockedSubscriber);
    }

    @Test
    public void getALlSubscribers_returnListOfSubscribers() {
        // Arrange
        when(subscribersRepository.getAll()).thenReturn(mockedSubscribersList);
        // Act

        List<Subscriber> actualRestaurants = subscriberService.getAll();

        // Assert
        Assert.assertEquals(actualRestaurants.size(), mockedSubscribersList.size());
    }

    @Test
    public void getAllSubscribers_whenNoSubscribers_ReturnEmptyList() {
        when(subscribersRepository.getAll()).thenReturn(new ArrayList<>());

        List<Subscriber> subscribers = subscriberService.getAll();

        Assert.assertEquals(subscribers.size(), 0);
    }

    @Test
    public void getSubscriberByPhone_whenSubscriberPresent_returnSubscriber() {
        when(subscribersRepository.getByNumber(VALID_PHONE_NUMBER)).thenReturn(mockedSubscriber);

        Subscriber subscriber = subscriberService.getSubscriberByPhone(VALID_PHONE_NUMBER);

        Assert.assertEquals(subscriber.getPhone(), mockedSubscriber.getPhone());
    }

    @Test
    public void getSubscriberByPhone_whenSubscriberNotPresent_returnNull() {
        when(subscribersRepository.getByNumber(INVALID_PHONE_NUMBER)).thenReturn(null);

        Subscriber subscriber = subscriberService.getSubscriberByPhone(INVALID_PHONE_NUMBER);

        Assert.assertNull(subscriber);
    }

    @Test
    public void create_whenSuccessful_returnTrue() throws SQLException {
        when(subscribersRepository.create(mockedSubscriber)).thenReturn(true);

        Boolean isSuccessful = subscriberService.create(mockedSubscriber);

        Assert.assertTrue(isSuccessful);
    }

    @Test
    public void update_whenSuccessful_returnTrue() throws SQLException {
        when(subscribersRepository.update(mockedSubscriber)).thenReturn(true);

        Boolean isSuccessful = subscriberService.update(mockedSubscriber);

        Assert.assertTrue(isSuccessful);
    }

    @Test
    public void delete_whenSuccessful_returnTrue() throws SQLException {
        when(subscribersRepository.update(mockedSubscriber)).thenReturn(true);

        Boolean isSuccessful = subscriberService.delete(mockedSubscriber);

        Assert.assertTrue(isSuccessful);
    }

    @Test
    public void getAllSubscribersByService_whenSuccessful_returnListSubscribers() throws SQLException {
        when(subscribersRepository.getAllSubscribersByService(VALID_TELECOMSERV_ID)).thenReturn(mockedSubscribersList);

        List<Subscriber> subscribers = subscriberService.getAllSubscribersByService(VALID_TELECOMSERV_ID);

        Assert.assertEquals(subscribers.get(0), mockedSubscribersList.get(0));
    }

    @Test
    public void getTenAllTimeSubscribersWithBiggestBillsPaid_whenSubscribersPresent_returnListSubscribers() {
        when(subscribersRepository.getTenAllTimeSubscribersWithBiggestBillsPaid(VALID_CLIENT_ID)).thenReturn(mockedSubscribersList);

        List<Subscriber> subscribers = subscriberService.getTenAllTimeSubscribersWithBiggestBillsPaid(VALID_CLIENT_ID);

        Assert.assertEquals(subscribers.get(0), mockedSubscribersList.get(0));
    }

    @Test
    public void getTenAllTimeSubscribersWithBiggestBillsPaid_whenSubscribersNotPresent_returnEmptyList() {
        when(subscribersRepository.getTenAllTimeSubscribersWithBiggestBillsPaid(VALID_CLIENT_ID)).thenReturn(new ArrayList<>());

        List<Subscriber> subscribers = subscriberService.getTenAllTimeSubscribersWithBiggestBillsPaid(VALID_CLIENT_ID);

        Assert.assertTrue(subscribers.size() == 0);
    }

    @Test
    public void addServiceToSubscriber_whenSubscriberDoesNotHaveService_shouldReturnTrue() throws SQLException, AlreadyBoundException {
        when(subscribersRepository.update(mockedSubscriber)).thenReturn(true);

        Boolean isSuccessful = subscriberService.addServiceToSubscriber(mockedSubscriber, mockedTelecomServ);

        Assert.assertTrue(isSuccessful);
    }

    @Test(expected = AlreadyBoundException.class)
    public void addServiceToSubscriber_whenSubscriberAlreadyHasService_shouldTrowException() throws SQLException, AlreadyBoundException {
        mockedSubscriber.addSubscriberServices(mockedTelecomServ);
        when(subscribersRepository.update(mockedSubscriber)).thenReturn(true);

        Boolean isSuccessful = subscriberService.addServiceToSubscriber(mockedSubscriber, mockedTelecomServ);

        Assert.assertTrue(isSuccessful);
    }

    @Test
    public void getSubscriberAverageSumOfPaidInvoices_shouldReturnAverageSumOfPaidInvoices() {
        when(subscribersRepository.getSubscriberAverageInvoiceSumPaid(VALID_SUBSCRIBER_ID, "2010-10-10", "2010-11-10")).thenReturn(1.43333);

        Double result = subscriberService.getSubscriberAverageSumOfPaidInvoices(VALID_SUBSCRIBER_ID, "2010-10-10", "2010-11-10");

        Assert.assertEquals(1.43333, result, 0);
    }

//    @Test
//    public void getSubscriberOfClientWithBiggestAmountPaid_shouldReturnSubscriberAndTotalAmountForThePeriodOfTime() {
//        Subscriber subscriber = new Subscriber();
//
//        Object[] object = new Object[1];
//        object[0] = new Object[]{new Subscriber[]{subscriber}, 27.666D};
//
//
//        when(subscribersRepository.getSubscriberOfClientWithBiggestAmountPaid(VALID_CLIENT_ID, "2010-10-10", "2010-11-10"))
//                .thenReturn(object);
//
//        Map<Subscriber, Double> result = subscriberService.getSubscriberOfClientWithBiggestAmountPaid(VALID_CLIENT_ID, "2010-10-10", "2010-11-10");
//
//        for (Map.Entry<Subscriber, Double> entry : result.entrySet()) {
//            Assert.assertEquals(entry.getKey(), subscriber);
//            Assert.assertEquals(27.666, entry.getValue(), 0);
//        }
//    }
}
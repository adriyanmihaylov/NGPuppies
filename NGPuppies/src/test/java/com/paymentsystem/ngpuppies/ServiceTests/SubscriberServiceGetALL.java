package com.paymentsystem.ngpuppies.ServiceTests;

import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.models.Subscriber;
import com.paymentsystem.ngpuppies.repositories.SubscribersRepositoryImpl;

import com.paymentsystem.ngpuppies.services.SubscriberServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscriberServiceGetALL {
    @Mock
    private SubscribersRepositoryImpl subscribersRepository;

    private List<Subscriber> subscribers;
    private SubscriberServiceImpl service;

    @Before
    public void beforeTest() {
        subscribers = new ArrayList<>();
        when(subscribersRepository.getAll()).thenReturn(subscribers);
        service = new SubscriberServiceImpl(subscribersRepository);
    }

    @Test
    public void getALlSubscribers_returnListOfSubscribers() {
        // Arrange
        Client newClient = new Client("UserNameTest1", "PasswordTest1", "EIKTEST" );
        subscribers.add(new Subscriber("087080156",
                "FN1", "LN1", "EGN", "DSK"));
        subscribers.add(new Subscriber("087081156",
                "FN2", "LN2", "EGN2", "DSK"));
        subscribers.add(new Subscriber("087080126",
                "FN3", "LN3", "EGN3", "DSK"));
        // Act
        List<Subscriber> actualRestaurants = service.getAll();

        // Assert

        Assert.assertEquals(actualRestaurants.size(),subscribers.size());
    }
    @Test
    public void getAllSubscribers_whenNoSubscribers_ReturnEmptyList(){
        List<Subscriber> subscribers = service.getAll();
        int actualResult = subscribers.size();
        int expectedResult = 0;
        Assert.assertEquals(actualResult,expectedResult);
    }




}

package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.Address;
import com.paymentsystem.ngpuppies.models.Subscriber;

import com.paymentsystem.ngpuppies.repositories.base.SubscriberRepository;
import com.paymentsystem.ngpuppies.services.SubscriberServiceImpl;
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
public class SubscriberServiceImplTest {

    @Mock
    private SubscriberRepository subscribersRepository;

    @InjectMocks
    private SubscriberServiceImpl subscriberService;

    private List<Subscriber> mockedSubscribers;

    private Subscriber mockSubscriber;

    @Before
    public void beforeTest() {
        mockSubscriber = new Subscriber("John", "Mcdonald", "878998778", "9708741020", new Address(), (double) 0);

        mockedSubscribers = new ArrayList<>();
        mockedSubscribers.add(mockSubscriber);
    }

    @Test
    public void getALlSubscribers_returnListOfSubscribers() {
        // Arrange
        when(subscribersRepository.getAll()).thenReturn(mockedSubscribers);
        // Act

        List<Subscriber> actualRestaurants = subscriberService.getAll();

        // Assert
        Assert.assertEquals(actualRestaurants.size(), mockedSubscribers.size());
    }

    @Test
    public void getAllSubscribers_whenNoSubscribers_ReturnEmptyList() {
        when(subscribersRepository.getAll()).thenReturn(new ArrayList<>());

        List<Subscriber> subscribers = subscriberService.getAll();

        Assert.assertEquals(subscribers.size(), 0);
    }
}

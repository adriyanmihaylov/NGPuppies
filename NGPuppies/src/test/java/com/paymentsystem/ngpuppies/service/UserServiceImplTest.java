package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.User;
import com.paymentsystem.ngpuppies.repositories.base.UserRepository;
import com.paymentsystem.ngpuppies.services.UserServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final Integer USER_ID_ONE = 1;
    private static final String USER_USERNAME = "username";
    private static final String INVALID_USERNAME = "invalid";
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User userMock;

    @Before
    public void init() {
        userMock = new User();
        userMock.setId(USER_ID_ONE);
        userMock.setPassword("123456");
        userMock.setUsername(USER_USERNAME);
        userMock.setAuthority(new Authority(AuthorityName.ROLE_ADMIN));
    }

    @Test
    public void getUserById_shouldReturnUser() {
        Mockito.when(userRepository.loadById(USER_ID_ONE)).thenReturn(userMock);

        User user = userService.loadById(USER_ID_ONE);

        Assert.assertNotNull(user);
        Mockito.verify(userRepository,Mockito.times(1)).loadById(Mockito.anyInt());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void getUserByUsername_shouldThrowException() {
        Mockito.when(userRepository.loadByUsername(INVALID_USERNAME)).thenReturn(null);

        UserDetails user = userService.loadUserByUsername(INVALID_USERNAME);

        Assert.assertNull(user);
        Mockito.verify(userRepository, Mockito.times(1)).loadByUsername(Mockito.anyString());
        Mockito.verifyNoMoreInteractions(userRepository);
    }
}

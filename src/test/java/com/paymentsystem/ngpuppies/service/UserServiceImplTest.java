package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.IpAddress;
import com.paymentsystem.ngpuppies.models.users.*;
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

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final Integer INVALID_USER_ID = 2;
    private static final Integer VALID_USER_ID = 1;
    private static final String VALID_USER_USERNAME = "username";
    private static final String INVALID_USERNAME = "invalid";
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User userMock;
    private Admin adminMock;
    private Authority adminAuthority;
    private Authority clientAuthority;
    private Client clientMock;
    private List<User> userList;

    @Before
    public void init() {
        adminAuthority = new Authority(AuthorityName.ROLE_ADMIN);
        clientAuthority = new Authority(AuthorityName.ROLE_CLIENT);
        adminMock = new Admin("admin", "123456", "admin@admin.com", adminAuthority);
        clientMock = new Client("client", "123456", "123412341", clientAuthority);

        userMock = new User();
        userMock.setId(VALID_USER_ID);
        userMock.setPassword("123456");
        userMock.setUsername(VALID_USER_USERNAME);

        userList = new ArrayList<>();
        userList.add(userMock);
        userList.add(adminMock);
        userList.add(clientMock);
    }

    @Test
    public void loadById_whenIdIsPresent_returnUser() {

        when(userRepository.loadById(VALID_USER_ID)).thenReturn(userMock);

        User user = userService.loadById(VALID_USER_ID);

        assertNotNull(user);
        verify(userRepository, Mockito.times(1)).loadById(Mockito.anyInt());
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void loadById_whenIdIsNotPresent_returnNull() {
        when(userRepository.loadById(INVALID_USER_ID)).thenReturn(null);

        User user = userService.loadById(INVALID_USER_ID);

        assertNull(user);
    }

    @Test
    public void getUserByUsername_whenUsernamePresent_returnUser() {
        when(userRepository.loadByUsername(VALID_USER_USERNAME)).thenReturn(userMock);

        User user = (User) userService.loadUserByUsername(VALID_USER_USERNAME);

        Assert.assertEquals(user, userMock);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void getUserByUsername_whenUsernameNotPresent_shouldThrowException() {
        when(userRepository.loadByUsername(INVALID_USERNAME)).thenReturn(null);

        UserDetails user = userService.loadUserByUsername(INVALID_USERNAME);

        assertNull(user);
    }

    @Test
    public void getAll_shouldReturnNotEmptyListOfUsers() {
        when(userRepository.getAll()).thenReturn(userList);

        List<User> users = userService.getAll();

        Assert.assertEquals(users.size(), userList.size());
    }

    @Test
    public void getAll_whenNoUsers_shouldReturnEmptyList() {
        when(userRepository.getAll()).thenReturn(new ArrayList<>());

        List<User> users = userService.getAll();

        Assert.assertEquals(users.size(), 0);
    }

    @Test
    public void delete_whenUserExists_shouldReturnTrue() {
        when(userRepository.delete(userMock.getUsername())).thenReturn(true);

        boolean result = userService.deleteByUsername(userMock.getUsername());

        Assert.assertTrue(result);
    }

    @Test(expected = InvalidParameterException.class)
    public void delete_whenUsernameIsNull_shouldThrowException() {
        userService.deleteByUsername(null);
    }

    @Test
    public void addIpAddress_whenUserIsNull_shouldReturnFalse() {
        boolean result = userService.addIpAddress(null, "address");

        Assert.assertFalse(result);
    }

    @Test
    public void addIpAddress_onSuccess_shouldReturnTrue() {
        when(userRepository.addIpAddressToUser(any(User.class), any(IpAddress.class))).thenReturn(true);

        boolean result = userService.addIpAddress(new User(), "address");

        Assert.assertTrue(result);
    }
}
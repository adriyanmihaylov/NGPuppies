package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.repositories.base.AuthorityRepository;
import com.paymentsystem.ngpuppies.services.AuthorityServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthorityServiceImplTest {
    @Mock
    private AuthorityRepository authorityRepository;

    @InjectMocks
    private AuthorityServiceImpl authorityService;

    private Authority authority;

    @Before
    public void beforeTest() {
        authority = new Authority(AuthorityName.ROLE_CLIENT);
        authority.setId(1);
    }

    @Test
    public void getById_onSuccess_shouldReturnAuthority() {
        when(authorityRepository.getById(authority.getId())).thenReturn(authority);

        Authority result = authorityService.getById(authority.getId());

        Assert.assertEquals(authority, result);
    }

    @Test
    public void getByName_whenNameIsNull_shouldReturnNull() {
        Authority result = authorityService.getByName(null);

        Assert.assertNull(result);
    }

    @Test
    public void getByName_onSuccess_shouldReturnAuthority() {
        when(authorityRepository.getByName(authority.getName())).thenReturn(authority);

        Authority result = authorityService.getByName(authority.getName());

        Assert.assertNotNull(result);
    }
}

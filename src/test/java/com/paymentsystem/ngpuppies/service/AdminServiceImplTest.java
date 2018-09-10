package com.paymentsystem.ngpuppies.service;

import com.paymentsystem.ngpuppies.models.users.Admin;
import com.paymentsystem.ngpuppies.models.users.Authority;
import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.repositories.base.AdminRepository;
import com.paymentsystem.ngpuppies.repositories.base.AuthorityRepository;
import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.web.dto.AdminDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    private final String ADMIN_EMAIL = "admin@admin.com";
    private Admin admin;
    private Authority authority;
    private List<Admin> adminList;

    @Before
    public void beforeTest() {
        when(passwordEncoder.encode(any())).thenReturn("password");

        authority = new Authority(AuthorityName.ROLE_ADMIN);
        admin = new Admin("username", "123456", ADMIN_EMAIL, authority);
        adminList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            adminList.add(new Admin());
        }
    }

    @Test
    public void getAll_onSuccess_shouldReturnListOfAdmin() {
        when(adminRepository.getAll()).thenReturn(adminList);

        List<Admin> resultList = adminService.getAll();

        Assert.assertEquals(adminList.size(),resultList.size());
    }

    @Test
    public void loadByEmail_whenEmailIsNull_shouldReturnNull() {
        Admin result = adminService.loadByEmail(null);

        Assert.assertNull(result);
    }

    @Test
    public void loadByEmail_onSuccess_shouldReturnAdmin() {
        when(adminRepository.loadByEmail(ADMIN_EMAIL)).thenReturn(admin);

        Admin result = adminService.loadByEmail(ADMIN_EMAIL);

        Assert.assertEquals(admin, result);
    }

    @Test
    public void loadByUsername_whenUsernameIsNull_shouldReturnNull() {
        Admin result = adminService.loadByUsername(null);

        Assert.assertNull(result);
    }

    @Test
    public void loadByUsername_onSuccess_shouldReturnAdmin() {
        when(adminRepository.loadByUsername("username")).thenReturn(admin);

        Admin result = adminService.loadByUsername("username");

        Assert.assertNotNull(result);
    }

    @Test(expected = InvalidParameterException.class)
    public void create_whenAdminDtoIsNull_shouldThrowException() throws Exception {
        adminService.create(null);
    }

    @Test(expected = InvalidParameterException.class)
    public void create_whenPasswordIsNull_shouldThrowException() throws Exception {
        adminService.create(new AdminDto());
    }

    @Test(expected = Exception.class)
    public void create_whenAuthorityIsNull_shouldThrowException() throws Exception {
        AdminDto adminDto = new AdminDto("admin","123456",ADMIN_EMAIL);
        when(authorityRepository.getByName(any(AuthorityName.class))).thenReturn(null);

        adminService.create(adminDto);
    }

    @Test
    public void create_onSuccess_shouldReturnTrue() throws Exception {
        AdminDto adminDto = new AdminDto("admin", "123456", ADMIN_EMAIL);
        when(authorityRepository.getByName(any(AuthorityName.class))).thenReturn(authority);
        when(adminRepository.create(any(Admin.class))).thenReturn(true);

        boolean result = adminService.create(adminDto);

        Assert.assertTrue(result);
    }

    @Test(expected = InvalidParameterException.class)
    public void update_whenUsernameIsNull_shouldThrowException() throws SQLException {
        adminService.update(null,new AdminDto());
    }

    @Test(expected = InvalidParameterException.class)
    public void update_whenAdminDtoIsNull_shouldThrowException() throws SQLException {
        adminService.update("username",null);
    }

    @Test(expected = InvalidParameterException.class)
    public void update_whenAdminIsNotFoundByUsername_shouldThrowException() throws SQLException {
        when(adminRepository.loadByUsername("username")).thenReturn(null);

        adminService.update("username",new AdminDto());
    }

    @Test
    public void update_onSuccess_shouldReturnTrue() throws SQLException {
        when(adminRepository.loadByUsername("username")).thenReturn(admin);
        when(adminRepository.update(any(Admin.class))).thenReturn(true);

        boolean result = adminService.update("username",new AdminDto("name","123456","admin@admin.com"));

        Assert.assertTrue(result);
    }

    @Test
    public void updateOnFirstLogin_whenAdminIsNull_shouldReturnFalse() throws Exception {
        boolean result = adminService.updateOnFirstLogin(null);

        Assert.assertFalse(result);
    }

    @Test(expected = Exception.class)
    public void updateOnFirstLogin_whenAuthorityIsNotFound_shouldThrowException() throws Exception {
        when(authorityRepository.getByName(any(AuthorityName.class))).thenReturn(null);
        adminService.updateOnFirstLogin(new Admin());
    }

    @Test
    public void updateOnFirstLogin_onSuccess_shouldReturnTrue() throws Exception {
        Authority authority = new Authority(AuthorityName.ROLE_INITIAL);
        Admin currentAdmin = new Admin("username","654321","user@user.com",authority);
        when(authorityRepository.getByName(any(AuthorityName.class))).thenReturn(new Authority(AuthorityName.ROLE_ADMIN));
        when(adminRepository.update(currentAdmin)).thenReturn(true);

        boolean result = adminService.updateOnFirstLogin(currentAdmin);

        Assert.assertTrue(result);
        Assert.assertEquals(AuthorityName.ROLE_ADMIN,currentAdmin.getAuthority().getName());
        Assert.assertTrue(currentAdmin.getEnabled());
    }




}
//    public boolean updateOnFirstLogin(Admin admin) throws SQLException {
//        if(admin == null) {
//            return false;
//        }
//
//        Authority authority = authorityRepository.getByName(AuthorityName.ROLE_ADMIN);
//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
//        admin.setAuthority(authority);
//        admin.setLastPasswordResetDate(new Date());
//        admin.setEnabled(Boolean.TRUE);
//
//        return adminRepository.update(admin);
//    }

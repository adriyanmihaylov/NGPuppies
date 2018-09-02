package com.paymentsystem.ngpuppies.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paymentsystem.ngpuppies.models.users.*;
import com.paymentsystem.ngpuppies.security.*;
import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
import com.paymentsystem.ngpuppies.services.AppUserServiceImpl;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import com.paymentsystem.ngpuppies.services.base.AdminService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationRestControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private ClientServiceImpl clientService;

    @MockBean
    private AdminServiceImpl adminService;

    @MockBean
    private AppUserServiceImpl appUserService;

    @MockBean
    private Client client;

    @MockBean
    private Admin admin;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    @WithAnonymousUser
    public void successfulAuthenticationWithAnonymousUser() throws Exception {

        JwtAuthenticationRequest jwtAuthenticationRequest = new JwtAuthenticationRequest("admin", "123456");

        mvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(jwtAuthenticationRequest)))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    public void successfulRefreshTokenWithClientRole() throws Exception {

        Authority authority = new Authority();
        authority.setId(0);
        authority.setName(AuthorityName.ROLE_CLIENT);

        Client client = new Client();
        client.setId(10);
        client.setUsername("username");
        client.setAuthority(authority);
        client.setEnabled(Boolean.TRUE);
        client.setEik("123456789");
        client.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));

        clientService.create(client);

        when(jwtTokenUtil.getIdFromToken(ArgumentMatchers.any())).thenReturn(client.getId());

        when(clientService.loadByUsername(ArgumentMatchers.eq(client.getUsername()))).thenReturn(client);

        when(jwtTokenUtil.canTokenBeRefreshed(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void successfulRefreshTokenWithAdminRole() throws Exception {

        Authority authority = new Authority();
        authority.setId(0);
        authority.setName(AuthorityName.ROLE_ADMIN);

        Admin admin = new Admin();
        admin.setId(10);
        admin.setUsername("admins");
        admin.setAuthority(authority);
        admin.setEnabled(Boolean.TRUE);
        admin.setLastPasswordResetDate(new Date(System.currentTimeMillis() + 1000 * 1000));


        when(jwtTokenUtil.getIdFromToken(ArgumentMatchers.any())).thenReturn(admin.getId());

        when(adminService.loadByUsername(ArgumentMatchers.eq(admin.getUsername()))).thenReturn(admin);

        when(jwtTokenUtil.canTokenBeRefreshed(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(true);
    }

    @Test
    @WithAnonymousUser
    public void shouldGetUnauthorizedWithAnonymousUser() throws Exception {

        mvc.perform(get("/refresh"))
            .andExpect(status().isUnauthorized());
    }
}


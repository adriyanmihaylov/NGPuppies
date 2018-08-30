package com.paymentsystem.ngpuppies.web.restControllers;

import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import com.paymentsystem.ngpuppies.services.base.ApplicationUserService;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ClientRestController {

    private ApplicationUserService applicationUserService;
    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    public ClientRestController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }


    @PostMapping("/client/register")
    public boolean registerClient(@RequestBody Client client) {

        if (applicationUserService.checkIfUsernameIsPresent(client.getUsername())) {
            return false;
        }
        if (clientService.checkIfEikIsPresent(client.getEik())) {
            return false;
        }

        //TODO encrypt password using BCrypt
        // client.setPassword(client.getPassword());

        return clientService.create(client);
    }
}

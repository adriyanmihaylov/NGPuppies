package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.base.ClientService;
import com.paymentsystem.ngpuppies.viewModels.ClientViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${common.basepath}/clients")
public class ClientRestController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/get")
    public ClientViewModel getByUsername(@RequestParam(name = "username") String username) {
        return ClientViewModel.fromModel(clientService.getByUsername(username));
    }

    @PostMapping("/register")
    public boolean registerClient(@RequestBody Client client) {

        Client client1 = new Client();
        client1.setEik(client.getEik());
        client1.setUsername(client.getUsername());
        client1.setPassword(client.getPassword());
        //TODO encrypt password using BCrypt

        return clientService.create(client1);
    }
}
package com.paymentsystem.ngpuppies.web;

import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.services.ClientServiceImpl;
import com.paymentsystem.ngpuppies.services.base.ApplicationUserService;
import com.paymentsystem.ngpuppies.viewModels.ClientViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${common.basepath}/clients")
public class ClientRestController {

    private ApplicationUserService applicationUserService;
    @Autowired
    private ClientServiceImpl clientService;

    @Autowired
    public ClientRestController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/all")

    public List<ClientViewModel> getAll(){
        return clientService.getAll().stream()
                .map(ClientViewModel::fromModel)
                .collect(Collectors.toList());
    }
    @GetMapping("/get")
    public ClientViewModel getByUsername(@RequestParam(name = "username") String username){
        return ClientViewModel.fromModel(clientService.getByUsername(username));
    }

    @PostMapping("/register")
    public boolean registerClient(@RequestBody Client client) {

        if (applicationUserService.checkIfUsernameIsPresent(client.getUsername())) {
            return false;
        }
        if (clientService.checkIfEikIsPresent(client.getEik())) {
            return false;
        }
        Client client1 = new Client();
        client1.setEik(client.getEik());
        client1.setUsername(client.getUsername());
        client1.setPassword(client.getPassword());
        //TODO encrypt password using BCrypt

        return clientService.create(client1);
    }
}

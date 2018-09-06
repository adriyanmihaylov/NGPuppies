package com.paymentsystem.ngpuppies.models.viewModels;

import com.paymentsystem.ngpuppies.models.users.AuthorityName;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.models.ClientDetail;

public class ClientViewModel {

    public String username;

    public String eik;

    public AuthorityName authority;

    public ClientDetail details;

    public static ClientViewModel fromModel(Client client) {
        ClientViewModel vm = new ClientViewModel();

        if (client != null) {
            vm.username = client.getUsername();
            vm.eik = client.getEik();
            vm.authority = client.getAuthority().getName();
            vm.details = client.getDetails();
        }

        return vm;
    }
}
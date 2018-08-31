package com.paymentsystem.ngpuppies.viewModels;

import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.models.ClientDetail;

public class ClientViewModel {
    public UserViewModel credentials;

    public String eik;

    public ClientDetail detail;

    public static ClientViewModel fromModel(Client client) {
        ClientViewModel vm = new ClientViewModel();

        if (client != null) {
            vm.credentials = new UserViewModel().fromModel(client);
            vm.eik = client.getEik();
            vm.detail = client.getDetails();
        }

        return vm;
    }
}
package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends ApplicationUser {

    @Column(name = "ClientEik")
    private String eik;

    @OneToOne
    @JoinColumn(name = "ClientDetailsID")
    private ClientDetail details;

    public Client() {

    }

    public Client(String username, String password, String role, String eik) {
        super(username,password,role);
        setEik(eik);
    }

    public Client(String username, String password, String role, String eik, ClientDetail details) {
        this(username,password,role,eik);
        setDetails(details);
    }

    public String getEik() {
        return eik;
    }

    public void setEik(String eik) {
        this.eik = eik;
    }

    public ClientDetail getDetails() {
        return details;
    }

    public void setDetails(ClientDetail details) {
        this.details = details;
    }
}

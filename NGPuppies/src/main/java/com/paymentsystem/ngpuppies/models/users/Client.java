package com.paymentsystem.ngpuppies.models.users;

import com.paymentsystem.ngpuppies.models.ClientDetail;
import com.paymentsystem.ngpuppies.validation.anotations.ValidEik;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends User {

    @ValidEik
    @Column(name = "ClientEik")
    private String eik;

    @OneToOne
    @JoinColumn(name = "ClientDetailsID")
    private ClientDetail details;

    public Client() {
    }
    public Client(String username, String password, String eik,Authority authority) {
        super(username,password,authority);
        setEik(eik);
        setEnabled(Boolean.TRUE);
        setLastPasswordResetDate(new Date());

    }

    public Client(String username, String password, String eik,Authority authority,ClientDetail clientDetail) {
        this(username, password, eik, authority);
        setDetails(clientDetail);
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

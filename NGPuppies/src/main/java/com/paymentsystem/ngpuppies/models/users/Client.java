package com.paymentsystem.ngpuppies.models.users;

import com.paymentsystem.ngpuppies.models.ClientDetail;

import javax.persistence.*;
import java.util.Date;

@Entity
@DiscriminatorValue("CLIENT")
public class Client extends AppUser {

    @Column(name = "ClientEik")
    private String eik;

    @OneToOne
    @JoinColumn(name = "ClientDetailsID")
    private ClientDetail details;

    public Client() {
        Date today = new Date();
        super.setEnabled(false);
        super.setLastPasswordResetDate(today);
        super.setAuthority(new Authority(AuthorityName.ROLE_CLIENT));
    }

    public Client(String username, String password, String eik) {
        super(username,password,AuthorityName.ROLE_CLIENT);
        setEik(eik);

    }

    public Client(String username, String password,String eik, ClientDetail details) {
        this(username,password,eik);
        setDetails(details);
        Date today = new Date();
        super.setEnabled(false);
        super.setLastPasswordResetDate(today);
        setEik(eik);
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

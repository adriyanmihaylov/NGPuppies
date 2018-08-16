package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "EIK")
    private String eik;

    @OneToOne
    @JoinColumn(name = "DetailsID")
    private ClientDetail detail;

    public Client() {

    }

    public Client(String username, String password, String eik, ClientDetail detail) {
        setUsername(username);
        setPassword(password);
        setEik(eik);
        setDetail(detail);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEik() {
        return eik;
    }

    public void setEik(String eik) {
        this.eik = eik;
    }

    public ClientDetail getDetail() {
        return detail;
    }

    public void setDetail(ClientDetail detail) {
        this.detail = detail;
    }
}

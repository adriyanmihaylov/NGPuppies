package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name="subscribers")
public class Subscriber {
    @Id
    @Column(name = "PhoneNumber")
    private String id;
    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "EGN")
    private String egn;
    @Column(name = "Address")
    private String address;
    @ManyToOne
    @JoinColumn(name = "BankID")
    private Bank bank;

    public Subscriber(){

    }
    public Subscriber(String id, String firstName, String lastName, String egn, String address, Bank bank) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.egn = egn;
        this.address = address;
        this.bank = bank;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

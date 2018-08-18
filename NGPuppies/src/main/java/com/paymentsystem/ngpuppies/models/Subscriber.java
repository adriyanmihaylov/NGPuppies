package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name="subscribers")
public class Subscriber {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "EGN")
    private String egn;

    @OneToOne
    @JoinColumn(name = "AddressID")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "ClientID")
    private ApplicationUser applicationUser;

    public Subscriber() {

    }

    public Subscriber(String phoneNumber, String firstName, String lastName, String egn, Address address, ApplicationUser applicationUser) {
        setPhoneNumber(phoneNumber);
        setFirstName(firstName);
        setLastName(lastName);
        setEgn(egn);
        setAddress(address);
        setApplicationUser(applicationUser);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }
}
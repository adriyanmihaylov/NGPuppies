package com.paymentsystem.ngpuppies.models;

import com.paymentsystem.ngpuppies.models.users.Client;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name="subscribers")
public class Subscriber {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "PhoneNumber")
    private String phoneNumber;

    @NotNull
    @Column(name = "FirstName")
    private String firstName;

    @NotNull
    @Column(name = "LastName")
    private String lastName;

    @NotNull
    @Column(name = "EGN")
    private String egn;

    @OneToOne
    @JoinColumn(name = "AddressID")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "ClientID")
    private Client client;

    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<BillingRecord> billingRecords;

    public Subscriber(){

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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, EGN: %s, PhoneNumber: %s", getFirstName(), getEgn(), getPhoneNumber());
    }

    public List<BillingRecord> getBillingRecords() {
        return billingRecords;
    }

    public void setBillingRecords(List<BillingRecord> billingRecords) {
        this.billingRecords = billingRecords;
    }
}

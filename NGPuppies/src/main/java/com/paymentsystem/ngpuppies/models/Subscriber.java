package com.paymentsystem.ngpuppies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.validator.base.ValidEgn;
import com.paymentsystem.ngpuppies.validator.base.ValidName;
import com.paymentsystem.ngpuppies.validator.base.ValidPhone;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name="subscribers")
public class Subscriber {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ValidPhone
    @Column(name = "PhoneNumber")
    private String phone;

    @ValidName
    @Size.List({
            @Size(min = 3, message = "First name must be at least 3 characters"),
            @Size(max = 50, message = "First name must be less than 50 characters")
    })
    @Column(name = "FirstName")
    private String firstName;

    @ValidName
    @Size.List({
            @Size(min = 3, message = "Last name must be at least 3 characters"),
            @Size(max = 50, message = "Last name must be less than 50 characters")
    })
    @Column(name = "LastName")
    private String lastName;

    @ValidEgn
    @Column(name = "EGN")
    private String egn;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AddressID")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "ClientID")
    private Client client;

    @JsonIgnore
    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Invoice> invoices;

    public Subscriber(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phoneNumber) {
        this.phone = phoneNumber;
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
        return String.format("Name: %s, EGN: %s, PhoneNumber: %s", getFirstName(), getEgn(), getPhone());
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}

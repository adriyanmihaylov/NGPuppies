package com.paymentsystem.ngpuppies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paymentsystem.ngpuppies.models.users.Client;
import com.paymentsystem.ngpuppies.validation.anotations.ValidEgn;
import com.paymentsystem.ngpuppies.validation.anotations.ValidName;
import com.paymentsystem.ngpuppies.validation.anotations.ValidPhone;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            @Size(max = 20, message = "First name must be less than 20 characters")
    })
    @Column(name = "FirstName")
    private String firstName;

    @ValidName
    @Size.List({
            @Size(min = 3, message = "Last name must be at least 3 characters"),
            @Size(max = 20, message = "Last name must be less than 20 characters")
    })
    @Column(name = "LastName")
    private String lastName;

    @ValidEgn
    @Column(name = "EGN")
    private String egn;


    @Column(name = "TotalAmountPaid")
    private Double totalAmount;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AddressID")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "ClientID")
    private Client client;

    @JsonIgnore
    @OneToMany(mappedBy = "subscriber", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Invoice> invoices;

    @Fetch(FetchMode.SELECT)
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "subscriber_services",
            joinColumns = {@JoinColumn(name = "SubscriberID")},
            inverseJoinColumns = {@JoinColumn(name = "OfferedServiceID")}
    )
    private Set<TelecomServ> subscriberServices;


    public Subscriber() {
    }

    public Subscriber(String firstName, String lastName, String phoneNumber, String egn, Address address, Double totalAmount) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhone(phoneNumber);
        setEgn(egn);
        setAddress(address);
        setTotalAmount(totalAmount);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Set<TelecomServ> getSubscriberServices() {
        return subscriberServices;
    }

    public void setSubscriberServices(Set<TelecomServ> subscriberServices) {
        this.subscriberServices = subscriberServices;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void addSubscriberServices(TelecomServ service) {
        if (subscriberServices == null) {
            subscriberServices = new HashSet<>();
        }

        subscriberServices.add(service);
    }
}

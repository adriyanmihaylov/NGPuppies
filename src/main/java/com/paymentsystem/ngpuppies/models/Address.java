package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "City")
    private String city;

    @Column(name = "Street")
    private String street;

    @Column(name = "State")
    private String state;

    @Column(name = "PostCode")
    private String postCode;

    @Column(name = "Country")
    private String country;

    public Address() {

    }

    public Address(String city, String street, String state, String postCode, String country) {
        setCity(city);
        setStreet(street);
        setState(state);
        setPostCode(postCode);
        setCountry(country);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format("City: %s, Street: %s , State: %s , Postcode: %s , Country: %s  ",
                getCity(), getStreet(), getState(), getPostCode(), getCountry());
    }
}
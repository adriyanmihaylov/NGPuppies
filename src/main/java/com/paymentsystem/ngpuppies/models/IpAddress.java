package com.paymentsystem.ngpuppies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.paymentsystem.ngpuppies.models.users.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ip_addresses")
public class IpAddress {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Address")
    private String address;


    public IpAddress() {

    }
    public IpAddress(String address) {
        setAddress(address);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof IpAddress))
            return false;
        IpAddress ipAddress = (IpAddress) obj;
        return this.getAddress().equals(ipAddress.getAddress());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + address.hashCode();
        return result;
    }
}

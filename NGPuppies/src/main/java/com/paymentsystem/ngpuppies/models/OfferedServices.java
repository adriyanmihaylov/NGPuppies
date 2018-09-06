package com.paymentsystem.ngpuppies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "offered_services")
public class OfferedServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @NotNull(message = "Please enter service name!")
    @Size.List({
            @Size(min = 2, message = "Service name must be at least 3 characters"),
            @Size(max = 50, message = "Service name must be less than 50 characters")
    })
    @Column(name = "Name")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER,mappedBy = "subscriberServices")
    private Set<Subscriber> subscribers;

    public OfferedServices() {

    }

    public OfferedServices(String name) {
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Set<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof OfferedServices))
            return false;
        OfferedServices otherService = (OfferedServices) obj;
        return this.getName().equals(otherService.getName());
    }

    @Override
    public int hashCode() {
        return this.id;
    }

}
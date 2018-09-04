package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "offered_services")
public class OfferedServices {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @Column(name = "Name")
    private String name;

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
}
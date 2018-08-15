package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "services")
public class OfferedService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @Column(name = "Type")
    private String type;

    public OfferedService() {

    }

    public OfferedService(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
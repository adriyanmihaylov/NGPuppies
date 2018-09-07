package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "clients_details")
public class ClientDetail {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Description")
    private String description;

    public ClientDetail() {

    }

    public ClientDetail(String description) {
        setDescription(description);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
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

    @Column(name = "FriendlyName")
    private String friendlyName;

    public ClientDetail() {

    }

    public ClientDetail(String description, String friendlyName) {
        setDescription(description);
        setFriendlyName(friendlyName);
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

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }
}
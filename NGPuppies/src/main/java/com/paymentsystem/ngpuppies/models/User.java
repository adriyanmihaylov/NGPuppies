package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int id;

    @Column(name = "Username")
    private String userName;

    @Column(name = "Password")
    private String password;

    @OneToOne
    @JoinColumn(name = "RoleID")
    private Role role;

    @Column(name = "EIK")
    private String eik;

    @Column(name = "Details")
    private String details;

    public User() {

    }

    public User(String userName, String password,
                Role role, String eik,
                String details) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.eik = eik;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEik() {
        return eik;
    }

    public void setEik(String eik) {
        this.eik = eik;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
package com.paymentsystem.ngpuppies.models;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private int Id;
    @Column(name = "Type")
    private String type;

    public Role(){

    }
    public Role(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return Id;
    }
}

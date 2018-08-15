package com.paymentsystem.ngpuppies.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="subscribers")
public class Subscriber {
    @Id
    private String id;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

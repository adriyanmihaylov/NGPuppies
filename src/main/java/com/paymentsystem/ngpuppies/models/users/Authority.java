package com.paymentsystem.ngpuppies.models.users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(name = "Id")
    private int id;

    @NotNull
    @Column(name = "Name")
    @Enumerated(EnumType.STRING)
    private AuthorityName name;

    public Authority() {

    }

    public Authority(AuthorityName name) {
        setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AuthorityName getName() {
        return name;
    }

    public void setName(AuthorityName name) {
        this.name = name;
    }

}

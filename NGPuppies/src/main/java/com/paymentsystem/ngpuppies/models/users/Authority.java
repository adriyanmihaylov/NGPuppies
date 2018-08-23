package com.paymentsystem.ngpuppies.models.users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "Name", length = 50)
    @NotNull
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

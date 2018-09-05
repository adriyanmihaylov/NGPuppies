package com.paymentsystem.ngpuppies.models.dto;

import javax.validation.constraints.Size;

public class OfferedServiceDTO {
    @Size.List({
            @Size(min = 3, message = "Service name must be at least 3 characters"),
            @Size(max = 50, message = "Service name must be less than 50 characters")
    })
    private String name;

    public OfferedServiceDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

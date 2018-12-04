package com.ftms.ftmsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@DiscriminatorValue( value="client" )
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ClientUser extends User {
    public ClientUser () {
        super();
    }

    public ClientUser(String fname, String lname, String email, String number, String role, Company company){
        super(fname, lname, email, number, role, company);
    }
}

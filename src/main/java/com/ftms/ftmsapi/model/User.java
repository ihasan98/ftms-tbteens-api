package com.ftms.ftmsapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.NaturalId;
import java.io.Serializable;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn( discriminatorType = DiscriminatorType.STRING,
        name = "user_type",
        columnDefinition = "VARCHAR(20)")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @NaturalId
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String number;

    private boolean active;

    @JsonIgnore
    private String password;

    @NotBlank
    private String role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public User () {
        this.active = false;
    }

    public User(String fname, String lname, String email, String number,
                String role){
        this.firstname = fname;
        this.lastname = lname;
        this.email = email;
        this.number = number;
        this.role = role;
        this.active = false;
        this.company = null;
    }

    public User(String fname, String lname, String email, String number,
                String role, Company company){
        this.firstname = fname;
        this.lastname = lname;
        this.email = email;
        this.number = number;
        this.role = role;
        this.active = false;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public Company getCompany(){
        return company;
    }

    public void setCompany(Company company){
        this.company = company;
    }

    public boolean getActive(){
        return active;
    }

    public void setActive(boolean active){
        this.active = active;
    }

}

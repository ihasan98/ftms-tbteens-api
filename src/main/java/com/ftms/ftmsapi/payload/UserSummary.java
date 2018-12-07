package com.ftms.ftmsapi.payload;

import com.ftms.ftmsapi.model.Company;

public class UserSummary {
    private Long id;
    private String email;
    private String fname;
    private String number;
    private String lname;
    private String role;
    private Long company;

    public UserSummary(Long id, String email, String fname, String lname,
                       String role, String number, Long company) {
        this.id = id;
        this.email = email;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
        this.number = number;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return fname;
    }

    public void setFirstname(String fname) {
        this.fname = fname;
    }

    public String getLastname() {
        return lname;
    }

    public void setLastname(String lname) {
        this.lname = lname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNumber() { return number; }

    public void setNumber(String number) { this.number = number; }

    public Long getCompany() {
        return company;
    }

    public void setCompany(Long company) {
        this.company = company;
    }
}

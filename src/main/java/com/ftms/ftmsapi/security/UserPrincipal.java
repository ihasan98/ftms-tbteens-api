package com.ftms.ftmsapi.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ftms.ftmsapi.model.Company;
import com.ftms.ftmsapi.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserPrincipal implements UserDetails {
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String role;

    @JsonIgnore
    private String number;

    @JsonIgnore
    private String password;

    private  boolean isActive;

    private Company company;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String firstname, String lastname,
                         String email, String number, String password, String role,
                         boolean isActive, Company company,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.number = number;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
        this.isActive = isActive;
        this.company = company;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>
                (Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));

        return new UserPrincipal(
                user.getID(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getNumber(),
                user.getPassword(),
                user.getRole(),
                user.getActive(),
                user.getCompany(),
                authorities
        );
    }

    public boolean getIsActive() {
        return isActive;
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getRole() {
        return role;
    }

    public Company getCompany() { return company;}

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}

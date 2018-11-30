package com.ftms.ftmsapi.model;

import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name="jobs")
public class Job implements Serializable{
    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String jobTitle;

    @NotBlank
    private String description;
    private String siteName;
    
    private Company company;

    

    // GETTERS/SETTERS
    public String getJobTitle() {
        return this.jobTitle;
    }

    public void setJobTitle(String title) {
        this.jobTitle = title;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}

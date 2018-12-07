package com.ftms.ftmsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name="jobs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    public Job() {

    }

    public Job(String jobTitle, String description, String siteName,
               Company company) {
        this.jobTitle = jobTitle;
        this.description = description;
        this.siteName = siteName;
        this.company = company;
    }

    // GETTERS/SETTERS

    /**
     * Getter for job title.
     *
     * @return Job title.
     */
    public String getJobTitle() {
        return this.jobTitle;
    }

    /**
     * Setter for job title.
     *
     * @param title The title to be changed.
     */
    public void setJobTitle(String title) {
        this.jobTitle = title;
    }

    /**
     * Getter for id
     *
     * @return The id.
     */
    public Long getID() {
        return id;
    }

    /**
     * Getter for description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     *
     * @param description The description to be changed.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for company.
     *
     * @return The company.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Company getCompany() {
        return company;
    }

    /**
     * Setter for company.
     *
     * @param company The company to be set.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public void setCompany(Company company) {
        this.company = company;
    }

    /**
     * Getter for site name.
     *
     * @return The site name.
     */
    public String getSiteName() {
        return siteName;
    }

    /**
     * Setter for site name.
     *
     * @param siteName The site name to be changed.
     */
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}

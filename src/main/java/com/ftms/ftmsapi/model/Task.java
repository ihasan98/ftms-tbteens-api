package com.ftms.ftmsapi.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="task")
public class Task implements Serializable{
    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User employee;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    private String description;

    // GETTERS/SETTERS

    /**
     * Getter for ID
     *
     * @return The ID of this.
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter for employee.
     *
     * @return The id.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public User getEmployee() {
        return employee;
    }

    /**
     * Setter for employee.
     *
     * @param employee The employee to be changed.
     */
    public void setEmployee(User employee) {
        this.employee = employee;
    }

    /**
     * Getter for job.
     *
     * @return The job.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Job getJob() {
        return job;
    }

    /**
     * Setter for job.
     *
     * @param job The job to be changed.
     */
    public void setJob(Job job) {
        this.job = job;
    }

    /**
     * The getter for description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The setter for description.
     *
     * @param description The description to be changed.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

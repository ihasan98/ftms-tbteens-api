package com.ftms.ftmsapi.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

//For the timesheet creation in jobassignment do not touch
@Entity
public class Selection implements Serializable{
    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User employee;

    private Job job;

    /**
     * @return the job
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Job getJob() {
        return job;
    }

    /**
     * @return the employee
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public User getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public void setEmployee(User employee) {
        this.employee = employee;
    }

    /**
     * @param job the job to set
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public void setJob(Job job) {
        this.job = job;
    }
}
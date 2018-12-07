package com.ftms.ftmsapi.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


@Entity
@Table(name="timesheet")
public class Timesheet implements Serializable{
    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 0- Not reviewed, 1- Rejected, 2- Accepted
    private int approvalStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    public Long getId() {
        return id;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    public Employee getEmployee() {
        return employee;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    public Job getJob() {
        return job;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    public void setJob(Job job) {
        this.job = job;
    }

    // Returns the Employee ID of the employee associated with this timesheet.
    @JsonIgnore
    public Long getEmployeeId() {
        return employee.getId();
    }

    
    // Returns the Job ID of the job that is associated with this task.
    @JsonIgnore
    public Long getJobId() {
        return job.getId();
    }

    public int getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}

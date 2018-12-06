package com.ftms.ftmsapi.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="timesheet")
public class Timesheet implements Serializable{
    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String approvalStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    public Long getId() {
        return id;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Employee getEmployee() {
        return employee;
    }


    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Job getJob() {
        return job;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public void setJob(Job job) {
        this.job = job;
    }

    // Returns the Employee ID of the employee associated with this timesheet.
    public Long getEmployeeId() {
        return employee.getId();
    }

    // Returns the Job ID of the job that is associated with this task.
    public Long getJobId() {
        return job.getId();
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}

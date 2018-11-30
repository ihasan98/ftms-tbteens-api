package com.ftms.ftmsapi.controller;

import javax.validation.Valid;

import com.ftms.ftmsapi.model.Timesheet;
import com.ftms.ftmsapi.repository.TimesheetRepository;
import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TimesheetController {

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    JobRepository jobRepository;

    // Create a new employee
    @PostMapping("/save")
    public Timesheet createTimesheet(@Valid @RequestBody Timesheet timesheet) {
        System.out.println("jnadkfbgfbgsdn");
        return timesheetRepository.save(timesheet);
    }

    //Get all the employees
    @PostMapping("/get")
    public List<Timesheet> getTimesheet(){
        return timesheetRepository.findAll();
    }

    //Get jobs from timesheet id
    @RequestMapping("/get/job_by_timesheet_id")
    public List<Job> getJobsByTimesheetId(@Valid @RequestBody Long timesheet_id){
        return jobRepository.findJobsFromTimesheetId(timesheet_id);
    }

    //Get timesheet from job and employee id
    @RequestMapping("/get/timesheet_by_employee_and_job_id")
    public List<Timesheet> getTimesheetByEmployeeAndJobId(@Valid @RequestBody Long employee_id, Long job_id){
        return timesheetRepository.findTimesheetFromEmployeeIdAndJobId(employee_id, job_id);}

    // Approve the timesheet.
    @PostMapping("/approve")
    public void approve(Long timesheetId) {
        Timesheet ts = timesheetRepository.getOne(timesheetId);
        ts.setApprovalStatus("Approved");
        timesheetRepository.save(ts);
    }

    // Reject the timesheet.
    @PostMapping("/reject")
    public void reject(Long timesheetId) {
        Timesheet ts = timesheetRepository.getOne(timesheetId);
        ts.setApprovalStatus("Rejected");
        timesheetRepository.save(ts);
    }

}
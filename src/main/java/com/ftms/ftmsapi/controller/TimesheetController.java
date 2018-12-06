package com.ftms.ftmsapi.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.Employee;
import com.ftms.ftmsapi.model.Timesheet;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.EmployeeRepository;
import com.ftms.ftmsapi.repository.TimesheetRepository;
import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.repository.JobRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TimesheetController {

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    /**
     * Get all the timesheets in a list.
     *
     * @return List of all timesheets in the repository.
     */
    @PostMapping("/get")
    public List<Timesheet> getTimesheet(){
        return timesheetRepository.findAll();
    }

    /**
     * Get all of jobs in the timesheet with id timesheet_id in a list.
     *
     * @param timesheet_id The timesheet ID we want to check the timesheet.
     * @return The list of jobs in the timesheet.
     */
    @RequestMapping("/get/job_by_timesheet_id")
    public List<Job> getJobsByTimesheetId(@Valid @RequestBody Long timesheet_id){
        return jobRepository.findJobsFromTimesheetId(timesheet_id);
    }

    //Get timesheet from job and employee id
    @GetMapping("/timesheets/{employeeID}/{jobID}")
    public ResponseEntity getTimesheetByEmployeeAndJobId(@PathVariable Long employeeID, @PathVariable Long jobID) {
        //Making sure the jobID and employeeID are correct.
        try {
            Employee employee = employeeRepository.getOne(employeeID);
            Job job = jobRepository.getOne(jobID);
            List<Timesheet> foundTimesheets = timesheetRepository.findByJobAndEmployee(employee, job);
            return new ResponseEntity<Object>(foundTimesheets, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Object>(new ApiResponse(false,
                    "Employee/Job not found!"), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/timesheets/{jobID}")
    public List<Timesheet> getTimesheetByJobs(@PathVariable Long jobID){
        List<Timesheet> timesheets = timesheetRepository.findAll();
        List<Timesheet> jobTimesheet = new ArrayList<>();
        for (Timesheet timesheet: timesheets){
            if (timesheet.getJob().getId().equals(jobID))
                jobTimesheet.add(timesheet);
        }
        return jobTimesheet;
    }
    /**
     * Approves a timesheet.
     *
     * @param timesheetId The ID of the timesheet to be approved.
     */
    @PostMapping("/approve")
    public void approve(Long timesheetId) {
        Timesheet ts = timesheetRepository.getOne(timesheetId);
        ts.setApprovalStatus("Approved");
        timesheetRepository.save(ts);
    }

    /**
     * Reject a timesheet.
     *
     * @param timesheetId The ID of the timesheet to be rejected.
     */
    @PostMapping("/reject")
    public void reject(Long timesheetId) {
        Timesheet ts = timesheetRepository.getOne(timesheetId);
        ts.setApprovalStatus("Rejected");
        timesheetRepository.save(ts);
    }

}
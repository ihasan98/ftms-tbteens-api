package com.ftms.ftmsapi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Selection;
import com.ftms.ftmsapi.model.Timesheet;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.repository.JobRepository;
import com.ftms.ftmsapi.repository.TimesheetRepository;
import com.ftms.ftmsapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    TimesheetRepository timesheetRepository;
    @Autowired
    TimesheetController timesheetController;
    @Autowired
    UserRepository userRepository;
 
    @PostMapping("/jobs/employees")
    public List<User> retrieveEmployeeFromJobs(@Valid @RequestBody Job job) {
        System.out.println(job.getJobTitle());
        System.out.println(job);
        ArrayList<User> employees = new ArrayList<>();
        List<Timesheet> timesheetsJob = retrieveTimesheetsFromJob(job);

        Job storedjob = jobRepository.findById(job.getId()).orElse(null);
        
        
        if (storedjob == null) {
            System.out.println("Job not found!");
        }
        else {
            for (Timesheet timesheet : timesheetsJob) {
                User storedUser = userRepository.findById(timesheet.getEmployeeId()).orElse(null);
                if (storedUser != null)
                    employees.add(storedUser);
            }
        }
        return employees;
    }



    @GetMapping("/timesheets/jobs")
    public List<Timesheet> retrieveTimesheetsFromJob(@Valid @RequestBody Job job) {
        ArrayList<Timesheet> timesheetsJob = new ArrayList<>();
        List<Timesheet> timesheets = timesheetRepository.findAll();

        Job storedjob = jobRepository.findById(job.getId()).orElse(null);

        if (storedjob == null) {
            System.out.println("Job not found!");
        }
        else {
            for (Timesheet timesheet : timesheets) {
                if (timesheet.getJobId() == job.getId()){
                    timesheetsJob.add(timesheet);
                }
                
            }
        }
        System.out.println(timesheetsJob);
        return timesheetsJob;
    }

    // Create a new Job
    @PostMapping("/jobs")
    public Job createJob(@Valid @RequestBody Job job) {
        return jobRepository.save(job);
    }

    @PutMapping("/jobsassign")
    void assignJob(@Valid @RequestBody Selection selection) {
        Timesheet timesheet = new Timesheet();
        
        timesheet.setJobId(selection.getJob().getId());
        timesheet.setEmployeeId(selection.getEmployee().getId());
        timesheet.setApprovalStatus("Not reviewed");
        timesheetController.createTimesheet(timesheet);

    }

    // Get all Jobs
    @GetMapping("/jobs")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
    
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee (@PathVariable Long id) {
        try {
            Job job = jobRepository.getOne(id);
            jobRepository.delete(job);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

  
}

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

import org.json.JSONObject;
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
    public List<Job> getJobsByTimesheetID(@Valid @RequestBody Long timesheet_id){
        return jobRepository.findJobsFromTimesheetId(timesheet_id);
    }

    //Get timesheet from job and employee id
    @GetMapping("/timesheets/{employeeID}/{jobID}")
    public ResponseEntity getTimesheetByEmployeeAndJobID(@PathVariable Long employeeID, @PathVariable Long jobID) {
        //Making sure the jobID and employeeID are correct.
        try {
            Employee employee = employeeRepository.getOne(employeeID);
            Job job = jobRepository.getOne(jobID);
            List<Timesheet> foundTimesheets = timesheetRepository.findByEmployeeAndJob(employee, job);
            return new ResponseEntity<Object>(foundTimesheets, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Object>(new ApiResponse(false,
                    "Employee/Job not found!"), HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/timesheets/{jobID}")
    public List<Timesheet> getTimesheetByJobID(@PathVariable Long jobID){
        List<Timesheet> timesheets = timesheetRepository.findAll();
        List<Timesheet> jobTimesheet = new ArrayList<>();
        for (Timesheet timesheet: timesheets){
            if (timesheet.getJob().getID().equals(jobID))
                jobTimesheet.add(timesheet);
        }
        return jobTimesheet;
    }

    @GetMapping("/timesheets/employee/{employeeID}")
    public List<Timesheet> getTimesheetByEmployeeID(@PathVariable Long employeeID){
        List<Timesheet> timesheets = timesheetRepository.findAll();
        List<Timesheet> employeeTimesheet = new ArrayList<>();
        for (Timesheet timesheet: timesheets){
            if (timesheet.getEmployee().getID().equals(employeeID))
                employeeTimesheet.add(timesheet);
        }
        return employeeTimesheet;
    }
    
    /**
     * Approves a timesheet.
     *
     * @param timesheetID The ID of the timesheet to be approved.
     */
    @PutMapping("/timesheet/approve/{timesheetID}")
    public ResponseEntity approveTimesheet(@PathVariable Long timesheetID) {
        try{
            Timesheet ts = timesheetRepository.getOne(timesheetID);
            if (ts.getApprovalStatus() != 0){
                return new ResponseEntity<Object>(new ApiResponse(false,
                        "Timesheet has already been reviewed!") , HttpStatus.BAD_REQUEST);
            }
            else{
                ts.setApprovalStatus(2);
                timesheetRepository.save(ts);
                return new ResponseEntity<Object>(new ApiResponse(false,
                            "Timesheet has been reviewed!") , HttpStatus.OK);
            }
        }
        catch (EntityNotFoundException error){
            return new ResponseEntity<Object>(new ApiResponse(false,
                        "Timesheet has not been reviewed!") , HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Reject a timesheet.
     *
     * @param timesheetID The ID of the timesheet to be rejected.
     */
    @PutMapping("/timesheet/reject/{timesheetID}")
    public ResponseEntity rejectTimesheet(@PathVariable Long timesheetID) {
        System.out.println("ALSKDJLASJDK");
        try{
            Timesheet ts = timesheetRepository.getOne(timesheetID);
            if (ts.getApprovalStatus() != 0){
                return new ResponseEntity<Object>(new ApiResponse(false,
                        "Timesheet has already been reviewed!") , HttpStatus.BAD_REQUEST);
            }
            else{
                ts.setApprovalStatus(1);
                timesheetRepository.save(ts);
                return new ResponseEntity<Object>(new ApiResponse(false,
                            "Timesheet has been reviewed!") , HttpStatus.OK);
            }
        }
        catch (EntityNotFoundException error){
            return new ResponseEntity<Object>(new ApiResponse(false,
                        "Timesheet has not been reviewed!") , HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/timesheet/completion/{jobId}")
    public ResponseEntity getJobCompletionFromTimesheet(@PathVariable Long jobId) {
        List<Timesheet> timesheets = timesheetRepository.findAll();
        List<Timesheet> timesheetsOfJob = new ArrayList<>();

        for (Timesheet timesheet : timesheets) {
            if (timesheet.getJobID().equals(jobId)) {
                timesheetsOfJob.add(timesheet);
            }
        }

        int done = 0;
        for (Timesheet timesheet : timesheetsOfJob) {
            if (timesheet.getApprovalStatus() == 2) {
                done++;
            }
        }
        return new ResponseEntity<Object>(done/timesheetsOfJob.size(), HttpStatus.ACCEPTED);
    }
}

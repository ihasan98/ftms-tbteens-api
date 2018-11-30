package com.ftms.ftmsapi.controller;

import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Notification;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.payload.NotificationService;
import com.ftms.ftmsapi.repository.JobRepository;
import com.ftms.ftmsapi.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    NotificationController() {}

    // Get all the notifications for a certain employee with an ID
    @GetMapping("/{id}")
    public List<Notification> getNotificationByUserId(@PathVariable Long id) {
        return notificationService.findByUserId(id);
    }

    // Create the notification for an employee notifying him/her that a job has been assigned to him/her
    @PostMapping("/jobassigned")
    public ResponseEntity<?> createNewNotificationJobAssign(String info) {
        ArrayList<Object> validationResult = validateInfo(info);
        return getJobNotification(validationResult, ") has been assigned to you.", "JOB_ASSIGN");
    }

    // Create the notification for an employee notifying him/her that a job has been removed from him/her
    @PostMapping("/jobdeleted")
    public ResponseEntity<?> createNewNotificationJobDeleted(String info) {
        ArrayList<Object> validationResult = validateInfo(info);
        return getJobNotification(validationResult, ") has been removed from your queue.", "JOB_DELETE");
    }

    // Process the JSON result, what kind of job action is done (ASSIGN/DELETE), and the type of the notification type
    private ResponseEntity<?> getJobNotification(ArrayList<Object> validationResult, String action, String notifType)
            throws JSONException {
        // Look at the function for validationResult for what this is
        // If validationResult's size is only 1, this means there is an error -> go straight to return the error
        if (validationResult.size() > 1) {
            // If there are goodies inside:

            // at 0: the user, at 1: the job, at 2: the job name, at 3: the HTTP response
            User user = (User) validationResult.get(0);
            Job job = (Job) validationResult.get(1);
            String jobName = (String) validationResult.get(2);
            String message = "A new job (" + jobName + action;

            // create the notification
            notificationService.createNotification(message, user, notifType, job);

            // return http response
            return (ResponseEntity) validationResult.get(3);
        }

        return (ResponseEntity) validationResult.get(0);
    }

    // Create a new notification for an employee notifying him/her that a job's timesheet has been reviewed
    @PostMapping("/jobreviewed")
    public ResponseEntity<?> createNewNotificationJobReviewed(String info) {
        // Similar to the above function
        ArrayList<Object> validationResult = validateInfo(info);
        if (validationResult.size() > 1) {
            User user = (User) validationResult.get(0);
            Job job = (Job) validationResult.get(1);
            JSONObject parser = new JSONObject(info);

            // the JSON for a job-review notification also has a component indicating
            // whether or not the job is approved
            boolean approved = parser.get("approved").equals(true);
            String jobName = (String) validationResult.get(2);

            // if approved is true status is approved, else rejected
            String status = approved ? "approved" : "rejected";

            String message = "Your timesheet for the job " + jobName + " has been " + status + ".";
            String type = "JOB_REVIEW";

            // create the notification
            notificationService.createNotification(message, user, type, job);

            // return http response
            return (ResponseEntity) validationResult.get(3);
        }
        return (ResponseEntity) validationResult.get(0);
    }

    private ArrayList<Object> validateInfo(String info) {
        ArrayList<Object> result = new ArrayList<>();

        // Parse the string <info> into a JSON
        JSONObject notification = new JSONObject(info);

        // Get the values from the keys
        Long userID = Long.parseLong(notification.get("userid").toString());
        Long jobID = Long.parseLong(notification.get("jobid").toString());
        User user;
        Job job;

        // Make sure that the user exists (look for the user by ID)
        try {
            user = userRepository.getOne(userID);
        } catch (EntityNotFoundException error) {
            result.add(new ResponseEntity<Object>(new ApiResponse(false, "User not found."),
                    HttpStatus.BAD_REQUEST));
            return result;
        }


        // Make sure that the job exists (look for the job by ID)
        try {
            job = jobRepository.getOne(jobID);
        } catch (EntityNotFoundException error) {
            result.add(new ResponseEntity<Object>(new ApiResponse(false, "Job not found."),
                    HttpStatus.BAD_REQUEST));
            return result;
        }

        // (ABOVE) If there is any error, the array list returned will simply have the error Response Entity
        // encoding the error

        // If there are no errors above, add the user, job and job's name to the return list
        // Also, add the successful http response to the end of the list for higher level functions to access
        result.add(user);
        result.add(job);
        result.add(notification.get("jobname"));
        result.add(new ResponseEntity<Object>(new ApiResponse(true,
                "Notification for this job assignment to employee #" + userID + " successfully created"),
                HttpStatus.ACCEPTED));
        return result;
    }

    // Delete a notification by looking for it by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotificationById(@PathVariable Long id) {
        try {
            Notification notification = notificationService.getRepository().getOne(id);
            notificationService.getRepository().delete(notification);
            return new ResponseEntity<Object>(new ApiResponse(true, "Deletion of notification #" + id
                    + " successfully completed!"), HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException error) {
            return new ResponseEntity<Object>(new ApiResponse(false, "User not found."),
                    HttpStatus.BAD_REQUEST);
        }
    }


}

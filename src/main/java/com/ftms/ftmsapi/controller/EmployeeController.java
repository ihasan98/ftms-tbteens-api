package com.ftms.ftmsapi.controller;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Task;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.UserRepository;
import com.ftms.ftmsapi.services.EmailService;

import org.hashids.Hashids;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class EmployeeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    Hashids hashids = new Hashids("FTMS", 10);

    // Get all employees that are not an administrator
    @GetMapping("")
    public List<User> getAllEmployees() {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        ArrayList<User> nonAdmin = new ArrayList<>();
        // For every user
        for (User user: users) {
            // if not an admin
            if (user.getRole().equals("ROLE_EMPLOYEE") ||
                    user.getRole().equals("ROLE_SUPERVISOR")) {
                // add to list of users to display
                nonAdmin.add(user);
            }
        }
        return nonAdmin;
    }

    // Create a new employee.
    @PostMapping("")
    public User createEmployee(@Valid @RequestBody User user) {
        // Hashing User id
        User createdUser = userRepository.save(user);
        String id = hashids.encode(createdUser.getId());

        emailService.prepareAndSend(createdUser.getEmail(),
                "Account Activation",
                "Please visit http://localhost:3000/usersignup/" + id + " " + 
                        "to set up your account");
        return userRepository.save(createdUser);
    }

    // Delete an employee.
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee (@PathVariable Long id) {
        try {
            // Try to look for employee by <id>
            User employee = userRepository.getOne(id);
            userRepository.delete(employee);
            return new ResponseEntity<Object>(new ApiResponse(true, employee.getFirstname()
                                    + " " + employee.getLastname() + " deleted!"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // If cannot find, return bad request response
            return new ResponseEntity<Object>(new ApiResponse(true, "This employee does not exist!"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // Edit an employee
    @PutMapping("")
    public ResponseEntity<?> editEmployee (@Valid @RequestBody String info) {
        // Parse string into JSON
        JSONObject employee = new JSONObject(info);

        // Parse the keys
        Long id = Long.parseLong(employee.get("id").toString());
        String firstName = (String) employee.get("firstname");
        String lastName = (String) employee.get("lastname");
        String phone = (String) employee.get("number");

        // Success/Failure messages
        String success = "Edited EMPLOYEE #" + id.toString() + " " +
                firstName + " " + lastName + " successfully!";

        String failure = "Cannot find EMPLOYEE #" + id.toString();
        try {
            // Try to find the user by <id>
            User findUser = userRepository.getOne(id);

            // Success: set the info to new info
            findUser.setFirstname(firstName);
            findUser.setLastname(lastName);
            findUser.setNumber(phone);
            userRepository.save(findUser);
            return new ResponseEntity<Object>(new ApiResponse(true, success), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // If cannot find, return bad request response
            return new ResponseEntity<Object>(new ApiResponse(true, failure), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/jobs")
    public List<Job> retrieveJobsFromEmployee(@Valid @RequestBody User user, List<Task> tasks) {
        ArrayList<Job> jobs = new ArrayList<>();
        if (!userRepository.findAll().contains(user)) {
            System.out.println("User not found!");
        }
        else {
            for (Task task : tasks) {
                if (task.getEmployee().getId().equals(user.getId())) {
                    jobs.add(task.getJob());
                }
            }
        }
        return jobs;
    }
}
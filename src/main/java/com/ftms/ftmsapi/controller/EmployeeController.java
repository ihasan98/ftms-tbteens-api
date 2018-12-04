package com.ftms.ftmsapi.controller;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Timesheet;
import com.ftms.ftmsapi.model.Employee;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.EmployeeRepository;
import com.ftms.ftmsapi.repository.JobRepository;
import com.ftms.ftmsapi.repository.TimesheetRepository;
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

public class EmployeeController {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private EmailService emailService;

    Hashids hashids = new Hashids("FTMS", 10);

    // Get all employees that are not an administrator
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("")
    public List<Employee> getAllEmployees() {
        ArrayList<Employee> employees = (ArrayList<Employee>) employeeRepository.findAll();
        ArrayList<Employee> nonAdmin = new ArrayList<>();
        // For every Employee
        for (Employee employee: employees) {
            // if not an admin
            if (employee.getRole().equals("ROLE_EMPLOYEE") ||
                    employee.getRole().equals("ROLE_SUPERVISOR")) {
                // add to list of employees to display
                nonAdmin.add(employee);
            }
        }
        System.out.println(nonAdmin);
        return nonAdmin;
    }

    // Create a new employee.
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        // Hashing Employee id
        Employee createdEmployee = employeeRepository.save(employee);
        String id = hashids.encode(createdEmployee.getId());
        String message = "Hello" + " " + employee.getFirstname() + " " + employee.getLastname() + ", \n\n" +
                "We’re excited to welcome you to the company. Please follow this link to set your account up: " +
                "http://localhost:3000/usersignup/" + id +
                "\n If you encounter any problems, please contact admin." +
                "\n\n - Nor-Weld";
        emailService.prepareAndSend(createdEmployee.getEmail(),
                "Account Activation",
                message);
        return employeeRepository.save(createdEmployee);
    }

    // Delete an employee.
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee (@PathVariable Long id) {
        try {
            // Try to look for employee by <id>
            Employee employee = employeeRepository.getOne(id);
            employeeRepository.deleteById(employee.getId());
            return new ResponseEntity<Object>(new ApiResponse(true, employee.getFirstname()
                                    + " " + employee.getLastname() + " deleted!"), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // If cannot find, return bad request response
            return new ResponseEntity<Object>(new ApiResponse(true, "This employee does not exist!"),
                    HttpStatus.BAD_REQUEST);
        }
    }

    // Edit an employee
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
            // Try to find the Employee by <id>
            Employee findEmployee = employeeRepository.getOne(id);

            // Success: set the info to new info
            findEmployee.setFirstname(firstName);
            findEmployee.setLastname(lastName);
            findEmployee.setNumber(phone);
            employeeRepository.save(findEmployee);
            return new ResponseEntity<Object>(new ApiResponse(true, success), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // If cannot find, return bad request response
            return new ResponseEntity<Object>(new ApiResponse(false, failure), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}/jobs")
    public ResponseEntity<?> retrieveJobsFromEmployee(@PathVariable Long id) {
        ArrayList<Job> jobs = new ArrayList<>();
        List<Timesheet> timesheets = timesheetRepository.findAll();

        Employee user = employeeRepository.findById(id).orElse(null);
        //Check if user is found
        if (user == null) {
            return new ResponseEntity<Object>(new ApiResponse(false, "User not found"),
                    HttpStatus.BAD_REQUEST);
        }
        else {
            for (Timesheet timesheet : timesheets) {
                if (timesheet.getEmployeeId().equals(id)) {
                    Job job = jobRepository.findById(timesheet.getJobId()).orElse(null);
                    jobs.add(job);
                }
            }
        }
        return new ResponseEntity<Object>(jobs, HttpStatus.OK);
    }
}
package com.ftms.ftmsapi.controller;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Task;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/employees")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class EmployeeController {
    @Autowired
    UserRepository userRepository;

    // Get all employees
    @GetMapping("")
    public List<User> getAllEmployees() {
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        ArrayList<User> nonAdmin = new ArrayList<>();
        for (User user: users) {
            if (user.getRole().equals("ROLE_EMPLOYEE") ||
                    user.getRole().equals("ROLE_SUPERVISOR")) {
                nonAdmin.add(user);
            }
        }
        return nonAdmin;
    }

    // Create a new employee.
    @PostMapping("")
    public User createEmployee(@Valid @RequestBody User user) {
        return userRepository.save(user);
    }

    // Delete an employee.
    @DeleteMapping("/{id}")
    public List<String> deleteEmployee (@PathVariable Long id) {
        ArrayList<String> success =  new ArrayList<>();
        ArrayList<String> failure =  new ArrayList<>();
        success.add("202 ACCEPTED");
        success.add("400 BAD REQUEST");
        try {
            User employee = userRepository.getOne(id);
            userRepository.delete(employee);
            return success;
        } catch (EntityNotFoundException e) {
            return failure;
        }
    }

    // Edit an employee
    @PutMapping("/{id}")
    public List<String> editEmployee (@PathVariable Long id, @RequestParam String firstName,
                                  @RequestParam String lastName, @RequestParam String email,
                                  @RequestParam String phone) {
        ArrayList<String> value = new ArrayList<>();
        value.add("Hello!");
        try {
            User findUser = userRepository.getOne(id);

            findUser.setFirstname(firstName);
            findUser.setLastname(lastName);
            findUser.setNumber(phone);
            userRepository.save(findUser);
            return value;
        } catch (EntityNotFoundException e) {
            return value;
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
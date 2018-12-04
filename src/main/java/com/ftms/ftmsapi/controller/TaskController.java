package com.ftms.ftmsapi.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.*;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.TaskRepository;
import com.ftms.ftmsapi.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TimesheetRepository timesheetRepository;
    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/timesheets/{timesheet_id}/tasks")
    public ResponseEntity getTasksByTimesheet(@PathVariable Long timesheet_id) {
        try {
            Timesheet timesheet = timesheetRepository.getOne(timesheet_id);
            List<Task> tasks = taskRepository.findByTimesheet(timesheet);
            return new ResponseEntity<Object>(tasks, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Object>(new ApiResponse(false,
                    "Timesheet not found!"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/timesheets/{timesheet_id}/tasks")
    public ResponseEntity createTask(@Valid @RequestBody Task task,
                                     @PathVariable Long timesheet_id){
        try {
            Timesheet timesheet = timesheetRepository.getOne(timesheet_id);
            Task createdTask = taskRepository.save(task);
            createdTask.setTimesheet(timesheet);    // Set the timesheet to the correct one.
            return new ResponseEntity<Object>(createdTask, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<Object>(new ApiResponse(false,
                    "Something went wrong! Please try again!"),
                    HttpStatus.BAD_REQUEST);    // Return error message.
        }
    }


    @GetMapping("")
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    @PutMapping("/tasks/{id}")
    public void editTask(@PathVariable long id,
                         @RequestParam User employee, @RequestParam Job job,
                         @RequestParam String description){
        try{
            Task taskToEdit = taskRepository.getOne(id);
            taskToEdit.setDescription(description);

            // Consider changing to taskRepository.update(id)
            taskRepository.save(taskToEdit);
        } catch (EntityNotFoundException e) {
            // Consider sending HTTP Response instead
            System.out.println("No such id");
        }
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable long id){
        try{
            Task task = taskRepository.getOne(id);
            taskRepository.delete(task);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

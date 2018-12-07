package com.ftms.ftmsapi.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.*;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.PartRequestRepository;
import com.ftms.ftmsapi.repository.TaskRepository;
import com.ftms.ftmsapi.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TimesheetRepository timesheetRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    PartRequestRepository partRequestRepository;

    @GetMapping("tasks/{ID}")
    public ResponseEntity getTaskByID(@PathVariable Long ID) {
        try {
            return new ResponseEntity<Object>(taskRepository.getOne(ID), HttpStatus.OK);
        } catch (EntityNotFoundException error) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Task not found."),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/timesheets/{timesheet_id}/tasks")
    public ResponseEntity getTasksByTimesheetID(@PathVariable Long timesheet_id) {
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
            task.setTimesheet(timesheet);    // Set the timesheet to the correct one.
            Task createdTask = taskRepository.save(task);
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
    public ResponseEntity editTask(@PathVariable Long id, @Valid @RequestBody Task task){
        try {
            Task taskToEdit = taskRepository.getOne(id);
            taskToEdit.setName(task.getName());
            taskToEdit.setDescription(task.getDescription());

            // Consider changing to taskRepository.update(id)
            taskRepository.save(taskToEdit);
            return new ResponseEntity<Object>(new ApiResponse(true, "Task updated successfully!"),
                    HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e) {
            // Consider sending HTTP Response instead
            return new ResponseEntity<Object>(new ApiResponse(false,
                    "Cannot find the task specified."),
                    HttpStatus.BAD_REQUEST);
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

    @PostMapping("tasks/parts")
    public ResponseEntity createPartRequest(@Valid @RequestBody PartRequest partRequest) {
        System.out.println("error");
        try {
            PartRequest newRequest = partRequestRepository.save(partRequest);
            return new ResponseEntity<Object>(newRequest, HttpStatus.ACCEPTED);
        } catch (Exception error) {
            error.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/tasksById/{id}")
//    public Optional<Task> getTaskById(@PathVariable long id){
//        return taskRepository.findById(id);
//    }
}

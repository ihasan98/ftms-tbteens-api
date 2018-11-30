package com.ftms.ftmsapi.controller;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.repository.TaskRepository;
import com.ftms.ftmsapi.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @PostMapping("")
    public Task createTask(@Valid @RequestBody Task task){
        return taskRepository.save(task);
    }

    @RequestMapping("/tasks/get")
    public List<Task> getTasksByJobEmployee(@Valid @RequestBody Long job_id, Long employee_id) {
        return taskRepository.getByJobAndEmployee(job_id, employee_id);
    }

        @GetMapping("")
    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable long id){
        try{
            Task task = taskRepository.getOne(id);
            taskRepository.delete(task);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public void editTask(@PathVariable long id,
                         @RequestParam User employee, @RequestParam Job job,
                         @RequestParam String description){
        try{
            Task taskToEdit = taskRepository.getOne(id);
            taskToEdit.setJob(job);
            taskToEdit.setEmployee(employee);
            taskToEdit.setDescription(description);

            // Consider changing to taskRepository.update(id)
            taskRepository.save(taskToEdit);
        } catch (EntityNotFoundException e) {
            // Consider sending HTTP Response instead
            System.out.println("No such id");
        }
    }
}

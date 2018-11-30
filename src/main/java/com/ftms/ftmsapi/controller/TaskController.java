package com.ftms.ftmsapi.controller;

import javax.validation.Valid;
import com.ftms.ftmsapi.repository.TaskRepository;
import com.ftms.ftmsapi.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @RequestMapping("/tasks/get")
    public List<Task> getTasksByJobEmployee(@Valid @RequestBody Long job_id, Long employee_id) {
        return taskRepository.getByJobAndEmployee(job_id, employee_id);
    }
}

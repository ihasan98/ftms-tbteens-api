package com.ftms.ftmsapi.repository;
import com.ftms.ftmsapi.model.Task;
import com.ftms.ftmsapi.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTimesheet(Timesheet timesheet);
}
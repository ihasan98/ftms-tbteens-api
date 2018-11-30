package com.ftms.ftmsapi.repository;
import com.ftms.ftmsapi.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    String JobEmployeeQuery = "SELECT * FROM task WHERE job = ?1 AND employee = ?2";

    @Query(value = JobEmployeeQuery, nativeQuery = true)
    List<Task> getByJobAndEmployee(Long job_id, Long employee_id);
}
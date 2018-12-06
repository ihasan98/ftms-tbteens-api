package com.ftms.ftmsapi.repository;
import com.ftms.ftmsapi.model.Employee;
import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    // Find timesheet by job and employee.
    public List<Timesheet> findByJobAndEmployee(Employee employee, Job job);
}

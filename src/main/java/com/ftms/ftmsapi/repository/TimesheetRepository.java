package com.ftms.ftmsapi.repository;
import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    String FIND_TIMESHEET_FROM_EMPLOYEE_ID_AND_JOB_ID = "SELECT * FROM timesheet WHERE employee_id = ?1 AND job_id = ?2";
    @Query(value = FIND_TIMESHEET_FROM_EMPLOYEE_ID_AND_JOB_ID, nativeQuery = true)
    public List<Timesheet> findTimesheetFromEmployeeIdAndJobId(Long employee_id, Long job_id);

    //Not used rt now
    // SQL queries
    String FIND_TIMESHEET_ID_FROM_EMPLOYEE_ID = "SELECT id FROM timesheet WHERE employee_id = ?1";
    @Query(value = FIND_TIMESHEET_ID_FROM_EMPLOYEE_ID, nativeQuery = true)
    public List<Integer> findTimesheetIdFromEmployeeId(Long employee_id);


    String FIND_TIMESHEET_FROM_EMPLOYEE_ID = "SELECT * FROM timesheet WHERE employee_id = ?1";
    @Query(value = FIND_TIMESHEET_FROM_EMPLOYEE_ID, nativeQuery = true)
    public List<Timesheet> findTimesheetFromEmployeeId(Long employee_id);





}

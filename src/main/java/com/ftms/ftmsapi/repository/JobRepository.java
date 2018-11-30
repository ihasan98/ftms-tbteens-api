package com.ftms.ftmsapi.repository;
import com.ftms.ftmsapi.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {


    // SQL queries
    String FIND_JOBS_FROM_TIMESHEET_ID = "SELECT * FROM jobs WHERE timesheet_id = ?1";


    @Query(value = FIND_JOBS_FROM_TIMESHEET_ID, nativeQuery = true)
    public List<Job> findJobsFromTimesheetId(Long timesheet_id);

}

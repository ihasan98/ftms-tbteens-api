package com.ftms.ftmsapi.controller;

import com.ftms.ftmsapi.model.CurrSession;
import com.ftms.ftmsapi.model.Task;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.repository.CurrSessionRepository;
import com.ftms.ftmsapi.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping("/api")
public class CurrSessionController {

    @Autowired
    CurrSessionRepository currSessionRepository;

    @Autowired
    TaskRepository taskRepository;

    @GetMapping("/sessions/bytask/{taskID}")
    public ResponseEntity getSessionsByTaskID(@PathVariable Long taskID) {
        ArrayList<CurrSession> sessions = new ArrayList<>();
        for (CurrSession currSession: currSessionRepository.findAll()) {
            if (currSession.getTaskID().equals(taskID)) {
                sessions.add(currSession);
            }
        }
        return new ResponseEntity<Object>(sessions, HttpStatus.ACCEPTED);
    }

    @GetMapping("/sessions/byid/{ID}")
    public ResponseEntity getSessionByID(@PathVariable Long ID) {
        CurrSession session;
        try {
            session = currSessionRepository.getOne(ID);
        } catch (EntityNotFoundException error) {
            error.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(session, HttpStatus.ACCEPTED);
    }

    @PostMapping("/sessions/create/{taskID}")
    public ResponseEntity initiateNewSession(@PathVariable Long taskID) {
        TimeZone timezone = TimeZone.getTimeZone("GMT-5");
        try {
            Task task = taskRepository.getOne(taskID);
            CurrSession newSession = new CurrSession(taskID);
            currSessionRepository.save(newSession);

            System.out.println("ID " + newSession.getID());

            if (task.getStartingSession() == null) {
                task.setStartingSession(newSession.getID());
            }
            task.setLatestSession(newSession.getID());
            taskRepository.save(task);
            return new ResponseEntity<Object>(newSession, HttpStatus.ACCEPTED);
        } catch (Exception error){
            error.printStackTrace();
            return new ResponseEntity<Object>(new ApiResponse(false, error.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/sessions/end/{id}")
    public ResponseEntity endSessionById(@PathVariable Long id) {
        CurrSession session;
        try {
            session = currSessionRepository.getOne(id);
            if (session.getEndTime() == null) {
                // get current time
                Long endTime = new Date().getTime();

                // set the end time
                session.setEndTime(endTime);

                // get the time elapsed
                long start = session.getStartTime();
                long end = session.getEndTime();

                long elapsedMilliseconds = end - start;
                double elapsedHours = (double) elapsedMilliseconds/3600000;

                session.setHoursElapsed(elapsedHours);

                session.stopProgress();

                // save
                currSessionRepository.save(session);

                System.out.println("taskid " + session.getTaskID());
                System.out.println("sessionid " + session.getID());
                Task task = taskRepository.getOne(session.getTaskID());
                task.setLatestSession(session.getID());
                task.incrementHoursElapsed(elapsedHours);
                taskRepository.save(task);
                return new ResponseEntity<Object>(new ApiResponse(true,
                        "Session ended successfully!"),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<Object>(new ApiResponse(false,
                        "This session has already ended!"),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (EntityNotFoundException error) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Cannot find session!"),
                    HttpStatus.BAD_REQUEST);
        }
    }
}

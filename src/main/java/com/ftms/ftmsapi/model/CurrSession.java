package com.ftms.ftmsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Entity
@Table(name="curr_session")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CurrSession implements Serializable {

    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startTime;

    // End times need to have the option to blank as they get added later.
    private Long endTime;

    private String startTimeString;

    private String endTimeString;

    private double hoursElapsed;

    private boolean inProgress;

    @Column(name = "taskid")
    private Long taskID;

    public CurrSession() {}

    public CurrSession(Long taskID) {
        this.hoursElapsed = 0.0;
        this.taskID = taskID;
        this.startTime = new Date().getTime();
        this.startTimeString = convertTimeToDisplay(this.startTime);
        this.inProgress = true;
    }

    // GETTERS/SETTERS
    public Long getID(){
        return id;
    }

    public Long getStartTime(){
        return startTime;
    }

    public void setStartTime(Long startTime){
        this.startTime = startTime;
    }

    public Long getEndTime(){
        return this.endTime;
    }

    public void setEndTime(Long endTime){
        this.endTime = endTime;
        this.endTimeString = convertTimeToDisplay(this.endTime);
    }

    public void setTaskID(Long taskID) {
        this.taskID = taskID;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Long getTaskID() {
        return taskID;
    }

    public double getHoursElapsed() {
        return hoursElapsed;
    }

    public void setHoursElapsed(double time) {
        hoursElapsed = time;
    }

    private String convertTimeToDisplay(Long time) {
        // Create new calendar and set to Toronto timezone
        GregorianCalendar newCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT-5"));

        // Set time to time wanted to convert
        newCalendar.setTimeInMillis(time);

        // convert to a string of all the info
        String string = newCalendar.toString();

        // find where "HOUR_OF_DAY" is in the string
        int hourIndex = string.lastIndexOf("HOUR_OF_DAY");
        String hour;

        // if the hour has 2 digits
        if (string.charAt(hourIndex + 13) == ',') {
            hour = string.substring(hourIndex + 12, hourIndex + 13);
            // if 1 digit
        } else {
            hour = string.substring(hourIndex + 12, hourIndex + 14);
        }

        // find where "MINUTE" is in the string
        int minuteIndex = string.lastIndexOf("MINUTE");
        String minute;

        // if the minute has 2 digits
        if (string.charAt(minuteIndex + 8) == ',') {
            minute = string.substring(minuteIndex + 7, minuteIndex + 8);
            // if 1 digit
        } else {
            minute = string.substring(minuteIndex + 7, minuteIndex + 9);
        }

        // build the display time
        String finalTime;
        // 1 digit hour and 1 digit minute
        if (hour.length() == 1 && minute.length() == 1) {
            finalTime = "0" + hour + ":0" + minute;
            // 2 digits hour and 1 digit minute
        } else if (hour.length() == 2 && minute.length() == 1) {
            finalTime = hour + ":0" + minute;
            // 1 digit hour and 2 digits minute
        } else if(hour.length() == 1 && minute.length() == 2) {
            finalTime = "0" + hour + ":" + minute;
            // 2 digits both
        } else {
            finalTime = hour + ":" + minute;
        }
        return finalTime;
    }

    public String getStartTimeString() {
        return startTimeString;
    }

    public String getEndTimeString() {
        return endTimeString;
    }

    public boolean getInProgress() {
        return inProgress;
    }

    public void stopProgress() {
        inProgress = false;
    }
}


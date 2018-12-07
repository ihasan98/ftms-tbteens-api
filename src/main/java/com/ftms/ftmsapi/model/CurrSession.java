package com.ftms.ftmsapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="curr_session")
public class CurrSession implements Serializable {

    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Can store ZonedDateTime instead if needed
    @NotBlank
    private LocalDateTime startTime;

    //Can store ZonedDateTime instead if needed
    // End times need to have the option to blank as they get added later.
    private LocalDateTime endTime;

    private Task task;

    // GETTERS/SETTERS
    public Long getId(){
        return id;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

    public void setTask(Task task) {
        this.task = task;
    }
    @NotBlank
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Task getTask() {
        return task;
    }
}

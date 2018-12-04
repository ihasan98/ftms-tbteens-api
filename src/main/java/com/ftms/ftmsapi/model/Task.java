package com.ftms.ftmsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name="tasks")
public class Task implements Serializable{

    // INSTANCE FIELDS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @JsonIgnore
    @JoinColumn(name = "timesheet_id")
    private Timesheet timesheet;

    // GETTERS/SETTERS

    /**
     * Getter for ID
     *
     * @return The ID of this.
     */
    public Long getId() {
        return id;
    }


    /**
     * Getter for the timesheet.
     *
     * @return The timesheet for the task.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    public Timesheet getTimesheet() {
        return timesheet;
    }

    /**
     * Setter for the timesheet.
     *
     * @param timesheet The timesheet to be changed.
     */
    public void setTimesheet(Timesheet timesheet) {
        this.timesheet = timesheet;
    }

    /**
     * The getter for name.
     *
     * @return The name of the task.
     */
    public String getName() {
        return name;
    }

    /**
     * The setter for name.
     *
     * @param name The name to be changed.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getter for description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * The setter for description.
     *
     * @param description The description to be changed.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}

package com.ftms.ftmsapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

    @Entity
    @Table(name="curr_session")
    public class CurrSession implements Serializable {

        // INSTANCE FIELDS
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        private Long startTime;

        // End times need to have the option to blank as they get added later.
        private Long endTime;

        private Task task;

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


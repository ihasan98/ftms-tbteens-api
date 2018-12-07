package com.ftms.ftmsapi.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name="notifications")
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    private String message;

    private LocalDateTime createdAt;

    private boolean isRead;

    private String type;

    private Long jobID;

    private Long userID;

    public Notification(){}

    public Notification(String message, LocalDateTime createdAt, Long userID, String type, Long jobID){
        this.message = message;
        this.createdAt = createdAt;
        this.userID = userID;
        this.type = type;
        this.jobID = jobID;
        this.isRead = false;
    }


    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getType() { return type; }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}

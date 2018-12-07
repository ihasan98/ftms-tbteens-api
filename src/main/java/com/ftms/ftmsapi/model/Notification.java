package com.ftms.ftmsapi.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="notifications")
public class Notification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notificationId;

    private String message;

    private Date createdAt;

    private boolean isRead;

    private String type;

    private Long jobID;

    private Long userID;

    public Notification(){}

    public Notification(String message, Date createdAt, Long userID, String type, Long jobID){
        this.message = message;
        this.createdAt = createdAt;
        this.userID = userID;
        this.type = type;
        this.jobID = jobID;
        this.isRead = false;
    }


    public Long getNotificationID() {
        return notificationId;
    }

    public void setNotificationID(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
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

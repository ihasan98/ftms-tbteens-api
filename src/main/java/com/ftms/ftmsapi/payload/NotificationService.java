package com.ftms.ftmsapi.payload;

import com.ftms.ftmsapi.model.Job;
import com.ftms.ftmsapi.model.Notification;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.repository.NotificationRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public NotificationService() {}

    private ResponseEntity<?> updateUserNotification(Notification notification) {
        Notification savedNotification = save(notification);
        if (savedNotification == null) {
            return new ResponseEntity<Object>(new ApiResponse(false, "Notification not updated."),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<Object>(new ApiResponse(true, "Notification updated."),
                HttpStatus.OK);
    }

    private Notification save(Notification notification) {
        try {
            return notificationRepository.save(notification);
        } catch (Exception e) {
            logger.error("Exception occurred while saving notification", e);
            return null;
        }
    }

    public List<Notification> findByUserId(Long id) {
        ArrayList<Notification> notifications = (ArrayList<Notification>) notificationRepository.findAll();
        ArrayList<Notification> notificationOfUser = new ArrayList<>();
        for (Notification notification: notifications) {
            if (notification.getUserID().equals(id)){
                notificationOfUser.add(notification);
            }
        }
        return notificationOfUser;
    }

    public Notification createNotification(String message, Long userID, String type, Long jobID) {
        Notification notification = new Notification(message, new Date(), userID, type, jobID);
        updateUserNotification(notification);
        return notification;
    }

    public NotificationRepository getRepository() {
        return notificationRepository;
    }
}


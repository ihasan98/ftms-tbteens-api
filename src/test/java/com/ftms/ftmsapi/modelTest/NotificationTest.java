package com.ftms.ftmsapi.modelTest;

import org.junit.Test;
import com.ftms.ftmsapi.model.Notification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.Assert.*;

public class NotificationTest {

    Notification notification = new Notification();
    @Test
    public void testNotificationId() {
        notification.setNotificationId(1000L);
        assertEquals(notification.getNotificationId(), 1000L, 1000L );
    }

    @Test
    public void testNotificationMessage() {
        notification.setMessage("Hi Ece!");
        assertEquals(notification.getMessage(), "Hi Ece!");
    }

    @Test
    public void testNotificationCreatedAt() {
        LocalDateTime d = LocalDateTime.now();
        notification.setCreatedAt(d);
        assertEquals(notification.getCreatedAt(), d);
    }

    @Test
    public void testNotificationUserID() {
        notification.setUserID(1000L);
        assertEquals(notification.getUserID(), 1000L, 1000L);
    }

    @Test
    public void testNotificationRead() {
        notification.setRead(true);
        assertEquals(notification.isRead(), true);
    }

}
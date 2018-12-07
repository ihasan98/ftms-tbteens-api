package com.ftms.ftmsapi.modelTest;

import org.junit.Test;
import com.ftms.ftmsapi.model.Notification;
import java.util.Date;

import static org.junit.Assert.*;

public class NotificationTest {

    Notification notification = new Notification();
    @Test
    public void testNotificationId() {
        notification.setNotificationID(1000L);
        assertEquals(notification.getNotificationID(), 1000L, 1000L );
    }

    @Test
    public void testNotificationMessage() {
        notification.setMessage("Hi Ece!");
        assertEquals(notification.getMessage(), "Hi Ece!");
    }

    @Test
    public void testNotificationCreatedAt() {
        Date d = new Date();
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
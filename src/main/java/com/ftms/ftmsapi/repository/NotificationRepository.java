package com.ftms.ftmsapi.repository;

import com.ftms.ftmsapi.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    //SQL query to select rows that match notification_id
    String Notification = "SELECT * FROM notifications WHERE notification_id = ?1";

    @Query(value = Notification, nativeQuery = true)
    Notification getNotification(Long notification_id);

}

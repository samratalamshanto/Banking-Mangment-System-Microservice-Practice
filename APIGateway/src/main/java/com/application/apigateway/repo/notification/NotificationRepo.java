package com.application.apigateway.repo.notification;

import com.application.apigateway.entity.notification.NotificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends JpaRepository<NotificationDetails, Long> {
}

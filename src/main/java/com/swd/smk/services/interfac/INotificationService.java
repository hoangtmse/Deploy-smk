package com.swd.smk.services.interfac;

import com.swd.smk.dto.NotificationDTO;
import com.swd.smk.dto.Response;

import java.util.List;

public interface INotificationService {
    Response createNotification(NotificationDTO notificationDTO);
    Response updateNotification(Long notificationId, NotificationDTO notificationDTO);
    Response deleteNotification(Long notificationId);
    Response getNotificationById(Long notificationId);
    Response getAllNotifications();
}


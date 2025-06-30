package com.swd.smk.services.impl;

import com.swd.smk.dto.NotificationDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.Notification;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.NotificationRepository;
import com.swd.smk.services.interfac.INotificationService;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Response createNotification(NotificationDTO notificationDTO) {
        Response response = new Response();
        try{
            // Validate the notificationDTO fields
            Optional<Member> memberOpt = memberRepository.findById(notificationDTO.getMemberId());
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + notificationDTO.getMemberId());
            }

            Notification notification = new Notification();
            notification.setMember(memberOpt.get());
            notification.setStatus(notificationDTO.getStatus());
            notification.setTitle(notificationDTO.getTitle());
            notification.setMessage(notificationDTO.getMessage());
            notification.setSentDate(LocalDateTime.now());
            notification.setStatus(Status.ACTIVE);
            notificationRepository.save(notification);
            response.setStatusCode(200);
            response.setMessage("Notification created successfully");
            response.setNotification(Converter.convertNotificationToDTO(notification));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateNotification(Long notificationId, NotificationDTO notificationDTO) {
        Response response = new Response();
        try{
            Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
            if (notificationOpt.isEmpty()) {
                throw new OurException("Notification not found with ID: " + notificationId);
            }

            Notification notification = notificationOpt.get();
            if (notificationDTO.getStatus() != null) {
                notification.setStatus(notificationDTO.getStatus());
            }
            if (notificationDTO.getTitle() != null) {
                notification.setTitle(notificationDTO.getTitle());
            }
            if (notificationDTO.getMessage() != null) {
                notification.setMessage(notificationDTO.getMessage());
            }
            notification.setSentDate(LocalDateTime.now());
            notificationRepository.save(notification);

            response.setStatusCode(200);
            response.setMessage("Notification updated successfully");

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteNotification(Long notificationId) {
        Response response = new Response();
        try {
            Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
            if (notificationOpt.isEmpty()) {
                throw new OurException("Notification not found with ID: " + notificationId);
            }
            Notification notification = notificationOpt.get();
            notification.setStatus(Status.DELETED);
            notificationRepository.save(notification);

            response.setStatusCode(200);
            response.setMessage("Notification deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getNotificationById(Long notificationId) {
        Response response = new Response();
        try {
            Optional<Notification> notificationOpt = notificationRepository.findById(notificationId);
            if (notificationOpt.isEmpty()) {
                throw new OurException("Notification not found with ID: " + notificationId);
            }
            Notification notification = notificationOpt.get();
            NotificationDTO dto = Converter.convertNotificationToDTO(notification);
            response.setStatusCode(200);
            response.setMessage("Notification retrieved successfully");
            response.setNotification(dto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllNotifications() {
        Response response = new Response();
        try {
            List<Notification> notifications = notificationRepository.findAll();
            List<NotificationDTO> notificationDTOs = notifications.stream()
                    .map(Converter::convertNotificationToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("All notifications retrieved successfully");
            response.setNotifications(notificationDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getNotificationsByMemberId(Long memberId) {
        Response response = new Response();
        try {
            List<Notification> notifications = notificationRepository.findByMemberId(memberId);
            if (notifications.isEmpty()) {
                throw new OurException("No notifications found for member with ID: " + memberId);
            }
            List<NotificationDTO> notificationDTOs = notifications.stream()
                    .map(Converter::convertNotificationToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Notifications retrieved successfully");
            response.setNotifications(notificationDTOs);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }
}


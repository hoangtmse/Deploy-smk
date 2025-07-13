package com.swd.smk.services.impl;

import com.swd.smk.dto.PlanDayDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Notification;
import com.swd.smk.model.plandetails.PlanDay;
import com.swd.smk.model.plandetails.PlanWeek;
import com.swd.smk.repository.NotificationRepository;
import com.swd.smk.repository.PlanDayRepository;
import com.swd.smk.repository.PlanRepository;
import com.swd.smk.repository.PlanWeekRepository;
import com.swd.smk.services.interfac.IPlanDay;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PlanDayService implements IPlanDay {

    @Autowired
    private PlanDayRepository  planDayRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanWeekRepository planWeekRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Response updatePlanDay(Long weekId, Long dayId, PlanDayDTO planDayDTO) {
        Response response = new Response();
        try {
            PlanWeek planWeek = planWeekRepository.findById(weekId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan week not found with ID: " + weekId));

            PlanDay planDay = planDayRepository.findByIdAndWeekId(dayId, weekId);
            if (planDay == null) {
                throw new IllegalArgumentException("Plan day not found ");
            }

            // Update the plan day details
            if (planDayDTO.getGoal() != null) {
                planDay.setGoal(planDayDTO.getGoal());
            }
            if (planDayDTO.getTask() != null) {
                planDay.setTask(planDayDTO.getTask());
            }
            if (planDayDTO.getTip() != null) {
                planDay.setTip(planDayDTO.getTip());
            }
            planDayRepository.save(planDay);
            response.setPlanDay(Converter.convertPlanDayToDTO(planDay));
            response.setStatusCode(200);
            response.setMessage("Plan day updated successfully");

            // Create a notification for the update
            Notification notification = new Notification();
            notification.setMember(planWeek.getPlan().getMember());
            notification.setStatus(Status.ACTIVE);
            notification.setTitle("Plan Day Updated");
            notification.setMessage("Plan day with ID " + dayId + " has been updated.");
            notification.setSentDate(LocalDateTime.now());
            notificationRepository.save(notification);

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
    public Response deletePlanDay(Long weekId, int dayNumber) {
        return null;
    }

    @Override
    public Response getPlanDayByWeekId(Long weekId) {
        return null;
    }
}

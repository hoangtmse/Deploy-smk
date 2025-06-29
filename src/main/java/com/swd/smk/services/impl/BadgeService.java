package com.swd.smk.services.impl;

import com.swd.smk.dto.BadgeDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Badge;
import com.swd.smk.repository.BadgeRepository;
import com.swd.smk.services.interfac.IBadgeService;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BadgeService implements IBadgeService {

    @Autowired
    private BadgeRepository badgeRepository;

    @Override
    public Response createBadge(BadgeDTO badgeDTO) {
        Response response = new Response();
        try{
            // Validate badgeDTO fields
            if (badgeDTO.getBadgeName() == null || badgeDTO.getBadgeName().trim().isEmpty()) {
                throw new OurException("Badge name is required");
            }

            Badge badge = new Badge();
            badge.setBadgeName(badgeDTO.getBadgeName());
            badge.setDescription(badgeDTO.getDescription());
            badge.setStatus(Status.ACTIVE);
            badge.setDateCreated(LocalDate.now());
            badge.setDateUpdated(LocalDate.now());

            badgeRepository.save(badge);
            response.setStatusCode(200);
            response.setMessage("Badge created successfully");
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
    public Response updateBadge(Long badgeId, BadgeDTO badgeDTO) {
        Response response = new Response();
        try{
            // Validate badgeDTO fields
            if (badgeDTO.getBadgeName() == null || badgeDTO.getBadgeName().trim().isEmpty()) {
                throw new OurException("Badge name is required");
            }

            Badge badge = badgeRepository.findById(badgeId)
                    .orElseThrow(() -> new OurException("Badge not found"));


            if (badgeDTO.getBadgeName() != null) badge.setBadgeName(badgeDTO.getBadgeName());
            if (badgeDTO.getDescription() != null) badge.setDescription(badgeDTO.getDescription());
            badge.setDateUpdated(LocalDate.now());

            badgeRepository.save(badge);

            response.setBadge(Converter.convertBadgeToDTO(badge));
            response.setStatusCode(200);
            response.setMessage("Badge updated successfully");
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
    public Response deleteBadge(Long badgeId) {
        Response response = new Response();
        try {
            Badge badge = badgeRepository.findById(badgeId)
                    .orElseThrow(() -> new OurException("Badge not found"));
            badge.setStatus(Status.DELETED);
            badgeRepository.save(badge);

            response.setStatusCode(200);
            response.setMessage("Badge deleted successfully");
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
    public Response getBadgeById(Long badgeId) {
        Response response = new Response();
        try {
            Badge badge = badgeRepository.findById(badgeId)
                    .orElseThrow(() -> new OurException("Badge not found"));
            response.setStatusCode(200);
            response.setBadge(Converter.convertBadgeToDTO(badge));
            response.setMessage("Fetched badge by id");
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
    public Response getAllBadges() {
        Response response = new Response();
        try {
            List<Badge> badges = badgeRepository.findAll();
            List<BadgeDTO> dtos = badges.stream()
                    .map(Converter::convertBadgeToDTO)
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setBadges(dtos);
            response.setMessage("Fetched all badges");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateBadgeImage(Long badgeId, String imageUrl) {
        Response response = new Response();
        try {
            Badge badge = badgeRepository.findById(badgeId)
                    .orElseThrow(() -> new OurException("Badge not found"));

            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                throw new OurException("Image URL is required");
            }

            badge.setImageUrl(imageUrl);
            badge.setDateUpdated(LocalDate.now());
            badgeRepository.save(badge);

            response.setStatusCode(200);
            response.setMessage("Badge image updated successfully");
            response.setBadge(Converter.convertBadgeToDTO(badge));
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

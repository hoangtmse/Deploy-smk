package com.swd.smk.services.interfac;

import com.swd.smk.dto.BadgeDTO;
import com.swd.smk.dto.Response;

public interface IBadgeService {

    Response createBadge(BadgeDTO badgeDTO);

    Response updateBadge(Long badgeId, BadgeDTO badgeDTO);

    Response deleteBadge(Long badgeId);

    Response getBadgeById(Long badgeId);

    Response getAllBadges();
}

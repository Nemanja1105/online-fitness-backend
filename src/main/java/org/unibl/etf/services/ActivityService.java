package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.ActivityDTO;
import org.unibl.etf.models.dto.ActivityRequestDTO;

import java.util.List;

public interface ActivityService {
    List<ActivityDTO> findAllActivitiesForClient(Long clientId, Authentication authentication);
    ActivityDTO insertActivityForClient(Long clientId, ActivityRequestDTO requestDTO,Authentication authentication);
    void deleteActivity(Long clientId,Long activityId,Authentication authentication);
}

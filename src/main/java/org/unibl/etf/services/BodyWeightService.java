package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.BodyWeightDTO;
import org.unibl.etf.models.dto.BodyWeightFilterDTO;
import org.unibl.etf.models.dto.BodyWeightRequestDTO;
import org.unibl.etf.models.dto.BodyWeightStatisticDTO;

public interface BodyWeightService {
    BodyWeightDTO insertBodyWeightForClient(Long clientId, BodyWeightRequestDTO requestDTO, Authentication authentication);
    BodyWeightStatisticDTO findStatisticForClient(Long clientId, BodyWeightFilterDTO request, Authentication authentication);
}

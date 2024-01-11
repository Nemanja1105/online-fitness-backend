package org.unibl.etf.services;

import org.unibl.etf.models.dto.FitnessNewsDTO;

import java.util.List;

public interface RssService {
    List<FitnessNewsDTO> consumeFeed();
}

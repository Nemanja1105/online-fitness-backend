package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.CategorySubscribeDTO;

import java.util.List;

public interface CategorySubscribeService {
    List<CategorySubscribeDTO> findAllForClient(Long id, Authentication auth);
    void changeSubscribeForClient(Long categoryId,Long clientId,Authentication auth);
}

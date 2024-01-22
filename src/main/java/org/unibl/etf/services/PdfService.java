package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.PdfDTO;

public interface PdfService {
    PdfDTO generatePdfForClient(Long clientId);
}

package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PdfDTO {
    private String fileName;
    private byte[] data;
}

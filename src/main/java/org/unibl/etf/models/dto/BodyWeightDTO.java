package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BodyWeightDTO extends BodyWeightRequestDTO {
    private Long id;
    private Long clientId;
}

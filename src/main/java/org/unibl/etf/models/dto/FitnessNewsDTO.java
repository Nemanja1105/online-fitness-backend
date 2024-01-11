package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitnessNewsDTO {
    private String category;
    private String title;
    private String description;
    private String link;
}

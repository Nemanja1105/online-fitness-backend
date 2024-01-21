package org.unibl.etf.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityRequestDTO {
    @NotBlank
    protected String name;
    protected Integer sets;
    protected Integer reps;
    protected Integer weight;
}

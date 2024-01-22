package org.unibl.etf.models.dto;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BodyWeightRequestDTO {
    @NotNull
    @Min(0)
    protected Double weight;
    @NotNull
    protected Date createdAt;
}

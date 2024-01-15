package org.unibl.etf.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.entities.ImageEntity;
import org.unibl.etf.models.enums.Difficulty;
import org.unibl.etf.models.enums.Location;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitnessProgramRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Long categoryId;
    @NotNull
    private BigDecimal price;
    @NotNull
    private Integer duration;
    @NotBlank
    private String difficulty;
    @NotBlank
    private String location;
    @NotNull
    private Long imageId;
    @NotBlank
    private String instructorName;
    @NotBlank
    private String instructorSurname;
    @NotBlank
    private String instructorContact;
    @NotBlank
    private String locationLink;
    @NotNull
    private List<AttributeRequestDTO> attributes;
}

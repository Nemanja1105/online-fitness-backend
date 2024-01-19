package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitnessProgramDTO {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal price;
    private Integer duration;
    private String difficulty;
    private String location;
    private Long imageId;
    private String instructorName;
    private String instructorSurname;
    private String instructorContact;
    private String linkAddress;
    private Date createdAt;
    private List<AttributeDTO> attributes;
    private ClientInfoDTO client;
}

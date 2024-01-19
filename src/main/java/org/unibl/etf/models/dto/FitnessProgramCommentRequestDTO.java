package org.unibl.etf.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitnessProgramCommentRequestDTO {
    @NotBlank
    private String comment;
    @NotNull
    private Long senderId;
    @NotNull
    private Long fitnessProgramId;
}

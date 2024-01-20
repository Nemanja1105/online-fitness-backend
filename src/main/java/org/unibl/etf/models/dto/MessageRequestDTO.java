package org.unibl.etf.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDTO {
    @NotBlank
    @Size(max=1000)
    private String message;
    @NotNull
    private Long sender;
    @NotNull
    private Long receiver;
}

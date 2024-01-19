package org.unibl.etf.models.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.models.entities.FitnessProgramEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FitnessProgramCommentDTO {
    private Long id;
    private String comment;
    private Date createdAt;
    private ClientInfoDTO sender;
    private Long fitnessProgramId;
    private Long fitnessProgramClientId;
}

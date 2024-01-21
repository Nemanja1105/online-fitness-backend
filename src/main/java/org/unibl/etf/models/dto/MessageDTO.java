package org.unibl.etf.models.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.entities.ClientEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private String message;
    private boolean seen;
    private Date createdAt;
    private ClientInfoDTO sender;
    private ClientInfoDTO receiver;
}

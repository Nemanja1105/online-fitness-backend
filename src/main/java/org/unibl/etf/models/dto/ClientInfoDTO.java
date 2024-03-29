package org.unibl.etf.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientInfoDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private Long profileImageId;
}

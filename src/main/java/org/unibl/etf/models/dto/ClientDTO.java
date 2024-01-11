package org.unibl.etf.models.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.unibl.etf.models.enums.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDTO {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String city;
    private String email;
    private String token;
}

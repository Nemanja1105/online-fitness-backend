package org.unibl.etf.models.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
    @NotBlank
    @Size(max=50)
    private String username;
    @NotBlank
    @Size(min=8)
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message="Password must contain at least 8 characters, of which at least one number and one capital letter!")
    private String password;
    @Email
    @Size(max=50)
    private String email;
    @NotBlank
    @Size(max=50)
    private String name;
    @NotBlank
    @Size(max=50)
    private String surname;
    @NotBlank
    @Size(max=50)
    private String city;
    private Long profileImageid;
}

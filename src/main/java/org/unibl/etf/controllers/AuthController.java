package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.services.AuthService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerClient(@Valid @RequestBody RegisterRequestDTO request){
        this.authService.registerClient(request);
    }

    @PostMapping("/login")
    public ClientDTO login(@Valid @RequestBody LoginRequestDTO requestDTO){
        return this.authService.loginClient(requestDTO);
    }

    @PostMapping("/check-details")
    public boolean checkDetails(@RequestBody CheckDetailsDTO checkDetailsDTO){
        return this.authService.checkDetails(checkDetailsDTO);
    }
    @PostMapping("/activate")
    public boolean activateAccount(@RequestBody ValidationDTO validationDTO){
        return this.authService.activateAccount(validationDTO);
    }
}

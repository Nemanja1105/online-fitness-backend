package org.unibl.etf.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.AlreadyExistsException;
import org.unibl.etf.exceptions.NotApprovedException;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.ClientDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.dto.LoginRequestDTO;
import org.unibl.etf.models.dto.RegisterRequestDTO;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.models.enums.Role;
import org.unibl.etf.repositories.ClientRepository;
import org.unibl.etf.services.AuthService;
import org.unibl.etf.services.JwtService;

@Service
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(ClientRepository clientRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public void registerClient(RegisterRequestDTO request) {
        if(clientRepository.existsByUsername(request.getUsername()))
            throw new AlreadyExistsException();
        var entity=mapper.map(request, ClientEntity.class);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole(Role.CLIENT);
        entity.setStatus(true);
        entity.setApprovalStatus(false);
        clientRepository.saveAndFlush(entity);
        //TODO VERIFIKACIJA
    }

    @Override
    public ClientDTO loginClient(LoginRequestDTO requestDTO) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        JwtUserDTO user = (JwtUserDTO) auth.getPrincipal();
        ClientDTO response=null;
        ClientEntity clientEntity=null;
        if(user !=null && user.getRole()==Role.CLIENT)
        {
            clientEntity=clientRepository.findById(user.getId()).get();
            response=mapper.map(clientEntity, ClientDTO.class);
        }
        else
            throw new UnauthorizedException();
        if(!clientEntity.isApprovalStatus())
            throw new NotApprovedException();//TODO PONOVO SLATI KOD
        var token=jwtService.generateToken(user);
        response.setToken(token);
        return response;
    }
}

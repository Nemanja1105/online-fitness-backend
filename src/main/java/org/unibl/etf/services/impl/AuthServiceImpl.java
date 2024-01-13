package org.unibl.etf.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.*;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.models.enums.Role;
import org.unibl.etf.repositories.ClientRepository;
import org.unibl.etf.repositories.ValidationTokenRepository;
import org.unibl.etf.services.AuthService;
import org.unibl.etf.services.EmailService;
import org.unibl.etf.services.JwtService;
import org.unibl.etf.services.ValidationTokenService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final ClientRepository clientRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationTokenService validationTokenService;
    private final EmailService emailService;

    private final ValidationTokenRepository validationTokenRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AuthServiceImpl(ClientRepository clientRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, ValidationTokenService validationTokenService, EmailService emailService, ValidationTokenRepository validationTokenRepository) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.validationTokenService = validationTokenService;
        this.emailService = emailService;
        this.validationTokenRepository = validationTokenRepository;
    }

    @Override
    public void registerClient(RegisterRequestDTO request) {
        if (clientRepository.existsByUsername(request.getUsername()))
            throw new AlreadyExistsException();
        var entity = mapper.map(request, ClientEntity.class);
        entity.setPassword(passwordEncoder.encode(request.getPassword()));
        entity.setRole(Role.CLIENT);
        entity.setStatus(true);
        entity.setApprovalStatus(false);
        entity = clientRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
        var token = validationTokenService.generateTokenForUser(entity);
        emailService.sendVerificationEmail(token.getToken(), entity.getEmail());
    }

    @Override
    public ClientDTO loginClient(LoginRequestDTO requestDTO) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        JwtUserDTO user = (JwtUserDTO) auth.getPrincipal();
        ClientDTO response = null;
        ClientEntity clientEntity = null;
        if (user != null && user.getRole() == Role.CLIENT) {
            clientEntity = clientRepository.findById(user.getId()).get();
            response = mapper.map(clientEntity, ClientDTO.class);
        } else
            throw new UnauthorizedException();
        if (!clientEntity.isStatus())
            throw new AccountBlockedException();
        if (!clientEntity.isApprovalStatus()) {
            resendActivation(clientEntity);
            throw new NotApprovedException();
        }
        var token = jwtService.generateToken(user);
        response.setToken(token);
        return response;
    }

    @Override
    public boolean checkDetails(CheckDetailsDTO checkDetailsDTO) {
        if (checkDetailsDTO.getColumn().equals("email") || checkDetailsDTO.getColumn().equals("clientEmail")) {
            return clientRepository.existsByEmail(checkDetailsDTO.getValue());
        } else if (checkDetailsDTO.getColumn().equals("username") || checkDetailsDTO.getColumn().equals("clientUsername")) {
            return clientRepository.existsByUsername(checkDetailsDTO.getValue());
        }
        return false;
    }

    @Override
    public boolean activateAccount(ValidationDTO validationDTO) {
        var token=validationTokenRepository.findByToken(validationDTO.getToken());
        if(token.isEmpty())
            return false;
        var user=token.get().getClient();
        user.setApprovalStatus(true);
        validationTokenRepository.delete(token.get());
        return true;
    }

    @Override
    public void resendActivation(ClientEntity client) {
        var token=validationTokenRepository.findByClientId(client.getId()).get();
        this.emailService.sendVerificationEmail(token.getToken(), client.getEmail());
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, Authentication authentication) {
        var user=clientRepository.findById(changePasswordDTO.getId()).orElseThrow(NotFoundException::new);
        var jwtUser=(JwtUserDTO)authentication.getPrincipal();
        if(!jwtUser.getId().equals(user.getId()))
            throw new UnauthorizedException();
        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword()))
            throw new PasswordMismatchException();
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
    }
}

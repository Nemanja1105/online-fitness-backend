package org.unibl.etf.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
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
import org.unibl.etf.services.*;

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
    private final LogService logService;

    private final HttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    public AuthServiceImpl(ClientRepository clientRepository, ModelMapper mapper, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, ValidationTokenService validationTokenService, EmailService emailService, ValidationTokenRepository validationTokenRepository, LogService logService, HttpServletRequest request) {
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.validationTokenService = validationTokenService;
        this.emailService = emailService;
        this.validationTokenRepository = validationTokenRepository;
        this.logService = logService;
        this.request = request;
    }

    @Override
    public void registerClient(RegisterRequestDTO request) {
        this.logService.info("Client "+this.request.getRemoteAddr()+" trying to register.");
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
        this.logService.info("New client "+entity.getUsername()+" successfully created.");
    }

    @Override
    public ClientDTO loginClient(LoginRequestDTO requestDTO) {
        this.logService.info("Client "+this.request.getRemoteAddr()+" trying to login.");
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getUsername(), requestDTO.getPassword()));
        JwtUserDTO user = (JwtUserDTO) auth.getPrincipal();
        ClientDTO response = null;
        ClientEntity clientEntity = null;
        if (user != null && user.getRole() == Role.CLIENT) {
            clientEntity = clientRepository.findById(user.getId()).get();
            response = mapper.map(clientEntity, ClientDTO.class);
        } else
            throw new UnauthorizedException();
        if (!clientEntity.isStatus()) {
            this.logService.warning("The client"+user.getUsername()+" is trying to login to a blocked account.");
            throw new AccountBlockedException();
        }
        if (!clientEntity.isApprovalStatus()) {
            resendActivation(clientEntity);
            this.logService.info("Client "+user.getUsername()+" tries to login, but the account is not verified.");
            throw new NotApprovedException();
        }
        var token = jwtService.generateToken(user);
        response.setToken(token);
        this.logService.info("Client "+user.getUsername()+" has successfully logged in to the application.");
        return response;
    }

    @Override
    public boolean checkDetails(CheckDetailsDTO checkDetailsDTO) {
       // this.logService.info("Client "+this.request.getRemoteAddr()+" trying to check email username availability.");
        if (checkDetailsDTO.getColumn().equals("email") || checkDetailsDTO.getColumn().equals("clientEmail")) {
            return clientRepository.existsByEmail(checkDetailsDTO.getValue());
        } else if (checkDetailsDTO.getColumn().equals("username") || checkDetailsDTO.getColumn().equals("clientUsername")) {
            return clientRepository.existsByUsername(checkDetailsDTO.getValue());
        }
        return false;
    }

    @Override
    public boolean activateAccount(ValidationDTO validationDTO) {
        this.logService.info("Client "+this.request.getRemoteAddr()+" trying to activate account.");
        var token=validationTokenRepository.findByToken(validationDTO.getToken());
        if(token.isEmpty()) {
            this.logService.warning("Client "+this.request.getRemoteAddr()+" failed to activate the account");
            return false;
        }
        var user=token.get().getClient();
        user.setApprovalStatus(true);
        validationTokenRepository.delete(token.get());
        this.logService.info("Client "+user.getUsername()+" successfully activated the account.");
        return true;
    }

    @Override
    public void resendActivation(ClientEntity client) {

        var token=validationTokenRepository.findByClientId(client.getId()).get();
        this.emailService.sendVerificationEmail(token.getToken(), client.getEmail());
        this.logService.info("Client "+client.getUsername()+" successfully finished sending the verification code to the email.");
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO, Authentication authentication) {
        this.logService.info("Client "+this.request.getRemoteAddr()+" trying to change password.");
        var user=clientRepository.findById(changePasswordDTO.getId()).orElseThrow(NotFoundException::new);
        var jwtUser=(JwtUserDTO)authentication.getPrincipal();
        if(!jwtUser.getId().equals(user.getId())) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        if(!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            this.logService.warning("Client "+jwtUser.getUsername()+"failed to change passwords. Passwords do not match");
            throw new PasswordMismatchException();
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        this.logService.info("Client "+jwtUser.getUsername()+" successfully changed the password.");
    }
}

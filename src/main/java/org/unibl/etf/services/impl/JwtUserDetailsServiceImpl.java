package org.unibl.etf.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.repositories.ClientRepository;
import org.unibl.etf.services.JwtUserDetailsService;

@Service
public class JwtUserDetailsServiceImpl implements JwtUserDetailsService {
    private final ModelMapper mapper;
    private final ClientRepository clientRepository;

    public JwtUserDetailsServiceImpl(ModelMapper mapper, ClientRepository clientRepository) {
        this.mapper = mapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        return mapper.map(this.clientRepository.findByUsername(username).orElseThrow(UnauthorizedException::new), JwtUserDTO.class);
    }
}

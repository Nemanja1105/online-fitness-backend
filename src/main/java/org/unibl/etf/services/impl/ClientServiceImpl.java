package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.ClientDTO;
import org.unibl.etf.models.dto.ClientInfoDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.dto.UpdateClientDTO;
import org.unibl.etf.models.entities.ImageEntity;
import org.unibl.etf.repositories.ClientRepository;
import org.unibl.etf.services.ClientService;
import org.unibl.etf.services.ImageService;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ImageService imageService;

    private final ModelMapper mapper;

    public ClientServiceImpl(ClientRepository clientRepository, ImageService imageService, ModelMapper mapper) {
        this.clientRepository = clientRepository;
        this.imageService = imageService;
        this.mapper = mapper;
    }

    @Override
    public ClientDTO updateClient(Long id, UpdateClientDTO request, Authentication auth) {
        var jwtUser=(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
            throw new UnauthorizedException();
        var client=this.clientRepository.findById(id).orElseThrow(NotFoundException::new);
        if(request.getName()!=null){
            client.setName(request.getName());
        }
        if(request.getSurname()!=null){
            client.setSurname(request.getSurname());
        }
        if(request.getCity()!=null){
            client.setCity(request.getCity());
        }
        if(request.getProfileImageId()!=null){
            if(client.getProfileImage()!=null) {
                try {
                    imageService.deleteImage(client.getProfileImage());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            ImageEntity image=new ImageEntity(); image.setId(request.getProfileImageId());
            client.setProfileImage(image);
        }
        return mapper.map(client,ClientDTO.class);
    }

    @Override
    public List<ClientInfoDTO> findAll() {
        return this.clientRepository.findAllByStatus(true).stream().map(el->mapper.map(el, ClientInfoDTO.class)).toList();
    }
}

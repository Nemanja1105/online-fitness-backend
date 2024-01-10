package org.unibl.etf.services;

import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.models.dto.ImageDTO;
import org.unibl.etf.models.entities.ImageEntity;

import java.io.IOException;

public interface ImageService {
    Long uploadImage(MultipartFile file)throws IOException;
    ImageDTO downloadImage(Long id)throws IOException;
    void deleteImage(ImageEntity image)throws IOException;
}

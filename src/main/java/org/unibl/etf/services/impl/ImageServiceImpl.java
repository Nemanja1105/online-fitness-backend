package org.unibl.etf.services.impl;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.models.dto.ImageDTO;
import org.unibl.etf.models.entities.ImageEntity;
import org.unibl.etf.repositories.ImageRepository;
import org.unibl.etf.services.ImageService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ModelMapper mapper;

    private File path;
    @PersistenceContext
    private EntityManager entityManager;

    public ImageServiceImpl(ImageRepository imageRepository, ModelMapper mapper) {
        this.imageRepository = imageRepository;
        this.mapper = mapper;
    }

    @PostConstruct
    public void initialize() throws IOException {
        ClassPathResource pdfPath = new ClassPathResource("");


        this.path =new File(pdfPath.getFile().getAbsolutePath() + File.separator + "images");
        if (!path.exists())
            path.mkdir();
    }

    @Override
    public Long uploadImage(MultipartFile file) throws IOException {
        var name = StringUtils.cleanPath(file.getOriginalFilename());
        var image = ImageEntity.builder().name(name).type(file.getContentType()).size(file.getSize()).build();
        imageRepository.saveAndFlush(image);
        entityManager.refresh(image);//dobio sam id od baze sada cuvamo na fajl sistemu
        Files.write(Path.of(getPath(image)), file.getBytes());
        return image.getId();
    }

    @Override
    public ImageDTO downloadImage(Long id) throws IOException {
        var image = imageRepository.findById(id).orElseThrow(NotFoundException::new);
        var path = getPath(image);
        var data = Files.readAllBytes(Path.of(path));
        var imageDto = mapper.map(image, ImageDTO.class);
        imageDto.setData(data);
        return imageDto;
    }


    @Override
    public void deleteImage(ImageEntity image) throws IOException {
        imageRepository.delete(image);
        var path = getPath(image);
        File file = new File(path);
        file.delete();
    }

    private String getPath(ImageEntity image) {
        var tmp = image.getType().split("/");
        var name = image.getId() + "." + tmp[1];
        var file = this.path + File.separator + name;
        System.out.println(file);
        return file;
    }

}

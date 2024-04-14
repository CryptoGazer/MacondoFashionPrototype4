package com.macondo_cs.MacondoFashionPrototype4.controllers;

import java.io.ByteArrayInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.macondo_cs.MacondoFashionPrototype4.models.Image;
import com.macondo_cs.MacondoFashionPrototype4.repo.ProductRepository;
import com.macondo_cs.MacondoFashionPrototype4.repo.ImageRepository;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageRepository imageRepository;

    private final ProductRepository productRepository;

    @RequestMapping(value = "/images/{id}", method = RequestMethod.GET)
    private ResponseEntity<?> getImageById(@PathVariable Long id) {
        Image image = imageRepository.findById(id).orElse(null);
        ResponseEntity<?> responseEntity = ResponseEntity.ok()
            .header("fileName", String.format(
                "%s_%s",
                productRepository.findByImage(image).get(0).getName(),
                image.getId()))
            .contentType(MediaType.valueOf(image.getContentType()))
            .contentLength(image.getSize())
            .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
        

        log.info(
            "\nIMG_INFO:\nOrigFileName: {}\nMediaType: {}\nSize: {}\n\n",
            image.getOriginalFileName(),
            MediaType.valueOf(image.getContentType()),
            image.getSize());
        log.info("\nRespEnt: {}\n", responseEntity);
        return responseEntity;
    }
}

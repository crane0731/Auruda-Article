package com.sw.AurudaArticle.controller;

import com.sw.AurudaArticle.dto.image.ImageResponseDto;
import com.sw.AurudaArticle.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auruda/article")
public class ImageUploadController {

    private final StorageService storageService;

    /**
     *
     * 이미지 업로드 API
     *
     * @Param image 업로들할 이미지 파일
     * @return 업로드된 이미지의 URL
     *
     *
     */

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("image")MultipartFile image){
        System.out.println("asd");
        try{
            String imageUrl = storageService.uploadFile(image);
            return ResponseEntity.ok().body(new ImageResponseDto(imageUrl));
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }


}

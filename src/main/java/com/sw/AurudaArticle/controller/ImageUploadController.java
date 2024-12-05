package com.sw.AurudaArticle.controller;

import com.sw.AurudaArticle.dto.image.DeleteImageRequestDto;
import com.sw.AurudaArticle.dto.image.ImageResponseDto;
import com.sw.AurudaArticle.service.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    //이미지를 저장하고 url을 반환하는 컨트롤러
    @PostMapping("/image")
    public ResponseEntity<Object> uploadImage(@RequestParam("image")MultipartFile image){
        try{
            String imageUrl = storageService.uploadFile(image);
            return ResponseEntity.ok().body(new ImageResponseDto(imageUrl));
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    //url을 받아 이미지 파일 삭제하는 컨트롤러

    @DeleteMapping("/image")
    public ResponseEntity<Object> deleteImage(@RequestBody DeleteImageRequestDto deleteImageRequestDto){

        List<String> imageUrls = deleteImageRequestDto.getImageUrls();

        for (String imageUrl : imageUrls) {
            //삭제 메서드
        }

    }


}

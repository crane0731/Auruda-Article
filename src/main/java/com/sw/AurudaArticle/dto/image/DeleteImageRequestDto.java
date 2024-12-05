package com.sw.AurudaArticle.dto.image;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteImageRequestDto {
    private List<String> ImageUrls;
}

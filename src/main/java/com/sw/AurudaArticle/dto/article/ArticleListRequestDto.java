package com.sw.AurudaArticle.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

//게시글 리스트 조회를 위한 요청 DTO

@Getter
public class ArticleListRequestDto {

    private String articleType;

    private String sortingType;
}

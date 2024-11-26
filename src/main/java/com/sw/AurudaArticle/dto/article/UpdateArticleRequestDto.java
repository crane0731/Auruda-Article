package com.sw.AurudaArticle.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateArticleRequestDto {
    @NotBlank(message = "제목을 입력하세요.")
    private String title;
    @NotBlank(message = "내용을 입력하세요.")
    private String content;
    @NotBlank(message = "타입을 입력하세요.")
    private String type;
}

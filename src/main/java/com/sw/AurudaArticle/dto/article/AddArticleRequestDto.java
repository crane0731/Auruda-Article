package com.sw.AurudaArticle.dto.article;

import jakarta.validation.constraints.NotBlank;

//게시글을 등록하기 위한 요청 DTO
public class AddArticleRequestDto {
    @NotBlank(message = "제목을 입력하세요.")
    public String title;
    @NotBlank(message = "내용을 입력하세요.")
    public String content;
    @NotBlank(message = "타입을 입력하세요.")
    public String type;

    public Long travelPlanId;


}

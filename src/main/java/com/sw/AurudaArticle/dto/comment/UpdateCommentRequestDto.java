package com.sw.AurudaArticle.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateCommentRequestDto {
    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}

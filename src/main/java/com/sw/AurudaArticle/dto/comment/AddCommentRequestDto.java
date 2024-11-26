package com.sw.AurudaArticle.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddCommentRequestDto {
    @NotNull(message = "게시글 아이디를 입력하세요.")
    private Long articleId;
    @NotBlank(message = "내용을 입력하세요.")
    private String content;

}

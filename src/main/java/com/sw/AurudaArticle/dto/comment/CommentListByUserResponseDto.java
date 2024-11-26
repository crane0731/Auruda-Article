package com.sw.AurudaArticle.dto.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentListByUserResponseDto {
    private Long commentId;
    private Long articleId;
    private Long userId;
    private String userNickname;
    private String userEmail;
    private String content;
}

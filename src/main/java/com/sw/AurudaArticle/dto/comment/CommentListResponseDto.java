package com.sw.AurudaArticle.dto.comment;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentListResponseDto {
    private Long commentId;
    private Long articleId;
    private Long userId;
    private String userNickname;
    private String userEmail;
    private String content;
    private List<CommentListResponseDto> replies;

}

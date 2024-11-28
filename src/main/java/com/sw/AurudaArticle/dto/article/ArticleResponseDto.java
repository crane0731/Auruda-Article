package com.sw.AurudaArticle.dto.article;

import com.sw.AurudaArticle.domain.Comment;
import com.sw.AurudaArticle.dto.comment.CommentListResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ArticleResponseDto {
    private Long articleId;
    private String articleType;
    private String title;
    private String content;
    private Long recommendation;
    private Long count;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userGrade;
    private Long travelPlanId;
    private List<CommentListResponseDto> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

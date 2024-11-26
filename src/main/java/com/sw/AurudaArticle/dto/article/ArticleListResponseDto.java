package com.sw.AurudaArticle.dto.article;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ArticleListResponseDto {
    private Long articleId;
    private String title;
    private Long recommendation;
    private Long userId;
    private String userName;
    private String userEmail;
    private String userGrade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

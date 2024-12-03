package com.sw.AurudaArticle.controller;


import com.sw.AurudaArticle.domain.Article;
import com.sw.AurudaArticle.domain.Comment;
import com.sw.AurudaArticle.dto.article.*;
import com.sw.AurudaArticle.dto.comment.CommentListResponseDto;
import com.sw.AurudaArticle.service.ArticleService;
import com.sw.AurudaArticle.service.CommentService;
import com.sw.AurudaArticle.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auruda/article")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;
    private final CommentService commentService;

    //게시물 등록
    @PostMapping("/new")
    public ResponseEntity<Object> createArticle(@RequestHeader("User-Id")Long userId, @Valid @RequestBody AddArticleRequestDto requestDto, BindingResult bindingResult) {
        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();
        //유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
        }

        //오류 메시지가 존재하면 이를 반환
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body(errorMessages);
        }


        articleService.addArticle(userId, requestDto);
        return ResponseEntity.ok("게시물 등록 성공");
    }

    //게시물 삭제
    @DeleteMapping("/{article_id}")
    public ResponseEntity<String> deleteArticle(@PathVariable("article_id") Long articleId) {
        articleService.deleteArticle(articleId);
        return ResponseEntity.ok("게시물 삭제 성공");
    }


    //게시물 타입별 조회:
    ////FREE_BOARD("자유 게시판"),
    ////TRAVEL_REVIEW("여행후기"),
    ////fIND_COMPANION("투게더"),
    ////TRAVEL_RATING("여행평가");

    //1 최근 순서대로 조회:
    //2 오래된 순서대로 조회:
    //3 추천수대로 조회:
    //4 조회수대로 조회:

    //게시물 조회(리스트)
    @PostMapping
    public ResponseEntity<List<ArticleListResponseDto>> getArticleList(@RequestBody ArticleListRequestDto requestDto) {

        List<Article> allArticles = articleService.findAllArticles(requestDto);

        List<ArticleListResponseDto> articleListResponse = createArticleListResponse(allArticles);

        return ResponseEntity.ok(articleListResponse);
    }


    //자신이 쓴 게시물 조회(리스트)
    @GetMapping("/me")
    public ResponseEntity<List<ArticleListResponseDto>> getArticlesByUserId(@RequestHeader("User-Id") Long userId) {
        List<Article> articles = articleService.findArticleByUserId(userId);

        List<ArticleListResponseDto> articleListResponseDto = createArticleListResponseDto(articles);
        return ResponseEntity.ok(articleListResponseDto);
    }

    //추천수 늘리기
    @GetMapping("/recommendation/{article_id}")
    public ResponseEntity<String> plusRecommendation(@PathVariable("article_id") Long articleId) {
        articleService.upRecommendation(articleId);
        Article article = articleService.findArticleById(articleId);
        userService.upPoint(article.getUser().getId());

        return ResponseEntity.ok("게시물 추천 성공");
    }

    //게시물 조회(상세)
    @GetMapping("/{article_id}")
    public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable("article_id") Long articleId) {
        Article article = articleService.findArticleById(articleId);
        List<Comment> comments = commentService.findComments(articleId);

        //조회수 1증가 시키기
        articleService.upCount(articleId);


        List<CommentListResponseDto> commentListResponseDtos = comments.stream().map(this::createCommentListResponseDto).toList();
        ArticleResponseDto articleResponseDto = createArticleResponseDto(article, commentListResponseDtos);

        return ResponseEntity.ok(articleResponseDto);
    }

    //게시글 수정
    @PutMapping("/{article_id}")
    public ResponseEntity<Object>updateArticle(@PathVariable("article_id") Long articleId, @Valid @RequestBody UpdateArticleRequestDto requestDto,BindingResult bindingResult) {

        // 오류 메시지를 담을 Map
        Map<String, String> errorMessages = new HashMap<>();

        //유효성 검사에서 오류가 발생한 경우 모든 메시지를 Map에 추가
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error ->
                    errorMessages.put(error.getField(), error.getDefaultMessage())
            );
        }

        //오류 메시지가 존재하면 이를 반환
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.badRequest().body(errorMessages);
        }

        articleService.updateArticle(articleId,requestDto);
        return ResponseEntity.ok("게시물 수정 성공");
    }


    //dto 생성 메서드
    private List<ArticleListResponseDto> createArticleListResponseDto(List<Article> articles) {
        return articles.stream().map(article -> ArticleListResponseDto.builder()
                .articleId(article.getId())
                .userId(article.getUser().getId())
                .userName(article.getUser().getNickname())
                .userEmail(article.getUser().getEmail())
                .userGrade(String.valueOf(article.getUser().getGrade()))
                .title(article.getTitle())
                .recommendation(article.getRecommendation())
                .count(article.getCount())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build()).toList();
    }

    private List<ArticleListResponseDto> createArticleListResponse(List<Article> allArticles) {
        return allArticles.stream()
                .map(article->ArticleListResponseDto.builder()
                        .articleId(article.getId())
                        .title(article.getTitle())
                        .recommendation(article.getRecommendation())
                        .count(article.getCount())
                        .userId(article.getUser().getId())
                        .userName(article.getUser().getNickname())
                        .userEmail(article.getUser().getEmail())
                        .userGrade(article.getUser().getGrade().getComment())
                        .createdAt(article.getCreatedAt())
                        .updatedAt(article.getUpdatedAt())
                        .build()
                ).collect(Collectors.toList());
    }

    // Comment 엔티티를 CommentListResponseDto로 변환하는 메서드
    private CommentListResponseDto createCommentListResponseDto(Comment comment) {
        return CommentListResponseDto.builder()
                .commentId(comment.getId())
                .articleId(comment.getArticle().getId())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .userEmail(comment.getUser().getEmail())
                .content(comment.getContent())
                .replies(Optional.ofNullable(comment.getReplies()) // null-safe 처리
                        .orElse(List.of()) // null일 경우 빈 리스트로 처리
                        .stream()
                        .map(this::createCommentListResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    //dto 생성 메서드
    private ArticleResponseDto createArticleResponseDto(Article article, List<CommentListResponseDto> comments) {
        return  ArticleResponseDto.builder()
                .articleId(article.getId())
                .articleType(String.valueOf(article.getArticleType()))
                .title(article.getTitle())
                .content(article.getContent())
                .recommendation(article.getRecommendation())
                .count(article.getCount())
                .userId(article.getUser().getId())
                .userName(article.getUser().getNickname())
                .userEmail(article.getUser().getEmail())
                .userGrade(article.getUser().getGrade().getComment())
                .comments(comments)
                .travelPlanId(article.getTravelPlanId())
                .createdAt(article.getCreatedAt())
                .updatedAt(article.getUpdatedAt())
                .build();
    }


}

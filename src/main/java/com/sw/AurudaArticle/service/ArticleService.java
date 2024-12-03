package com.sw.AurudaArticle.service;


import com.sw.AurudaArticle.domain.Article;
import com.sw.AurudaArticle.domain.ArticleType;
import com.sw.AurudaArticle.domain.Comment;
import com.sw.AurudaArticle.domain.User;
import com.sw.AurudaArticle.dto.article.*;
import com.sw.AurudaArticle.dto.comment.CommentListResponseDto;
import com.sw.AurudaArticle.exception.CustomException;
import com.sw.AurudaArticle.exception.ErrorCode;
import com.sw.AurudaArticle.exception.ErrorMessage;
import com.sw.AurudaArticle.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final CommentService commentService;

    //게시글 찾기
    public Article findArticleById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_ARTICLE));
    }


    @Transactional
    //게시물 저장
    public void addArticle(Long userId,AddArticleRequestDto dto) {
        //유저 찾기
        User user = userService.findUser(userId);

        //게시글 생성
        Article article = Article.builder()
                .user(user)
                .title(dto.title)
                .content(dto.content)
                .articleType(ArticleType.valueOf(dto.type))
                .recommendation(0L)
                .count(0L)
                .travelPlanId(dto.travelPlanId)
                .build();

        //게시글 저장
        articleRepository.save(article);
    }

    @Transactional
    //게시물 삭제
    public void deleteArticle(Long articleId) {
        //먼저 삭제할 게시글이 있나 조회
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND, ErrorMessage.NO_ARTICLE));
        List<Comment> comments = commentService.findCommentsByUserId(article.getUser().getId());

        article.deleteAllComments();
        articleRepository.deleteById(articleId);
    }


    //게시물 조회(리스트)
    public List<Article> findAllArticles(ArticleListRequestDto dto) {

            if(dto.getSortingType()==null){
                throw new CustomException(ErrorCode.INVALID_REQUEST,ErrorMessage.NO_SORTING_TYPE);
            }

            //게시물 타입별 조회
            //1번이면 최근 순서대로 조회
            if(dto.getSortingType().equals("1")){
                return articleRepository.findAllByArticleTypeOrderByLatest(ArticleType.valueOf(dto.getArticleType()));
            }
            //2번이면 오래된 순서대로 조회
            else if (dto.getSortingType().equals("2")) {
                return articleRepository.findAllByArticleTypeOrderByEarliest(ArticleType.valueOf(dto.getArticleType()));
            }
            //3번이면 추천수대로 조회
            else if (dto.getSortingType().equals("3")) {
                return articleRepository.findAllByArticleTypeOrderByRecommendationDesc(ArticleType.valueOf(dto.getArticleType()));
            }
            //4번이면 조회수 대로 조회
            else if(dto.getSortingType().equals("4")) {
                return articleRepository.findAllByArticleTypeOrderByCountDesc(ArticleType.valueOf(dto.getArticleType()));
            }
            else{
                throw new CustomException(ErrorCode.INVALID_REQUEST,ErrorMessage.INVALID_SORTING_TYPE);
            }
    }

    //유저별 게시글 조회
    public List<Article> findArticleByUserId(Long userId) {
        return articleRepository.findByUserId(userId);
    }

    //추천수 증가
    @Transactional
    public void upRecommendation(Long articleId) {
        Article article = findArticleById(articleId);
        article.addRecommendation();
    }

    //조회수 증가
    @Transactional
    public void upCount(Long articleId) {
        Article article = findArticleById(articleId);
        article.addCount();
    }

    //게시글 수정
    @Transactional
    public void updateArticle(Long articleId, UpdateArticleRequestDto dto) {
        Article article = findArticleById(articleId);
        article.update(dto.getTitle(), dto.getContent(), dto.getType());
    }


}

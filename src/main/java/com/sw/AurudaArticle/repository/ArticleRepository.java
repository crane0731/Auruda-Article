package com.sw.AurudaArticle.repository;

import com.sw.AurudaArticle.domain.Article;
import com.sw.AurudaArticle.domain.ArticleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {


    // ArticleType별로 recommendation 값을 기준으로 내림차순 정렬된 리스트 반환
    List<Article> findAllByArticleTypeOrderByRecommendationDesc(ArticleType articleType);

    //ArticleType별로 조회수가 높은 순서대로 정렬된 리스트 반환
    List<Article> findAllByArticleTypeOrderByCountDesc(ArticleType articleType);

    // ArticleType별로 최신순 정렬된 리스트 반환
    @Query("SELECT a FROM Article a WHERE a.articleType = :articleType ORDER BY COALESCE(a.updatedAt, a.createdAt) DESC")
    List<Article> findAllByArticleTypeOrderByLatest(ArticleType articleType);

    // ArticleType별로 빠른 순서대로 정렬된 리스트 반환
    @Query("SELECT a FROM Article a WHERE a.articleType = :articleType ORDER BY COALESCE(a.updatedAt, a.createdAt) ASC")
    List<Article> findAllByArticleTypeOrderByEarliest(ArticleType articleType);




    //userId로 게시글 찾기
    List<Article> findByUserId(Long userId);

}

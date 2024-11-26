package com.sw.AurudaArticle.repository;

import com.sw.AurudaArticle.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleIdAndParentIsNull(Long articleId); // 부모가 없는 댓글(헤드 댓글)만 조회

    List<Comment> findByUserId(Long userId);
}

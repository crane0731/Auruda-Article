package com.sw.AurudaArticle.service;

import com.sw.AurudaArticle.domain.Article;
import com.sw.AurudaArticle.domain.Comment;
import com.sw.AurudaArticle.domain.User;
import com.sw.AurudaArticle.dto.comment.AddCommentRequestDto;
import com.sw.AurudaArticle.dto.comment.UpdateCommentRequestDto;
import com.sw.AurudaArticle.exception.CustomException;
import com.sw.AurudaArticle.exception.ErrorCode;
import com.sw.AurudaArticle.exception.ErrorMessage;
import com.sw.AurudaArticle.repository.ArticleRepository;
import com.sw.AurudaArticle.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleRepository articleRepository;

    //댓글 찾기
    public Comment findCommentById(long id) {
        return commentRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND,ErrorMessage.NO_COMMENT));
    }

    //댓글 등록
    @Transactional
    public void saveComment(Long userId, AddCommentRequestDto dto){
        //유저 찾기
        User user = userService.findUser(userId);

        //게시글 찾기
        Article article = articleRepository.findById(dto.getArticleId()).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND,ErrorMessage.NO_ARTICLE));

        Comment comment = Comment.builder()
                .user(user)
                .article(article)
                .content(dto.getContent())
                .build();

        article.addComment(comment);
        commentRepository.save(comment);
    }
    //대댓글 등록
    @Transactional
    public void saveReply(Long userId,Long commentId, AddCommentRequestDto dto){
        //유저 찾기
        User user = userService.findUser(userId);

        //게시글 찾기
        Article article = articleRepository.findById(dto.getArticleId()).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND,ErrorMessage.NO_ARTICLE));

        //부모 댓글 찾기
        Comment parentComment = findCommentById(commentId);

        //자식 댓글 생성
        Comment comment = Comment.builder()
                .user(user)
                .article(article)
                .parent(parentComment)
                .content(dto.getContent())
                .build();

        commentRepository.save(comment);

        parentComment.addReply(comment);
    }

    //댓글 삭제
    @Transactional
    public void removeComment(Long commentId){
        Comment comment = findCommentById(commentId);
        comment.deleteAllReplies();
        commentRepository.delete(comment);
    }
    //댓글 조회(리스트)
    public List<Comment> findComments(Long articleId){

        //게시글에 대한 댓글 찾기
        return  commentRepository.findByArticleIdAndParentIsNull(articleId);


    }

    //유저별로 댓글 조회(리스트)
    public List<Comment> findCommentsByUserId(Long userId){
        return commentRepository.findByUserId(userId);
    }


    //댓글 수정
    @Transactional
    public void updateComment(Long commentId, UpdateCommentRequestDto dto){
        Comment comment = findCommentById(commentId);
        comment.update(dto.getContent());

    }
}

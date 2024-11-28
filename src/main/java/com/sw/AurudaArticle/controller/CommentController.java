package com.sw.AurudaArticle.controller;

import com.sw.AurudaArticle.domain.Comment;
import com.sw.AurudaArticle.dto.comment.AddCommentRequestDto;
import com.sw.AurudaArticle.dto.comment.CommentListByUserResponseDto;
import com.sw.AurudaArticle.dto.comment.CommentListResponseDto;
import com.sw.AurudaArticle.dto.comment.UpdateCommentRequestDto;
import com.sw.AurudaArticle.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/auruda/comment")
public class CommentController {

    private final CommentService commentService;

    //댓글 등록
    @PostMapping
    public ResponseEntity<Object> createComment(@RequestHeader("User-Id")Long userId, @Valid @RequestBody AddCommentRequestDto requestDto, BindingResult bindingResult) {

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
        commentService.saveComment(userId, requestDto);
        return ResponseEntity.ok("댓글 등록 성공");
    }

    //대댓글 등록
    @PostMapping("/{comment_id}")
    public ResponseEntity<Object>createReplyComment(@RequestHeader("User-Id")Long userId,@PathVariable("comment_id") Long commentId, @Valid @RequestBody AddCommentRequestDto requestDto,BindingResult bindingResult) {
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
        commentService.saveReply(userId, commentId, requestDto);
        return ResponseEntity.ok("댓글 등록 성공");
    }

    //댓글 삭제
    @DeleteMapping("/{comment_id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment_id") Long commentId){
        commentService.removeComment(commentId);
        return ResponseEntity.ok("댓글 삭제 성공");
    }

    //댓글 조회(리스트)
    @GetMapping("/list/{article_id}")
    public ResponseEntity<List<CommentListResponseDto>> getCommentList(@PathVariable("article_id") Long articleId){
        List<Comment> comments = commentService.findComments(articleId);
        List<CommentListResponseDto> commentListResponseDtoList = comments.stream().map(this::createCommentListResponseDto).toList();
        return ResponseEntity.ok().body(commentListResponseDtoList);
    }

    //자신이 쓴 댓글 조회
    @GetMapping("/me")
    public ResponseEntity<List<CommentListByUserResponseDto>> getCommentListByUserId(@RequestHeader("User-Id") Long userId){
        List<Comment> comments = commentService.findCommentsByUserId(userId);

        List<CommentListByUserResponseDto> commentListByUserResponseDto = createCommentListByUserResponseDto(comments);
        return ResponseEntity.ok().body(commentListByUserResponseDto);
    }

    //댓글 수정
    @PutMapping("/{comment_id}")
    public ResponseEntity<Object> updateComment(@PathVariable("comment_id") Long commentId, @Valid@RequestBody UpdateCommentRequestDto requestDto,BindingResult bindingResult){
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
        commentService.updateComment(commentId, requestDto);
        return ResponseEntity.ok("댓글 수정 성공");
    }

    //dto 생성 메서드
    private List<CommentListByUserResponseDto> createCommentListByUserResponseDto(List<Comment> comments) {
        return comments.stream().map(comment -> CommentListByUserResponseDto.builder()
                .articleId(comment.getArticle().getId())
                .commentId(comment.getId())
                .userId(comment.getUser().getId())
                .userNickname(comment.getUser().getNickname())
                .userEmail(comment.getUser().getEmail())
                .content(comment.getContent())
                .build()).toList();
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






}

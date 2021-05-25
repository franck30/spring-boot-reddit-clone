package com.franckcarin.redditclone.controller;

import com.franckcarin.redditclone.dto.CommentsDto;
import com.franckcarin.redditclone.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

  private final CommentService commentService;

  @PostMapping
  public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {

    commentService.save(commentsDto);
    return new ResponseEntity<>(CREATED);
  }


  @GetMapping("/by-post/{postId}")
  public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
    return ResponseEntity.status(OK).body(commentService.getAllCommentsForPost(postId));
  }

  @GetMapping("/by-user/{userName}")
  public ResponseEntity<List<CommentsDto>> getAllCommentForUser(@PathVariable String userName) {

    return ResponseEntity.status(OK).body(commentService.getAllCommentsForUser(userName));

  }
}

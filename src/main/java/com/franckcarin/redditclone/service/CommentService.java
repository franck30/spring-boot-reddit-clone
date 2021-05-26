package com.franckcarin.redditclone.service;


import com.franckcarin.redditclone.dto.CommentsDto;
import com.franckcarin.redditclone.exception.PostNotFoundException;
import com.franckcarin.redditclone.mapper.CommentMapper;
import com.franckcarin.redditclone.model.Comment;
import com.franckcarin.redditclone.model.NotificationEmail;
import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.User;
import com.franckcarin.redditclone.repository.CommentRepository;
import com.franckcarin.redditclone.repository.PostRepository;
import com.franckcarin.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CommentService {

  private static final String POST_URL = "";

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final AuthService authService;
  private final CommentMapper commentMapper;
  private final CommentRepository commentRepository;
  private final MailContentBuilder mailContentBuilder;
  private final MailService mailService;

  public void save(CommentsDto commentsDto) {

    Post post = postRepository.findById(commentsDto.getPostId())
            .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));

    Comment comment = commentMapper.map(commentsDto, post, authService.getCurrentUser());

    commentRepository.save(comment);

    System.out.println("user" + comment.getUser());
    System.out.println("username" + comment.getUser().getUsername());

    String message = mailContentBuilder.build(comment.getUser().getUsername() + " posted a comment on your post." + POST_URL);

    sendCommentNotification(message, post.getUser());
  }

  private void sendCommentNotification(String message, User user) {

      mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your post", user.getEmail(), message));
  }

  public List<CommentsDto> getAllCommentsForPost(Long postId) {

    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(postId.toString()));
    return commentRepository.findByPost(post).stream().map(commentMapper::mapToDto).collect(toList());
  }

  public List<CommentsDto> getAllCommentsForUser(String userName) {
    User user = userRepository.findByUsername(userName)
            .orElseThrow(() -> new UsernameNotFoundException(userName));

    return commentRepository.findAllByUser(user)
            .stream()
            .map(commentMapper::mapToDto)
            .collect(toList());
  }
}

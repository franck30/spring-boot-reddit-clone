package com.franckcarin.redditclone.service;


import com.franckcarin.redditclone.dto.CommentsDto;
import com.franckcarin.redditclone.exception.PostNotFoundException;
import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.repository.PostRepository;
import com.franckcarin.redditclone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {


  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public void save(CommentsDto commentsDto) {

    Post post = postRepository.findById(commentsDto.getPostId())
            .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
  }
}

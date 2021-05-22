package com.franckcarin.redditclone.service;


import com.franckcarin.redditclone.dto.PostRequest;
import com.franckcarin.redditclone.dto.PostResponse;
import com.franckcarin.redditclone.exception.PostNotFoundException;
import com.franckcarin.redditclone.exception.SubredditNotFoundException;
import com.franckcarin.redditclone.mapper.PostMapper;
import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.Subreddit;
import com.franckcarin.redditclone.model.User;
import com.franckcarin.redditclone.repository.PostRepository;
import com.franckcarin.redditclone.repository.SubredditRepository;
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
public class PostService {

  private final SubredditRepository subredditRepository;
  private final AuthService authService;
  private final PostMapper postMapper;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public void save(PostRequest postRequest) {
    Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
            .orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));




    User currentUser = authService.getCurrentUser();
    log.info(currentUser.getUsername());
    log.info(subreddit.getName());
    System.out.println(currentUser.getUsername());

    postRepository.save(postMapper.map(postRequest, subreddit, currentUser)) ;
  }

  @Transactional(readOnly = true)
  public PostResponse getPost(Long id) {
    Post post = postRepository.findById(id)
            .orElseThrow(() -> new PostNotFoundException(id.toString()));
    return postMapper.mapToDto(post);
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getAllPosts() {
    return postRepository.findAll()
            .stream()
            .map(postMapper::mapToDto)
            .collect(toList());
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getPostsBySubreddit(Long subredditId) {
    Subreddit subreddit = subredditRepository.findById(subredditId)
            .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
    List<Post> posts = postRepository.findAllBySubreddit(subreddit);
    return posts.stream().map(postMapper::mapToDto).collect(toList());
  }

  @Transactional(readOnly = true)
  public List<PostResponse> getPostsByUsername(String username) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    return postRepository.findByUser(user)
            .stream()
            .map(postMapper::mapToDto)
            .collect(toList());
  }

}

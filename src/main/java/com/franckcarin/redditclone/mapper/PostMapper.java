package com.franckcarin.redditclone.mapper;


import com.franckcarin.redditclone.dto.PostRequest;
import com.franckcarin.redditclone.dto.PostResponse;
import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.Subreddit;
import com.franckcarin.redditclone.model.User;
import com.franckcarin.redditclone.repository.CommentRepository;
import com.franckcarin.redditclone.repository.VoteRepository;
import com.franckcarin.redditclone.service.AuthService;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {


  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private VoteRepository voteRepository;

  @Autowired
  private AuthService authService;

  @Mappings({
          @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
          @Mapping(target = "description", source = "postRequest.description"),
          @Mapping(target = "subreddit", source = "subreddit"),
          @Mapping(target = "voteCount", constant = "0"),
          @Mapping(target = "user", source = "user")
  })
  public abstract Post map(PostRequest postRequest, Subreddit subreddit, User user);

  @Mappings({
          @Mapping(target = "id", source = "post_id"),
          @Mapping(target = "subredditName", source = "subreddit.name"),
          @Mapping(target = "userName", source = "user.username"),
          @Mapping(target = "commentCount", expression = "java(commentCount(post))"),
          @Mapping(target = "duration", expression = "java(getDuration(post))")
  })
  public abstract PostResponse mapToDto(Post post);

  Integer commentCount(Post post) {
    return commentRepository.findByPost(post).size();
  }

  String getDuration(Post post) {
    return TimeAgo.using(post.getCreatedDate().toEpochMilli());
  }
}

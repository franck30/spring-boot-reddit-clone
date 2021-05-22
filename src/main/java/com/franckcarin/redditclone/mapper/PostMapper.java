package com.franckcarin.redditclone.mapper;


import com.franckcarin.redditclone.dto.PostRequest;
import com.franckcarin.redditclone.dto.PostResponse;
import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.Subreddit;
import com.franckcarin.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mappings({
          @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
          @Mapping(target = "description", source = "postRequest.description"),
          @Mapping(target = "subreddit", source = "subreddit"),
          @Mapping(target = "user", source = "user")
  })

  Post map(PostRequest postRequest, Subreddit subreddit, User user);

  @Mappings({
          @Mapping(target = "id", source = "post_id"),
          @Mapping(target = "subredditName", source = "subreddit.name"),
          @Mapping(target = "userName", source = "user.username")
  })

  PostResponse mapToDto(Post post);
}

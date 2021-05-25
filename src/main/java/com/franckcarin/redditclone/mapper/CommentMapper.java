package com.franckcarin.redditclone.mapper;


import com.franckcarin.redditclone.dto.CommentsDto;
import com.franckcarin.redditclone.model.Comment;
import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "text", source = "commentsDto.text")
  @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
  @Mapping(target = "post", source = "post")
  @Mapping(target = "user" ,source = "user")
  Comment map(CommentsDto commentsDto, Post post, User user);

  @Mapping(target = "postId", expression = "java(comment.getPost().getPost_id())")
  @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
  CommentsDto mapToDto(Comment comment);
}

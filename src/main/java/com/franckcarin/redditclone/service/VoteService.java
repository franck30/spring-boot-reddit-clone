package com.franckcarin.redditclone.service;

import com.franckcarin.redditclone.dto.VoteDto;
import com.franckcarin.redditclone.exception.PostNotFoundException;
import com.franckcarin.redditclone.exception.SpringRedditException;
import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.Vote;
import com.franckcarin.redditclone.repository.PostRepository;
import com.franckcarin.redditclone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.franckcarin.redditclone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

  private final VoteRepository voteRepository;
  private final PostRepository postRepository;
  private final AuthService authService;

  @Transactional
  public void vote(VoteDto voteDto) {
    Post post = postRepository.findById(voteDto.getPostId())
            .orElseThrow(() -> new PostNotFoundException("Post Not Found with ID - " + voteDto.getPostId()));
    Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByIdDesc(post, authService.getCurrentUser());
    if (voteByPostAndUser.isPresent() &&
            voteByPostAndUser.get().getVoteType()
                    .equals(voteDto.getVoteType())) {
      throw new SpringRedditException("You have already "
              + voteDto.getVoteType() + "'d for this post");
    }
    if (UPVOTE.equals(voteDto.getVoteType())) {
      post.setVoteCount(post.getVoteCount() + 1);
    } else {
      post.setVoteCount(post.getVoteCount() - 1);
    }
    voteRepository.save(mapToVote(voteDto, post));
    postRepository.save(post);
  }

  private Vote mapToVote(VoteDto voteDto, Post post) {
    return Vote.builder()
            .voteType(voteDto.getVoteType())
            .post(post)
            .user(authService.getCurrentUser())
            .build();
  }
}
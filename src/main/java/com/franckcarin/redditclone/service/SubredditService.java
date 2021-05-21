package com.franckcarin.redditclone.service;


import com.franckcarin.redditclone.dto.SubredditDto;
import com.franckcarin.redditclone.exception.SpringRedditException;
import com.franckcarin.redditclone.mapper.SubredditMapper;
import com.franckcarin.redditclone.model.Subreddit;
import com.franckcarin.redditclone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {


  private final SubredditRepository subredditRepository;

  private final SubredditMapper subredditMapper;

  @Transactional
  public Subreddit save(SubredditDto subredditDto) {
    Subreddit save = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
    return save;
  }

//  private Subreddit mapSubredditDto(SubredditDto subredditDto) {
//
//    return Subreddit.builder().name(subredditDto.getName())
//            .description(subredditDto.getDescription())
//            .build();
//  }

  @Transactional(readOnly = true)
  public List<SubredditDto> getAll() {
      return subredditRepository.findAll()
              .stream()
              .map(subredditMapper::mapSubredditToDto)
              .collect(toList());
  }

  public SubredditDto getSubreddit(Long id) {
    Subreddit subreddit = subredditRepository.findById(id)
            .orElseThrow(() -> new SpringRedditException("No subreddit found with this id"));

    return subredditMapper.mapSubredditToDto(subreddit);
  }

//  private SubredditDto mapToDto(Subreddit subreddit) {
//    return SubredditDto.builder().name(subreddit.getName())
//            .id(subreddit.getId())
//            .numberOfPosts(subreddit.getPosts().size())
//            .build();
//  }
}

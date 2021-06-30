package com.franckcarin.redditclone.repository;

import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.User;
import com.franckcarin.redditclone.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

  Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);

//  Optional<Vote> findTopByPostAndUserOrderByIdDesc(Post post, User currentUser);
}

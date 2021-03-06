package com.franckcarin.redditclone.repository;

import com.franckcarin.redditclone.model.Subreddit;
import com.franckcarin.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubredditRepository extends JpaRepository<Subreddit, Long> {

  Optional<Subreddit> findByName(String name);
}

package com.franckcarin.redditclone.repository;

import com.franckcarin.redditclone.model.Post;
import com.franckcarin.redditclone.model.Subreddit;
import com.franckcarin.redditclone.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>{

  List<Post> findAllBySubreddit(Subreddit subreddit);

  List<Post> findByUser(User user);
}

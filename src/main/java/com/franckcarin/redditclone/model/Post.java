package com.franckcarin.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long post_id;
  @NotBlank(message = "Post Name cannot be empty or Null")
  private String postName;
  @Nullable
  private String url;
  @Nullable
  @Lob
  private String description;
  private Integer voteCount = 0;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;

  @Column(name = "created_date")
  private Instant createdDate;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "id", referencedColumnName = "id")
  private Subreddit subreddit;
}

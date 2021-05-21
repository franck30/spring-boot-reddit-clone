package com.franckcarin.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Vote {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long vote_id;

  @Column(name = "vote_type")
  private VoteType voteType;
  @NotNull
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "post_id", referencedColumnName = "post_id")
  private Post post;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;
}

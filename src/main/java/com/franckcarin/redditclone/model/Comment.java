package com.franckcarin.redditclone.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.time.Instant;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  @NotEmpty
  private String text;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "post_id", referencedColumnName = "post_id")
  private Post post;
  private Instant createdDate;
  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private User user;
}

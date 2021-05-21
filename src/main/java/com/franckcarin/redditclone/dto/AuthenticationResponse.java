package com.franckcarin.redditclone.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
  private String authenticationToken;
  private String username;

}

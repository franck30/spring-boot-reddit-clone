package com.franckcarin.redditclone.controller;

import com.franckcarin.redditclone.dto.AuthenticationResponse;
import com.franckcarin.redditclone.dto.LoginRequest;
import com.franckcarin.redditclone.dto.RefreshTokenRequest;
import com.franckcarin.redditclone.dto.RegisterRequest;
import com.franckcarin.redditclone.service.AuthService;
import com.franckcarin.redditclone.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  private final RefreshTokenService refreshTokenService;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {

    authService.signup(registerRequest);
    return new ResponseEntity<>("User Registration Successfull", HttpStatus.OK);
  }

  @GetMapping("accountVerification/{token}")
  private ResponseEntity<String> verifyAccount(@PathVariable String token){
    authService.verifyAccount(token);
    return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
  }

  @PostMapping("/login")
  public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
      return authService.login(loginRequest);
  }

  @PostMapping("refresh/token")
  public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    return authService.refreshToken(refreshTokenRequest);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    return ResponseEntity.status(HttpStatus.OK).body("Refresh Token Deleted Successfully!!");
  }
}

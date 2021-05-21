package com.franckcarin.redditclone.controller;

import com.franckcarin.redditclone.dto.AuthenticationResponse;
import com.franckcarin.redditclone.dto.LoginRequest;
import com.franckcarin.redditclone.dto.RegisterRequest;
import com.franckcarin.redditclone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;


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
}

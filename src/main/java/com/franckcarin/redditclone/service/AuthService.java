package com.franckcarin.redditclone.service;

import com.franckcarin.redditclone.dto.AuthenticationResponse;
import com.franckcarin.redditclone.dto.LoginRequest;
import com.franckcarin.redditclone.dto.RefreshTokenRequest;
import com.franckcarin.redditclone.dto.RegisterRequest;
import com.franckcarin.redditclone.exception.SpringRedditException;
import com.franckcarin.redditclone.model.NotificationEmail;
import com.franckcarin.redditclone.model.User;
import com.franckcarin.redditclone.model.VerificationToken;
import com.franckcarin.redditclone.repository.UserRepository;
import com.franckcarin.redditclone.repository.VerificationTokenRepository;
import com.franckcarin.redditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

  private final PasswordEncoder passwordEncoder;

  private final UserRepository userRepository;

  private final VerificationTokenRepository verificationTokenRepository;

  private final MailService mailService;

  private final AuthenticationManager authenticationManager;

  private final JwtProvider jwtProvider;

  private final RefreshTokenService refreshTokenService;

  @Transactional
  public void signup(RegisterRequest registerRequest) {

    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setCreated(Instant.now());
    user.setEmail(registerRequest.getEmail());
    user.setEnabled(false);
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

    userRepository.save(user);

//    generateVerificationToken(user);

    String token = generateVerificationToken(user);
    mailService.sendMail(new NotificationEmail("Please activate your account",
            user.getEmail(), "merci de rejoindre la communaute spring reddit, " +
            "Cliquez sur le lien suivant pour activer votre compte : " + "http://localhost:8080/api/auth/accountVerification/" + token));
  }

  private String generateVerificationToken(User user) {
    String token = UUID.randomUUID().toString();
    VerificationToken verificationToken = new VerificationToken();
    verificationToken.setToken(token);
    verificationToken.setUser(user);

    verificationTokenRepository.save(verificationToken);

    return token;
  }

  @Transactional(readOnly = true)
  public User getCurrentUser() {
    org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
            getContext().getAuthentication().getPrincipal();
    return userRepository.findByUsername(principal.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getUsername()));
  }


  public void verifyAccount(String token) {
    Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
    verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
    fetchUserAndEnable(verificationToken.get());
  }

  @Transactional
  public void fetchUserAndEnable(VerificationToken verificationToken) {
    String username = verificationToken.getUser().getUsername();
    User user = userRepository.findByUsername(username).orElseThrow(() -> new SpringRedditException("User not found with name - " + username));
    user.setEnabled(true);
    userRepository.save(user);
  }

  public AuthenticationResponse login(LoginRequest loginRequest) {
    Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
            loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authenticate);
    String token = jwtProvider.generateToken(authenticate);
    return AuthenticationResponse.builder()
            .authenticationToken(token)
            .refreshToken(refreshTokenService.generateRefreshToken().getToken())
            .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
            .username(loginRequest.getUsername())
            .build();
  }

  public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

    refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
    String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());

    return AuthenticationResponse.builder()
            .authenticationToken(token)
            .refreshToken(refreshTokenRequest.getRefreshToken())
            .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
            .username(refreshTokenRequest.getUsername())
            .build();

  }

  public boolean isLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
  }
}

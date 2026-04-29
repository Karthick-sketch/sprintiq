package com.karthi.sprintiq.auth;

import com.karthi.sprintiq.auth.dto.AuthResponseDTO;
import com.karthi.sprintiq.auth.dto.LoginRequestDTO;
import com.karthi.sprintiq.auth.dto.RegisterRequestDTO;
import com.karthi.sprintiq.constants.SecurityConstants;
import com.karthi.sprintiq.exception.EmailAlreadyExistsException;
import com.karthi.sprintiq.exception.InactiveAccountException;
import com.karthi.sprintiq.exception.InvalidCredentialsException;
import com.karthi.sprintiq.exception.InvalidTokenException;
import com.karthi.sprintiq.user.entity.User;
import com.karthi.sprintiq.user.enums.Role;
import com.karthi.sprintiq.user.enums.UserStatus;
import com.karthi.sprintiq.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;

  public AuthResponseDTO register(RegisterRequestDTO request) {
    Optional<User> user = userRepository.findByEmail(
      request.getEmail().toLowerCase()
    );
    if (user.isEmpty()) {
      User newUser = User.builder()
        .name(request.getName())
        .email(request.getEmail().toLowerCase())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .status(UserStatus.ACTIVE)
        .build();
      userRepository.save(newUser);
      return buildAuthResponse(newUser.getEmail());
    }

    User existingUser = user.get();
    if (existingUser.getStatus().equals(UserStatus.ACTIVE)) {
      throw new EmailAlreadyExistsException(request.getEmail());
    } else if (existingUser.getStatus().equals(UserStatus.INACTIVE)) {
      throw new InactiveAccountException(request.getEmail());
    }

    existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
    existingUser.setStatus(UserStatus.ACTIVE);
    userRepository.save(existingUser);
    return buildAuthResponse(existingUser.getEmail());
  }

  public AuthResponseDTO login(LoginRequestDTO request) {
    String email = request.getEmail().toLowerCase();
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(email, request.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new InvalidCredentialsException();
    }

    return buildAuthResponse(email);
  }

  public AuthResponseDTO refreshToken(String refreshToken) {
    String email = jwtService.extractEmail(refreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    if (
      !jwtService.isTokenValid(refreshToken, userDetails) ||
      !jwtService.isRefreshToken(refreshToken)
    ) {
      throw new InvalidTokenException("Invalid refresh token");
    }

    return buildAuthResponse(email);
  }

  // ──────────────────────────────────────────────
  //  Private helpers
  // ──────────────────────────────────────────────

  /**
   * Generates access + refresh tokens and wraps them in a response DTO.
   */
  private AuthResponseDTO buildAuthResponse(String email) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    return AuthResponseDTO.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .tokenType(SecurityConstants.BEARER)
      .expiresIn(jwtService.getAccessTokenExpiration())
      .build();
  }
}

package com.karthi.sprintiq.auth;

import com.karthi.sprintiq.auth.dto.AuthResponseDTO;
import com.karthi.sprintiq.auth.dto.LoginRequestDTO;
import com.karthi.sprintiq.auth.dto.RegisterRequestDTO;
import com.karthi.sprintiq.constants.SecurityConstants;
import com.karthi.sprintiq.user.entity.User;
import com.karthi.sprintiq.user.enums.Role;
import com.karthi.sprintiq.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new RuntimeException("Email already exists: " + request.getEmail());
    }

    User user = User.builder()
      .name(request.getName())
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .role(Role.USER)
      .build();

    userRepository.save(user);

    UserDetails userDetails = userDetailsService.loadUserByUsername(
      user.getEmail()
    );
    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    return AuthResponseDTO.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .tokenType(SecurityConstants.BEARER)
      .expiresIn(jwtService.getAccessTokenExpiration())
      .build();
  }

  public AuthResponseDTO login(LoginRequestDTO request) {
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword()
      )
    );

    UserDetails userDetails = userDetailsService.loadUserByUsername(
      request.getEmail()
    );
    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    return AuthResponseDTO.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .tokenType(SecurityConstants.BEARER)
      .expiresIn(jwtService.getAccessTokenExpiration())
      .build();
  }

  public AuthResponseDTO refreshToken(String refreshToken) {
    String email = jwtService.extractEmail(refreshToken);
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    if (
      !jwtService.isTokenValid(refreshToken, userDetails) ||
      !jwtService.isRefreshToken(refreshToken)
    ) {
      throw new RuntimeException("Invalid refresh token");
    }

    String newAccessToken = jwtService.generateAccessToken(userDetails);
    String newRefreshToken = jwtService.generateRefreshToken(userDetails);

    return AuthResponseDTO.builder()
      .accessToken(newAccessToken)
      .refreshToken(newRefreshToken)
      .tokenType(SecurityConstants.BEARER)
      .expiresIn(jwtService.getAccessTokenExpiration())
      .build();
  }
}

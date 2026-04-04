package com.karthi.sprintiq.auth;

import com.karthi.sprintiq.auth.dto.AuthResponseDTO;
import com.karthi.sprintiq.auth.dto.LoginRequestDTO;
import com.karthi.sprintiq.auth.dto.RegisterRequestDTO;
import com.karthi.sprintiq.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @Value("${jwt.refresh-token-expiration}")
  private long refreshTokenExpiration;

  @PostMapping("/register")
  public ResponseEntity<AuthResponseDTO> register(
    @RequestBody RegisterRequestDTO request
  ) {
    AuthResponseDTO authResponse = authService.register(request);
    String cookie = setRefreshTokenCookie(authResponse.getRefreshToken());
    authResponse.setRefreshToken(null);
    return ResponseEntity.status(HttpStatus.CREATED)
      .header(HttpHeaders.SET_COOKIE, cookie)
      .body(authResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(
    @RequestBody LoginRequestDTO request
  ) {
    AuthResponseDTO authResponse = authService.login(request);
    String cookie = setRefreshTokenCookie(authResponse.getRefreshToken());
    authResponse.setRefreshToken(null);
    return ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, cookie)
      .body(authResponse);
  }

  @PostMapping("/refresh")
  public ResponseEntity<AuthResponseDTO> refresh(
    @CookieValue(
      name = SecurityConstants.REFRESH_TOKEN_COOKIE,
      required = false
    ) String refreshToken
  ) {
    if (refreshToken == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    AuthResponseDTO authResponse = authService.refreshToken(refreshToken);
    String cookie = setRefreshTokenCookie(authResponse.getRefreshToken());
    authResponse.setRefreshToken(null);
    return ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, cookie)
      .body(authResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logout() {
    String cookie = clearRefreshTokenCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie).build();
  }

  private String setRefreshTokenCookie(String refreshToken) {
    return buildRefreshTokenCookie(refreshToken, refreshTokenExpiration / 1000);
  }

  private String clearRefreshTokenCookie() {
    return buildRefreshTokenCookie("", 0);
  }

  private String buildRefreshTokenCookie(String value, long maxAge) {
    return ResponseCookie.from(SecurityConstants.REFRESH_TOKEN_COOKIE, value)
      .httpOnly(true)
      .sameSite("Lax")
      .secure(false)
      .path("/api/auth")
      .maxAge(maxAge)
      .build()
      .toString();
  }
}

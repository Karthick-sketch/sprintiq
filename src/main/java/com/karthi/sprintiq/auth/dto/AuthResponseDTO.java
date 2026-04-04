package com.karthi.sprintiq.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {

  private String accessToken;
  private String refreshToken;
  private String tokenType;
  private Long expiresIn;
}

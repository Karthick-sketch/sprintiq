package com.karthi.sprintiq.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDTO {

  private String accessToken;

  @JsonIgnore
  private String refreshToken;

  private String tokenType;
  private Long expiresIn;
}
